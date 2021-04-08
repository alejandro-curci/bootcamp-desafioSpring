package com.desafiospring.desafio.services;

import com.desafiospring.desafio.dtos.ClienteDTO;
import com.desafiospring.desafio.dtos.StatusDTO;
import com.desafiospring.desafio.exceptions.ClientException;

import java.util.List;

public interface ClientesServicio {
    public StatusDTO darDeAlta(ClienteDTO data, String pass) throws ClientException;

    public List<ClienteDTO> listarClientes(String provincia) throws ClientException;
}
