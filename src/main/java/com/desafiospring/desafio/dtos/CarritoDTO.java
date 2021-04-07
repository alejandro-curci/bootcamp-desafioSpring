package com.desafiospring.desafio.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CarritoDTO {
    private int userID;
    private int totalAcumulado;
    private List<ProductoCompraDTO> articulosEnCarrito;
}
