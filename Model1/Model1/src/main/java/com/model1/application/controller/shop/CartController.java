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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

//    //Xem giỏ hàng
    @GetMapping("/api/cart")
    public ResponseEntity<List<ViewCartDTO>> getCart() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUser().getId();

            List<ViewCartDTO> cartItems = cartService.getCartItemsByUserId(userId);
            for(var item: cartItems)
            {
                System.out.println("Hello" +item);
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
    public ResponseEntity<Map<String, String>> addToCart(@RequestBody CartAddDTO cartAddDTO) {
        try {
            cartService.addToCart(cartAddDTO);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Đã thêm sản phẩm vào giỏ hàng."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Đã xảy ra lỗi khi thêm vào giỏ hàng."));
        }
    }

    // Xóa sản phẩm
    @DeleteMapping("/api/cart/remove")
    public ResponseEntity<String> removeCartItem(@RequestParam Long cartId) {
        try {
            cartService.removeFromCart(cartId);
            return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi xóa sản phẩm.");
        }
    }

    // Sua so luong sản phẩm
    @PutMapping("/api/cart/update")
    public ResponseEntity<Map<String, String>> updateCartItem(@RequestBody CartUpdateDTO cartUpdateDTO) {
        try {
            cartService.updateFromCart(cartUpdateDTO);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Cập nhật giỏ hàng thành công!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Có lỗi xảy ra khi cập nhật giỏ hàng."));
        }
    }

}





