package com.desafiospring.desafio.dtos;

import lombok.Data;

@Data
public class ProductoDTO {
    private int id;
    private String name;
    private String category;
    private String brand;
    private int price;
    private int quantity;
    private boolean freeShipping;
    private String prestige;
}
