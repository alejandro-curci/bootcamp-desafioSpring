package com.desafiospring.desafio.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PayloadDTO {
    private List<ProductoCompraDTO> articles;
}
