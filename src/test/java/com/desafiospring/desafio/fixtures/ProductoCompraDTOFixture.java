package com.desafiospring.desafio.fixtures;

import com.desafiospring.desafio.dtos.ProductoCompraDTO;
import com.desafiospring.desafio.dtos.ProductoDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductoCompraDTOFixture {

    // wrong purchase
    public static List<ProductoCompraDTO> defaultWrongList() {
        List<ProductoCompraDTO> productos = new ArrayList<>();
        productos.add(ProductoCompraDTOFixture.default2());
        productos.add(ProductoCompraDTOFixture.default3());
        return productos;
    }

    // successful purchase
    public static List<ProductoCompraDTO> defaultSuccessfulList() {
        List<ProductoCompraDTO> productos = new ArrayList<>();
        productos.add(ProductoCompraDTOFixture.default1());
        productos.add(ProductoCompraDTOFixture.default4());
        return productos;
    }

    public static ProductoCompraDTO default1() {
        ProductoCompraDTO producto = new ProductoCompraDTO();
        producto.setProductId(1);
        producto.setName("Pelota");
        producto.setBrand("Adidas");
        producto.setQuantity(6);
        return producto;
    }

    public static ProductoCompraDTO default2() {
        ProductoCompraDTO producto = new ProductoCompraDTO();
        producto.setProductId(2);
        producto.setName("Martillo");
        producto.setBrand("Acindar");
        producto.setQuantity(25);
        return producto;
    }

    public static ProductoCompraDTO default3() {
        ProductoCompraDTO producto = new ProductoCompraDTO();
        producto.setProductId(3);
        producto.setName("Contenedor");
        producto.setBrand("Tenaris");
        producto.setQuantity(7);
        return producto;
    }

    public static ProductoCompraDTO default4() {
        ProductoCompraDTO producto = new ProductoCompraDTO();
        producto.setProductId(4);
        producto.setName("Autito");
        producto.setBrand("Hot Wheels");
        producto.setQuantity(3);
        return producto;
    }
}
