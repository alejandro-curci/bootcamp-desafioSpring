package com.desafiospring.desafio.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TicketDTO {
    private int id;
    private List<ProductoCompraDTO> articles;
    private int total;
}
