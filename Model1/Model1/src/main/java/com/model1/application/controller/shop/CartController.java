package com.model1.application.controller.shop;

import com.model1.application.entity.Cart;
import com.model1.application.model.dto.CartAddDTO;
import com.model1.application.model.dto.CartUpdateDTO;
import com.model1.application.model.dto.ViewCartDTO;
import com.model1.application.security.CustomUserDetails;
import com.model1.application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private static final int MAX_AGE_COOKIE = 7 * 24 * 60 * 60; // 7 ngày, đồng bộ với /api/login

    @GetMapping("/api/cart")
    public ResponseEntity<List<ViewCartDTO>> getCart() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUser().getId();

            List<ViewCartDTO> cartItems = cartService.getCartItemsByUserId(userId);
            for (var item : cartItems) {
                System.out.println("Hello" + item);
            }
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        return "shop/cart";
    }

    @PostMapping("/api/cart/add")
    public ResponseEntity<Map<String, String>> addToCart(@RequestBody CartAddDTO cartAddDTO, HttpServletResponse response) {
        try {
            cartService.addToCart(cartAddDTO);
            updateCartItemCountCookie(response); // Cập nhật cookie
            return ResponseEntity.ok(Map.of("status", "success", "message", "Đã thêm sản phẩm vào giỏ hàng."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Đã xảy ra lỗi khi thêm vào giỏ hàng."));
        }
    }

    @DeleteMapping("/api/cart/remove")
    public ResponseEntity<String> removeCartItem(@RequestParam Long cartId, HttpServletResponse response) {
        try {
            cartService.removeFromCart(cartId);
            updateCartItemCountCookie(response); // Cập nhật cookie
            return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi xóa sản phẩm.");
        }
    }

    @PutMapping("/api/cart/update")
    public ResponseEntity<Map<String, String>> updateCartItem(@RequestBody CartUpdateDTO cartUpdateDTO, HttpServletResponse response) {
        try {
            cartService.updateFromCart(cartUpdateDTO);
            updateCartItemCountCookie(response); // Cập nhật cookie
            return ResponseEntity.ok(Map.of("status", "success", "message", "Cập nhật giỏ hàng thành công!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Có lỗi xảy ra khi cập nhật giỏ hàng."));
        }
    }

    @GetMapping("/api/cart/count")
    public ResponseEntity<Integer> getCartItemCount(HttpServletResponse response) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUser().getId();
            int itemCount = cartService.getCartItemsByUserId(userId).size();
            updateCartItemCountCookie(response); // Cập nhật cookie
            System.out.println("Fetching cart count for userId: " + userId + ", itemCount: " + itemCount); // Debug
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            System.err.println("Error fetching cart count: " + e.getMessage()); // Debug
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private void updateCartItemCountCookie(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUser().getId();
            int cartItemCount = cartService.getCartItemsByUserId(userId).size();
            Cookie cartCookie = new Cookie("CART_ITEM_COUNT", String.valueOf(cartItemCount));
            cartCookie.setMaxAge(MAX_AGE_COOKIE);
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
        }
    }
}