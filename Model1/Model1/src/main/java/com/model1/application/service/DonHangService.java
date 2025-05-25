package com.model1.application.service;

import com.model1.application.entity.DonHang;
import com.model1.application.model.dto.ConfirmOrderDTO;
import com.model1.application.model.dto.CreateOrderDTO;
import com.model1.application.model.dto.DonHangDTO;
import com.model1.application.model.dto.OrderInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DonHangService {
    DonHang createDonHang(CreateOrderDTO dto);
    DonHangDTO getDonHangById(Long id);
    DonHang confirmOrder(ConfirmOrderDTO dto);
    List<DonHang> getListOrderOfPersonByStatus(int status, long userId);
    DonHang getDonHangByOrderId(Long id);
    void huyDonHang(Long id);
    Page<DonHang> adminGetListOrders(String id, String name, String phone, String status, Long total, String sort, int page);
    void capnhatDonHang(Long id, int status);
}
