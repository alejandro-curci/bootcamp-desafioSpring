package com.desafiospring.desafio.fixtures;

import com.desafiospring.desafio.dtos.CarritoDTO;

public class CarritoDTOFixture {

    // build an object CarritoDTO
    public static CarritoDTO crearCarrito() {
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setUserID(1);
        carritoDTO.setTotalAcumulado(6*6000+3*4300);
        carritoDTO.setArticulosEnCarrito(ProductoCompraDTOFixture.defaultSuccessfulList());
        return carritoDTO;
    }
}
