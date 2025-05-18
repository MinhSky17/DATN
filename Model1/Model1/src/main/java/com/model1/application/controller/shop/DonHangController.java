package com.model1.application.controller.shop;

import com.model1.application.entity.DonHang;
import com.model1.application.entity.ProductColor;
import com.model1.application.exception.NotFoundException;
import com.model1.application.model.dto.ConfirmOrderDTO;
import com.model1.application.model.dto.CreateOrderDTO;
import com.model1.application.model.dto.DetailProductInfoDTO;
import com.model1.application.model.dto.DonHangDTO;
import com.model1.application.service.DonHangService;
import com.model1.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DonHangController {
    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ProductService productService;

    @PostMapping("/api/don-hang")
    public ResponseEntity<DonHang> createDonHang(@RequestBody CreateOrderDTO dto) {
        DonHang donHang = donHangService.createDonHang(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(donHang);
    }

    @GetMapping("/dat-hang")
    public String getOrderPage(@RequestParam("orderId") Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "shop/payment";
    }

    @GetMapping("/api/don-hang/{id}")
    public ResponseEntity<DonHangDTO> getDonHangById(@PathVariable Long id){
        try{
            DonHangDTO dto = donHangService.getDonHangById(id);
            return ResponseEntity.ok(dto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DonHangDTO());
        }
    }

    @PostMapping("/api/don-hang/confirm")
    public ResponseEntity<DonHang> confirmDonHang(@RequestBody ConfirmOrderDTO dto){
        DonHang donHang = donHangService.confirmOrder(dto);
        return  ResponseEntity.ok(donHang);
    }
}
