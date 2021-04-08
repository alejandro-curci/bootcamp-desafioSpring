package com.desafiospring.desafio.dtos;

import lombok.Data;

@Data
public class ClienteDataDTO {
    private String nombre;
    private String email;
    private String password;
    private String provincia;
}
