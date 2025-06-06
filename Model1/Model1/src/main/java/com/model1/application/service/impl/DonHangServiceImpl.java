package com.model1.application.service.impl;

import com.model1.application.entity.*;
import com.model1.application.model.dto.*;
import com.model1.application.repository.*;
import com.model1.application.security.CustomUserDetails;
import com.model1.application.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.model1.application.config.Contant.*;

@Component
public class DonHangServiceImpl implements DonHangService {
    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Transactional
    public DonHang createDonHang(CreateOrderDTO dto) {
        // 1. Lấy user hiện tại từ JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        // Kiểm tra user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user"));

        // 2. Kiểm tra sản phẩm trong giỏ hàng
        List<CartItemDTO> cartItems = dto.getCartItems();
//        List<Cart> userCartItems = cartRepository.findByUserIdAndCartItems(userId, cartItems);
//        if (userCartItems.size() != cartItems.size()) {
//            throw new IllegalArgumentException("Một số sản phẩm hoặc màu không có trong giỏ hàng");
//        }

        // 3. Tạo đơn hàng
        DonHang donHang = new DonHang();
        donHang.setUserId(userId);
        donHang.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        //donHang.setStatus(1);
        donHang = donHangRepository.save(donHang);

        // 4. Duyệt qua từng item trong giỏ hàng
        for (CartItemDTO item : cartItems) {
            String productId = item.getProductId();
            Long colorId = item.getColorId();
            int quantity = item.getQuantity();

            // Lấy sản phẩm và kiểm tra
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            // Lấy màu sản phẩm và kiểm tra tồn kho
            ProductColor productColor = productColorRepository.findById(colorId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy màu"));

            // Kiểm tra số lượng
            if (productColor.getQuantity() < quantity) {
                throw new RuntimeException("Số lượng màu trong kho không đủ");
            }

            // Cập nhật số lượng
//            productColor.setQuantity(productColor.getQuantity() - quantity);
//            productColorRepository.save(productColor);

            // Tạo chi tiết đơn hàng
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(donHang.getId());
            orderDetail.setProductId(productId);
            orderDetail.setColorId(colorId);
            orderDetail.setQuantity(quantity);
            orderDetail.setCreateAt(new Timestamp(System.currentTimeMillis()));
            orderDetailRepository.save(orderDetail);
        }

        // 5. Xóa các sản phẩm được chọn khỏi giỏ hàng
        cartRepository.deleteByUserIdAndCartItems(userId, cartItems);

        return donHang;
    }


    @Transactional
    public DonHangDTO getDonHangById(Long id){
        DonHang donHang = donHangRepository.findById(id).orElseThrow(()->new RuntimeException("Khong tim thay don hang"));

        List<OrderDetail> details = orderDetailRepository.findByOrderId(id);

        DonHangDTO dto = new DonHangDTO();
        dto.setId(donHang.getId());
        dto.setStatus(donHang.getStatus());
        dto.setCreatedAt(donHang.getCreatedAt());

        List<OrderItemDTO> items = details.stream().map(detail -> {
            Product product = productRepository.findById(detail.getProductId()).orElse(null);
            ProductColor productColor = productColorRepository.findById(detail.getColorId()).orElse(null);

            OrderItemDTO item = new OrderItemDTO();
            item.setProductId(detail.getProductId());
            item.setProductName(product != null? product.getName() : "N/A");
            item.setProductImage(product != null? product.getImages().get(0): "N/A");
            item.setCode(productColor != null ? productColor.getCode() : "N/A");
            item.setColorId(detail.getColorId());
            item.setPrice(product != null? product.getSalePrice() : 0);
            item.setQuantity(detail.getQuantity());
            return item;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }


    @Transactional
    public DonHang getDonHangByOrderId(Long id){
        return donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
    }

    @Transactional
    public DonHang confirmOrder(ConfirmOrderDTO dto) {
        DonHang donHang = donHangRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (dto.getReceiverName() == null || dto.getReceiverName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người nhận không được rỗng");
        }
        if (dto.getReceiverPhone() == null || !dto.getReceiverPhone().matches("^(09|03|07|08|05)[0-9]{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        if (dto.getReceiverAddress() == null || dto.getReceiverAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được rỗng");
        }
        if (dto.getTotalPrice() < 0) {
            throw new IllegalArgumentException("Tổng tiền không hợp lệ");
        }
        if (dto.getPaymentMethod() == null || !List.of("cod", "vnpay", "aftee", "card", "momo", "fundiin").contains(dto.getPaymentMethod())) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
        }
        if (dto.getCouponCode() != null && !dto.getCouponCode().trim().isEmpty()) {
            Promotion voucher = promotionRepository.checkPromotion(dto.getCouponCode());
            if (voucher == null) {
                throw new IllegalArgumentException("Mã khuyến mãi không tồn tại");
            }
        }

        donHang.setReceiverName(dto.getReceiverName());
        donHang.setReceiverPhone(dto.getReceiverPhone());
        donHang.setReceiverAddress(dto.getReceiverAddress());
        donHang.setNote(dto.getNote());
        donHang.setCouponCode(dto.getCouponCode());
        donHang.setPrice(dto.getPrice());
        donHang.setTotalPrice(dto.getTotalPrice());
        donHang.setPaymentMethod(dto.getPaymentMethod());
        donHang.setStatus(1);
        donHang.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        //Cap nhat so luong mau san pham
        List<OrderDetail> details = orderDetailRepository.findByOrderId(donHang.getId());
        for (OrderDetail detail : details) {
            ProductColor productColor = productColorRepository.findById(detail.getColorId()).orElse(null);
            productColor.setQuantity(productColor.getQuantity() - detail.getQuantity());
            productColorRepository.save(productColor);

            Product product = productRepository.findById(detail.getProductId()).orElse(null);
            product.setTotalSold(product.getTotalSold() + detail.getQuantity());
            productRepository.save(product);
        }

        return donHangRepository.save(donHang);
    }

    @Override
    public List<DonHang> getListOrderOfPersonByStatus(int status, long userId) {
        List<DonHang> list = donHangRepository.findByStatusAndUserIdOrderByCreatedAtDesc(status, userId);
        return list;
    }

    @Transactional
    public void huyDonHang(Long id){
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        donHang.setStatus(5);
        donHang.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        //Cap nhat so luong mau san pham
        List<OrderDetail> details = orderDetailRepository.findByOrderId(donHang.getId());
        for (OrderDetail detail : details) {
            ProductColor productColor = productColorRepository.findById(detail.getColorId()).orElse(null);
            productColor.setQuantity(productColor.getQuantity() + detail.getQuantity());
            productColorRepository.save(productColor);

            Product product = productRepository.findById(detail.getProductId()).orElse(null);
            product.setTotalSold(product.getTotalSold() + detail.getQuantity());
            productRepository.save(product);
        }

        donHangRepository.save(donHang);
    }

    @Transactional
    public void capnhatDonHang(Long id, int status){
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        donHang.setStatus(status);
        donHang.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        donHangRepository.save(donHang);
    }

    @Transactional
    public Page<DonHang> adminGetListOrders(String id, String name, String phone, String status, Long total, String sort, int page) {
        if (page < 1) {
            page = 1;
        }
        int limit = 10;
        Sort sortBy = Sort.by("created_at").descending();
        if ("price-asc".equals(sort)) {
            sortBy = Sort.by("total_price").ascending();
        } else if ("price-desc".equals(sort)) {
            sortBy = Sort.by("total_price").descending();
        }
        Pageable pageable = PageRequest.of(page - 1, limit, sortBy);
        Integer statusInt = status != null && !status.isEmpty() ? Integer.parseInt(status) : null;
        return donHangRepository.adminGetListOrder(
                id != null && !id.isEmpty() ? id : null,
                name != null && !name.isEmpty() ? name : null,
                phone != null && !phone.isEmpty() ? phone : null,
                statusInt,
                total,
                pageable
        );
    }
}