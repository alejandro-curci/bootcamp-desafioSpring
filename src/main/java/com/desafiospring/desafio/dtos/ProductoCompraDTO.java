package com.desafiospring.desafio.dtos;

import lombok.Data;

@Data
public class ProductoCompraDTO {
    private int productId;
    private String name;
    private String brand;
    private int quantity;
}
