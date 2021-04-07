package com.desafiospring.desafio.dtos;

import lombok.Data;

@Data
public class ProductoStringDTO {
    private String id;
    private String name;
    private String category;
    private String brand;
    private String price;
    private String quantity;
    private String freeShipping;
    private String prestige;
}
