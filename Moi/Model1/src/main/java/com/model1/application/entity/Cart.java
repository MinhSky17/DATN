package com.model1.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "color_id", nullable = false)
    private Long colorId;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "create_at", nullable = true)
    private Timestamp createAt;

    @Column(name = "update_at", nullable = true)
    private Timestamp updateAt;
}
