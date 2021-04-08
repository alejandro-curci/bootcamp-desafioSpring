package com.desafiospring.desafio.controllers;

import com.desafiospring.desafio.dtos.PayloadDTO;
import com.desafiospring.desafio.dtos.StatusDTO;
import com.desafiospring.desafio.exceptions.ProductException;
import com.desafiospring.desafio.exceptions.UserException;
import com.desafiospring.desafio.services.ProductosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductosControlador {

    @Autowired
    private ProductosServicio productoServicio;

    @GetMapping("/articles")
    public ResponseEntity listarProductos(@RequestParam(required = false, defaultValue = "") String category,
                                          @RequestParam(required = false, defaultValue = "") String brand,
                                          @RequestParam(required = false, defaultValue = "") String price,
                                          @RequestParam(required = false, defaultValue = "") String freeShipping,
                                          @RequestParam(required = false, defaultValue = "") String prestige,
                                          @RequestParam(required = false, defaultValue = "") String orden) throws ProductException {
        return new ResponseEntity(productoServicio.procesarConsulta(category, brand, price, freeShipping, prestige, orden), HttpStatus.OK);
    }

    @PostMapping("/purchase-request")
    public ResponseEntity procesarCompra(@RequestBody PayloadDTO payload) throws ProductException {
        return new ResponseEntity(productoServicio.generarTicket(payload), HttpStatus.OK);
    }

    @PostMapping("/carrito")
    public ResponseEntity agregar(@RequestParam(required = false, defaultValue = "") String userID, @RequestBody(required = false) PayloadDTO payload) throws ProductException, UserException {
        return new ResponseEntity(productoServicio.agregarAlCarrito(userID, payload), HttpStatus.OK);
    }

    @PostMapping("/carrito/comprar")
    public ResponseEntity comprar(@RequestParam(required = false, defaultValue = "") String userID) throws ProductException, UserException {
        return new ResponseEntity(productoServicio.comprarCarrito(userID), HttpStatus.OK);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity handleProductExcepcion(ProductException e) {
        return new ResponseEntity(new StatusDTO(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity handleUserExcepcion(UserException e) {
        return new ResponseEntity(new StatusDTO(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
