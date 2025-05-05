package com.model1.application.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Setter
@Getter
public class ShortProductInfoDTO {
    private String id;

    private String name;

    private long price;

    List<Integer> availableColors;

    public ShortProductInfoDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShortProductInfoDTO(String id, String name, long price, Object availableColors) {
        this.id = id;
        this.name = name;
        this.price = price;
        if (availableColors != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.availableColors = mapper.readValue((String) availableColors, new TypeReference<List<Integer>>(){});
            } catch (IOException e) {
                this.availableColors = null;
            }
        }
    }
}