package com.model1.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

    private long id;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String note;

    private long price;

    private long totalPrice;

    private int quantity;

    private int status;

    private String statusText;

    private Timestamp createdAt;

    private Timestamp modifiedAt;

    private Long userId;

    private Long voucherId;

    private String couponCode;
    private String paymentMethod;

}
