package com.desafiospring.desafio.repositories;

import com.desafiospring.desafio.dtos.ClienteDataDTO;
import com.desafiospring.desafio.exceptions.ClientException;

import java.util.Map;

public interface ClienteRepositorio {
    public void guardarCliente(ClienteDataDTO cliente) throws ClientException;

    public Map<Integer, ClienteDataDTO> cargarClientes();
}
