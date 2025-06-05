package com.model1.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "donhang")
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone")
    private String receiverPhone;
    @Column(name = "receiver_address")
    private String receiverAddress;
    @Column(name = "note")
    private String note;
    @Column(name = "price")
    private long price;
    @Column(name = "total_price")
    private long totalPrice;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "status")
    private int status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "payment_mothod")
    private String paymentMethod;

}