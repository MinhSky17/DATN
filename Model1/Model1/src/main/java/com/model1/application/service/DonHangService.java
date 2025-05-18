package com.model1.application.service;

import com.model1.application.entity.DonHang;
import com.model1.application.model.dto.ConfirmOrderDTO;
import com.model1.application.model.dto.CreateOrderDTO;
import com.model1.application.model.dto.DonHangDTO;
import org.springframework.stereotype.Service;

@Service
public interface DonHangService {
    DonHang createDonHang(CreateOrderDTO dto);
    DonHangDTO getDonHangById(Long id);
    DonHang confirmOrder(ConfirmOrderDTO dto);
}
