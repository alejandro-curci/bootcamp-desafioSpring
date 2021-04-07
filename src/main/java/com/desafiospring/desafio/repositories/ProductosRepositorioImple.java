package com.desafiospring.desafio.repositories;

import com.desafiospring.desafio.dtos.ProductoDTO;
import com.desafiospring.desafio.dtos.ProductoStringDTO;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductosRepositorioImple implements ProductosRepositorio {

    // cargo el archivo CSV y devuelvo una lista de productos (con todos sus campos tipo String)
    private List<ProductoStringDTO> cargarRepositorio() {
        BufferedReader bufferLectura = null;
        List<ProductoStringDTO> productos = new ArrayList<>();
        int fila = 0;
        try {
            bufferLectura = new BufferedReader(new FileReader("src/main/resources/dbProductos.csv"));
            String linea = bufferLectura.readLine();

            while (linea != null) {
                String[] celdas = linea.split(",");
                // salteo la primer fila (títulos)
                if(fila>0) {
                    ProductoStringDTO productoDto = new ProductoStringDTO();
                    productoDto.setId(celdas[0]);
                    productoDto.setName(celdas[1]);
                    productoDto.setCategory(celdas[2]);
                    productoDto.setBrand(celdas[3]);
                    productoDto.setPrice(celdas[4]);
                    productoDto.setQuantity(celdas[5]);
                    productoDto.setFreeShipping(celdas[6]);
                    productoDto.setPrestige(celdas[7]);
                    productos.add(productoDto);
                }

                linea = bufferLectura.readLine();
                fila++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return productos;
    }

    // Genero el repositorio final con una lista de productos de tipo ProductoDTO
    // Se hacen las conversiones de dato correspondientes
    @Override
    public List<ProductoDTO> generarRepositorio() {
        List<ProductoStringDTO> productosString = cargarRepositorio();
        List<ProductoDTO> productos = new ArrayList<>();
        for(ProductoStringDTO p : productosString) {
            ProductoDTO nuevoProducto = new ProductoDTO();
            try {
                nuevoProducto.setId(Integer.parseInt(p.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            nuevoProducto.setName(p.getName());
            nuevoProducto.setCategory(p.getCategory());
            nuevoProducto.setBrand(p.getBrand());
            try {
                String numero = p.getPrice();
                numero = numero.replaceAll("\\.","").replaceAll("\\$","");
                nuevoProducto.setPrice(Integer.parseInt(numero));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                nuevoProducto.setQuantity(Integer.parseInt(p.getQuantity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(p.getFreeShipping().equals("SI")) {
                nuevoProducto.setFreeShipping(true);
            } else {
                nuevoProducto.setFreeShipping(false);
            }
            nuevoProducto.setPrestige(p.getPrestige());
            productos.add(nuevoProducto);
        }
        return productos;
    }
}
