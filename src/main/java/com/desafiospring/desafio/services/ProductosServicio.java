package com.desafiospring.desafio.services;

import com.desafiospring.desafio.dtos.CarritoDTO;
import com.desafiospring.desafio.dtos.PayloadDTO;
import com.desafiospring.desafio.dtos.ProductoDTO;
import com.desafiospring.desafio.dtos.ResponseDTO;
import com.desafiospring.desafio.exceptions.ProductException;
import com.desafiospring.desafio.exceptions.UserException;

import java.util.List;

public interface ProductosServicio {

    public List<ProductoDTO> procesarConsulta(String category, String brand,
                                              String priceStr, String freeShippingStr,
                                              String prestige, String orden) throws ProductException;

    public ResponseDTO generarTicket(PayloadDTO compra) throws ProductException;

    public CarritoDTO agregarAlCarrito(String userID, PayloadDTO compra) throws ProductException, UserException;

    public ResponseDTO comprarCarrito(String userID) throws UserException, ProductException;
}
