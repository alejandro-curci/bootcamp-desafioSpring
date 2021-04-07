package com.desafiospring.desafio.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusDTO {
    private int code;
    private String message;
}
