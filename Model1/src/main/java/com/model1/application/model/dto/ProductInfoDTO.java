package com.model1.application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductInfoDTO {
    private String id;
    private String name;
    private String slug;
    private long price;
    private long sale;
    private int views;
    private String images;
    private int totalSold;
    private long promotionPrice;

    public ProductInfoDTO(String id, String name, String slug, long price, long sale, int views, String images, int totalSold) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.sale = sale;
        this.views = views;
        this.images = images;
        this.totalSold = totalSold;
    }
}