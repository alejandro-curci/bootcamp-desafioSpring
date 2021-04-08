package com.desafiospring.desafio.controllers;

import com.desafiospring.desafio.dtos.ClienteDTO;
import com.desafiospring.desafio.dtos.StatusDTO;
import com.desafiospring.desafio.exceptions.ClientException;
import com.desafiospring.desafio.services.ClientesServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ClientesControlador {

    @Autowired
    private ClientesServicio clientesServicio;

    @PostMapping("/client-create")
    public ResponseEntity crearCliente(@RequestHeader String password, @RequestBody ClienteDTO cliente) throws ClientException {
        return new ResponseEntity(clientesServicio.darDeAlta(cliente, password), HttpStatus.OK);
    }

    @GetMapping("/client-list")
    public ResponseEntity listarClientes(@RequestParam(required = false, defaultValue = "") String provincia) throws ClientException {
        return new ResponseEntity(clientesServicio.listarClientes(provincia), HttpStatus.OK);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity handleUserExcepcion(ClientException e) {
        return new ResponseEntity(new StatusDTO(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
