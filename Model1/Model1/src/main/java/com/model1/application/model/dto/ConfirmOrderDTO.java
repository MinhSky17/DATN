package com.model1.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ConfirmOrderDTO {
    private Long orderId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;
    private String couponCode;
    private Long price;
    private Long totalPrice;
    private String paymentMethod;
}
