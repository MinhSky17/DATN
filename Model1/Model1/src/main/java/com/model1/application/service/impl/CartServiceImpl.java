package com.model1.application.service.impl;

import com.model1.application.entity.*;
import com.model1.application.model.dto.CartAddDTO;
import com.model1.application.model.dto.CartUpdateDTO;
import com.model1.application.model.dto.ViewCartDTO;
import com.model1.application.repository.*;
import com.model1.application.security.CustomUserDetails;
import com.model1.application.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    //Them san pham vao gio hang
    @Transactional
    public void addToCart(CartAddDTO cartAddDTO){
        // Lấy user hiện tại từ JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId(); // hoặc getId() nếu bạn override sẵn

        // Không cần dùng cartAddDTO.getUserId() nữa
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user"));

        Product product = productRepository.findById(cartAddDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));

        ProductColor productColor = productColorRepository.findById(cartAddDTO.getColorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy màu"));
        // Kiểm tra số lượng
        if (productColor.getQuantity() < cartAddDTO.getQuantity()) {
            throw new RuntimeException("Số lượng màu trong kho không đủ");
        }

        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts) {
            if (cart.getColorId().equals(cartAddDTO.getColorId()) &&
                    cart.getProductId().equals(cartAddDTO.getProductId())) {
                cart.setQuantity(cart.getQuantity() + cartAddDTO.getQuantity());
                cart.setUpdateAt(new Timestamp(System.currentTimeMillis()));
                cartRepository.save(cart);
                return;
            }
        }

        // Nếu chưa có thì thêm mới
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(cartAddDTO.getProductId());
        cart.setColorId(cartAddDTO.getColorId());
        cart.setQuantity(cartAddDTO.getQuantity());
        cart.setCreateAt(new Timestamp(System.currentTimeMillis()));
        cartRepository.save(cart);

    }

    // Xem giỏ hàng
    public List<ViewCartDTO> getCartItemsByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<ViewCartDTO> dtoList = new ArrayList<>();

        for (Cart cart : carts) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));
            ProductColor color = productColorRepository.findById(cart.getColorId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy màu"));

            ViewCartDTO dto = new ViewCartDTO();
            dto.setCartId(cart.getId());
            dto.setProductId(cart.getProductId());
            dto.setColorId(cart.getColorId());
            dto.setProductName(product.getName());
            dto.setColor(color.getCode());
            dto.setQuantity(cart.getQuantity());
            dto.setProductImage(product.getImages().get(0)); // nếu có ảnh
            dto.setPrice(product.getSalePrice());           // nếu có giá

            dtoList.add(dto);
        }

        return dtoList;
    }

    //Xoa khoi gio hang
    public void removeFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng với ID: " + cartId));
        cartRepository.delete(cart);
    }

    //Cap nhat gio hang
    public void updateFromCart(CartUpdateDTO cartUpdateDTO) {
        Cart cart = cartRepository.findById(cartUpdateDTO.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng"));
        cart.setQuantity(cartUpdateDTO.getQuantity());
        cart.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        cartRepository.save(cart);
    }

}




    // Lấy tổng số lượng sản phẩm trong giỏ hàng
//    public int getCartItemCount(Long userId) {
//        Cart cart = cartRepository.findByUserId(userId)
//                .orElse(new Cart()); // Trả về giỏ hàng rỗng nếu không tồn tại
//        return cart.getCartItems().stream()
//                .mapToInt(CartItem::getQuantity)
//                .sum();
//    }
//
//

//
//    // Sửa số lượng
//    @Transactional
//    public void updateCartItemQuantity(Long userId, Long cartItemId, Integer newQuantity) {
//        Cart cart = cartRepository.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
//
//        CartItem cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new RuntimeException("Mục giỏ hàng không tồn tại"));
//
//        if (!cart.getId().equals(cartItem.getCart().getId())) {
//            throw new RuntimeException("Mục giỏ hàng không thuộc giỏ hàng của người dùng");
//        }
//
//        ProductColor productColor = productColorRepository.findById(cartItem.getProductColorId())
//                .orElseThrow(() -> new RuntimeException("Màu sản phẩm không tồn tại"));
//
//        // Kiểm tra số lượng tồn kho
//        int quantityDifference = newQuantity - cartItem.getQuantity();
//        if (quantityDifference > 0 && productColor.getQuantity() < quantityDifference) {
//            throw new RuntimeException("Số lượng tồn kho không đủ cho màu " + productColor.getName());
//        }
//
//        // Cập nhật số lượng
//        cartItem.setQuantity(newQuantity);
//        cartItemRepository.save(cartItem);
//
//        // Cập nhật tồn kho
//        productColor.setQuantity(productColor.getQuantity() - quantityDifference);
//        productColorRepository.save(productColor);
//    }
//



