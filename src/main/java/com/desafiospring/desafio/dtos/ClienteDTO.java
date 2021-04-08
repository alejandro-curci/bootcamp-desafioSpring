package com.desafiospring.desafio.dtos;

import lombok.Data;

@Data
public class ClienteDTO {
    private int dni;
    private String nombre;
    private String email;
    private String provincia;
}
