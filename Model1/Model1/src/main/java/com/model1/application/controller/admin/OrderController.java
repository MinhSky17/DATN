package com.model1.application.controller.admin;

import com.model1.application.entity.DonHang;
import com.model1.application.entity.Order;
import com.model1.application.entity.Promotion;
import com.model1.application.entity.User;
import com.model1.application.exception.BadRequestException;
import com.model1.application.model.dto.*;
import com.model1.application.model.request.CreateOrderRequest;
import com.model1.application.model.request.UpdateDetailOrder;
import com.model1.application.model.request.UpdateStatusOrderRequest;
import com.model1.application.security.CustomUserDetails;
import com.model1.application.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.model1.application.config.Contant.*;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DonHangService donHangService;

//    @GetMapping("/admin/orders")
//    public String getListOrderPage(Model model,
//                                   @RequestParam(defaultValue = "1") Integer page,
//                                   @RequestParam(defaultValue = "", required = false) String id,
//                                   @RequestParam(defaultValue = "", required = false) String name,
//                                   @RequestParam(defaultValue = "", required = false) String phone,
//                                   @RequestParam(defaultValue = "", required = false) String status,
//                                   @RequestParam(defaultValue = "", required = false) String sort) {
//        Page<DonHang> orderPage = donHangService.adminGetListOrders(id, name, phone, status, null, sort, page);
//        model.addAttribute("orderPage", orderPage.getContent());
//        model.addAttribute("totalPages", orderPage.getTotalPages());
//        model.addAttribute("currentPage", orderPage.getPageable().getPageNumber() + 1);
//        model.addAttribute("id", id);
//        model.addAttribute("name", name);
//        model.addAttribute("phone", phone);
//        model.addAttribute("status", status);
//        model.addAttribute("sort", sort);
//        return "admin/order/list";
//    }

    @GetMapping("/admin/orders")
    public String getListOrderPage(Model model,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "", required = false) String id,
                                   @RequestParam(defaultValue = "", required = false) String name,
                                   @RequestParam(defaultValue = "", required = false) String phone,
                                   @RequestParam(defaultValue = "", required = false) String status,
                                   @RequestParam(defaultValue = "", required = false) Long total,
                                   @RequestParam(defaultValue = "", required = false) String sort) {
        try {
            Page<DonHang> orderPage = donHangService.adminGetListOrders(id, name, phone, status, total, sort, page);
            model.addAttribute("orderPage", orderPage.getContent());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("currentPage", orderPage.getPageable().getPageNumber() + 1);
            model.addAttribute("id", id);
            model.addAttribute("name", name);
            model.addAttribute("phone", phone);
            model.addAttribute("status", status);
            model.addAttribute("total", total);
            model.addAttribute("sort", sort);
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage());
            model.addAttribute("orderPage", Collections.emptyList());
            model.addAttribute("totalPages", 0);
            model.addAttribute("currentPage", 1);
            model.addAttribute("id", id);
            model.addAttribute("name", name);
            model.addAttribute("phone", phone);
            model.addAttribute("status", status);
            model.addAttribute("total", total);
            model.addAttribute("sort", sort);
        }
        return "admin/order/list";
    }

    @GetMapping("/admin/orders/update/{id}")
    public String updateOrderPage(Model model, @PathVariable long id) {
        try {
            DonHang order = donHangService.getDonHangByOrderId(id);
            if (order == null) {
                throw new IllegalArgumentException("Đơn hàng không tồn tại");
            }
            // Lấy thông tin người dùng từ userId
            User user = null;
            if (order.getUserId() != null) {
                user = userService.findByUserId(order.getUserId());
            }
            model.addAttribute("order", order);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi lấy thông tin đơn hàng: " + e.getMessage());
            model.addAttribute("order", null);
            model.addAttribute("user", null);
        }
        return "admin/order/edit";
    }

    @PutMapping("/api/admin/orders/update/{id}/{status}")
    public ResponseEntity<Object> updateStatusOrder(@PathVariable long id, @PathVariable int status) {
        try {
            donHangService.capnhatDonHang(id, status);
            return ResponseEntity.ok(Map.of("message", "Cập nhật đơn hàng thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi cập nhật đơn hàng: " + e.getMessage()));
        }
    }


    @GetMapping("/admin/orders/create")
    public String createOrderPage(Model model) {

        //Get list product
        List<ShortProductInfoDTO> products = productService.getAvailableProducts();
        model.addAttribute("products", products);

        // Get list size
        model.addAttribute("sizeVn", SIZE_VN);

//        //Get list valid promotion
        List<Promotion> promotions = promotionService.getAllValidPromotion();
        model.addAttribute("promotions", promotions);
        return "admin/order/create";
    }

    @PostMapping("/api/admin/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Order order = orderService.createOrder(createOrderRequest, user.getId());
        return ResponseEntity.ok(order);
    }

//    @GetMapping("/admin/orders/update/{id}")
//    public String updateOrderPage(Model model, @PathVariable long id) {
//
//        Order order = orderService.findOrderById(id);
//        model.addAttribute("order", order);
//
//        if (order.getStatus() == ORDER_STATUS) {
//            // Get list product to select
//            List<ShortProductInfoDTO> products = productService.getAvailableProducts();
//            model.addAttribute("products", products);
//
//            // Get list valid promotion
//            List<Promotion> promotions = promotionService.getAllValidPromotion();
//            model.addAttribute("promotions", promotions);
//            if (order.getPromotion() != null) {
//                boolean validPromotion = false;
//                for (Promotion promotion : promotions) {
//                    if (promotion.getCouponCode().equals(order.getPromotion().getCouponCode())) {
//                        validPromotion = true;
//                        break;
//                    }
//                }
//                if (!validPromotion) {
//                    promotions.add(new Promotion(order.getPromotion()));
//                }
//            }
//
//            // Check size available
//            boolean sizeIsAvailable = productService.checkProductSizeAvailable(order.getProduct().getId(), order.getSize());
//            model.addAttribute("sizeIsAvailable", sizeIsAvailable);
//        }
//
//        return "admin/order/edit";
//    }

    @PutMapping("/api/admin/orders/update-detail/{id}")
    public ResponseEntity<Object> updateDetailOrder(@Valid @RequestBody UpdateDetailOrder detailOrder, @PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        orderService.updateDetailOrder(detailOrder, id, user.getId());
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @PutMapping("/api/admin/orders/update-status/{id}")
    public ResponseEntity<Object> updateStatusOrder(@Valid @RequestBody UpdateStatusOrderRequest updateStatusOrderRequest, @PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        orderService.updateStatusOrder(updateStatusOrderRequest, id, user.getId());
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @GetMapping("/tai-khoan/lich-su-giao-dich")
    public String getOrderHistoryPage(Model model){

        //Get list order pending
        User user =((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<OrderInfoDTO> orderList = orderService.getListOrderOfPersonByStatus(ORDER_STATUS,user.getId());
        model.addAttribute("orderList",orderList);

        return "shop/order_history";
    }

    @GetMapping("/api/get-order-list")
    public ResponseEntity<Object> getListOrderByStatus(@RequestParam int status) {
        // Validate status
        if (!LIST_ORDER_STATUS.contains(status)) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
        }

        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        //List<OrderInfoDTO> orders = orderService.getListOrderOfPersonByStatus(status, user.getId());
        List<DonHang> orders = donHangService.getListOrderOfPersonByStatus(status, user.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/tai-khoan/lich-su-giao-dich/{id}")
    public String getDetailOrderPage(Model model, @PathVariable Long id) {

        DonHang order = donHangService.getDonHangByOrderId(id);
        if (order == null) {
            return "error/404";
        }
        model.addAttribute("order", order);

        if (order.getStatus() == 1) {
            model.addAttribute("canCancel", true);
        } else {
            model.addAttribute("canCancel", false);
        }

        return "shop/order-detail";
    }

    @PostMapping("/api/cancel-order/{id}")
    public ResponseEntity<Object> cancelOrder(@PathVariable long id) {
        try{
            donHangService.huyDonHang(id);
            return ResponseEntity.ok("Hủy đơn hàng thành công");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi hủy đơn hàng: " + e.getMessage());
        }
    }

}
