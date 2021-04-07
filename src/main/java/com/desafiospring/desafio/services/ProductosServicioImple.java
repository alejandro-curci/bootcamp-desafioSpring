package com.desafiospring.desafio.services;

import com.desafiospring.desafio.dtos.*;
import com.desafiospring.desafio.exceptions.ProductException;
import com.desafiospring.desafio.exceptions.UserException;
import com.desafiospring.desafio.repositories.ProductosRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductosServicioImple implements ProductosServicio {

    @Autowired
    private ProductosRepositorio repositorio;

    private int ticketID;
    private Map<Integer, List<ProductoCompraDTO>> carrito;

    public ProductosServicioImple() {
        ticketID = 1;
        carrito = new HashMap<>();
    }

    // método para controlar la cantidad de parámetros recibidos en el request
    private int controlarInput(String category, String brand, String price, String freeShipping, String prestige) {
        String parametros[] = {category, brand, price, freeShipping, prestige};
        int input = 0;
        for (String s : parametros) {
            if (s.equals("")) {
                input++;
            }
        }
        return input;
    }

    // método para listar todos los productos disponibles
    private List<ProductoDTO> listarProductosDisponibles() {
        return repositorio.generarRepositorio();
    }

    // método para listar los productos filtrados (con control en caso de no encontrar ningún producto con esa característica)
    private List<ProductoDTO> filtrarProductos(String category, String brand, String priceStr, String freeShippingStr, String prestige) throws ProductException {
        List<ProductoDTO> productos = repositorio.generarRepositorio();
        if (!category.isEmpty()) {
            productos.removeIf(p -> !p.getCategory().equalsIgnoreCase(category));
            if (productos.isEmpty()) {
                throw new ProductException("No se han encontrado productos de la categoria " + category);
            }
        }
        if (!brand.isEmpty()) {
            productos.removeIf(p -> !p.getBrand().equalsIgnoreCase(brand));
            if (productos.isEmpty()) {
                throw new ProductException("No se han encontrado productos de la marca " + brand);
            }
        }
        if (!priceStr.isEmpty()) {
            int price = Integer.parseInt(priceStr);
            productos.removeIf(p -> (p.getPrice() > price));
            if (productos.isEmpty()) {
                throw new ProductException("No se han encontrado productos con precio menor o igual a " + price);
            }
        }
        if (!prestige.isEmpty()) {
            productos.removeIf(p -> !p.getPrestige().equalsIgnoreCase(prestige));
            if (productos.isEmpty()) {
                throw new ProductException("No se han encontrado productos con prestigio " + prestige);
            }
        }
        if (!freeShippingStr.isEmpty()) {
            if (freeShippingStr.equalsIgnoreCase("false")) {
                productos.removeIf(p -> p.isFreeShipping() == true);
            } else {
                productos.removeIf(p -> p.isFreeShipping() == false);
            }
            if (productos.isEmpty()) {
                if (freeShippingStr.equalsIgnoreCase("true")) {
                    throw new ProductException("No se han encontrado productos con envio gratis");
                } else {
                    throw new ProductException("No se han encontrado productos sin envio gratis");
                }
            }
        }
        return productos;
    }

    // método para ordenar la lista de productos según el tipo de ordenamiento
    private void ordenarProductos(List<ProductoDTO> lista, String ordenamiento) throws ProductException {
        if (!ordenamiento.isEmpty()) {
            int ord = Integer.parseInt(ordenamiento);
            switch (ord) {
                case 0:
                    // ordeno por nombre ascendete
                    lista.sort((p1, p2) -> (p1.getName().compareTo(p2.getName())));
                    break;
                case 1:
                    // ordeno por nombre descendente
                    lista.sort((p1, p2) -> (p2.getName().compareTo(p1.getName())));
                    break;
                case 2:
                    // ordeno por precio ascendente
                    lista.sort((p1, p2) -> (p1.getPrice() - p2.getPrice()));
                    break;
                case 3:
                    // ordeno por precio descendente
                    lista.sort((p1, p2) -> (p2.getPrice() - p1.getPrice()));
                    break;
                default:
                    throw new ProductException("No se ha podido ordenar la lista");
            }
        }

    }

    // método que procesa la GET request con o sin parámetros (llama a todas las funciones anteriores)
    @Override
    public List<ProductoDTO> procesarConsulta(String category, String brand, String priceStr, String freeShippingStr, String prestige, String orden) throws ProductException {
        int count = controlarInput(category, brand, priceStr, freeShippingStr, prestige);
        // si hay más de 2 parámetros, largo una excepción
        if (count < 3) {
            throw new ProductException("No se puede filtrar por más de 2 parámetros");
        }
        // si no recibo parámetros, listo todos los productos disponibles
        if (count == 5) {
            List<ProductoDTO> listado = listarProductosDisponibles();
            ordenarProductos(listado, orden);
            return listado;
            // si recibo 1 o 2 parámetros, filtro lo que corresponda
        } else {
            List<ProductoDTO> listado = filtrarProductos(category, brand, priceStr, freeShippingStr, prestige);
            ordenarProductos(listado, orden);
            return listado;
        }
    }

    // Genero el objeto de respuesta de la api (ticket + status)
    @Override
    public ResponseDTO generarTicket(PayloadDTO compra) throws ProductException {
        verificarCompra(compra);
        TicketDTO ticket = new TicketDTO();
        ticket.setId(ticketID);
        ticketID++;
        ticket.setArticles(compra.getArticles());
        ticket.setTotal(calcularPrecio(compra.getArticles()));
        StatusDTO statusDto = new StatusDTO(200, "La solicitud de compra se completó con éxito");
        ResponseDTO responseTicket = new ResponseDTO();
        responseTicket.setTicket(ticket);
        responseTicket.setStatusCode(statusDto);
        return responseTicket;
    }

    // método para verificar los articulos de la compra
    // (compruebo ID, nombre, marca y stock)
    private void verificarCompra(PayloadDTO compra) throws ProductException {
        List<ProductoDTO> productos = repositorio.generarRepositorio();
        List<ProductoCompraDTO> listaCompra = compra.getArticles();
        for (ProductoCompraDTO articulo : listaCompra) {
            ProductoDTO item = null;
            Optional<ProductoDTO> itemOption = productos.stream()
                    .filter(p -> p.getId() == articulo.getProductId())
                    .findFirst();
            if (itemOption.isPresent()) {
                item = itemOption.get();
            } else {
                throw new ProductException("No se ha encontrado un producto con ese ID");
            }
            if (!(item.getName().equalsIgnoreCase(articulo.getName()))) {
                throw new ProductException("El nombre " + articulo.getName() + " del producto de ID=" + articulo.getProductId() + " es incorrecto");
            }
            if (!(item.getBrand().equalsIgnoreCase(articulo.getBrand()))) {
                throw new ProductException("La marca " + articulo.getBrand() + " del producto de ID=" + articulo.getProductId() + " es incorrecta");
            }
            if (item.getQuantity() < articulo.getQuantity()) {
                throw new ProductException("No hay suficiente stock del producto '" + item.getName() + "'. Quedan " + item.getQuantity() + " items.");
            }
        }
    }

    // método para calcular el precio total de un listado de productos recibidos
    private int calcularPrecio(List<ProductoCompraDTO> itemsCompra) throws ProductException {
        int total = 0;
        List<ProductoDTO> itemsStock = repositorio.generarRepositorio();
        for (ProductoCompraDTO articulo : itemsCompra) {
            ProductoDTO item;
            Optional<ProductoDTO> itemOption = itemsStock.stream()
                    .filter(p -> p.getId() == articulo.getProductId())
                    .findFirst();
            if (itemOption.isPresent()) {
                item = itemOption.get();
            } else {
                throw new ProductException("No se ha encontrado un producto con ese ID");
            }
            total += articulo.getQuantity() * item.getPrice();
        }
        return total;
    }

    // método para agregar un listado de items al carrito
    @Override
    public CarritoDTO agregarAlCarrito(String userIdStr, PayloadDTO compra) throws ProductException, UserException {
        int userID;
        if(!userIdStr.isEmpty()) {
            userID = Integer.parseInt(userIdStr);
        } else {
            throw new UserException("Debe proveerse un id para agregar articulos al carrito");
        }
        if (compra != null) {
            verificarCompra(compra);
            if (carrito.containsKey(userID)) {
                // usuario existente, agrego articulos nuevos y actualizo la cantidad de los ya presentes en el carrito
                List<ProductoCompraDTO> productosEnCarrito = carrito.get(userID);
                for (ProductoCompraDTO articulo : compra.getArticles()) {
                    Optional<ProductoCompraDTO> itemOption = productosEnCarrito.stream()
                            .filter(p -> p.getProductId() == articulo.getProductId())
                            .findFirst();
                    Optional<ProductoDTO> itemStock = repositorio.generarRepositorio().stream()
                            .filter(p -> p.getId() == articulo.getProductId())
                            .findFirst();
                    if (itemOption.isPresent()) {
                        ProductoCompraDTO item = itemOption.get();
                        ProductoDTO stock = itemStock.get();
                        if ((item.getQuantity() + articulo.getQuantity()) > stock.getQuantity()) {
                            throw new ProductException("No hay suficiente stock del artículo " + stock.getName());
                        }
                        item.setQuantity(item.getQuantity() + articulo.getQuantity());
                    } else {
                        productosEnCarrito.add(articulo);
                    }
                }
            } else {
                // usuario nuevo, agrego todos los articulos al carrito
                carrito.put(userID, new ArrayList<>());
                carrito.get(userID).addAll(compra.getArticles());
            }
        }
        return mostrarCarrito(userID);
    }

    // método para mostrar los items presentes en el carrito de un usuario
    private CarritoDTO mostrarCarrito(int userID) throws ProductException, UserException {
        if (!carrito.containsKey(userID)) {
            throw new UserException("'userID' incorrecto. El usuario no existe");
        }
        List<ProductoCompraDTO> listado = carrito.get(userID);
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setUserID(userID);
        carritoDTO.setArticulosEnCarrito(listado);
        carritoDTO.setTotalAcumulado(calcularPrecio(listado));
        return carritoDTO;
    }

    // método para crear el ticket al comprar los articulos del carrito
    @Override
    public ResponseDTO comprarCarrito(String userIdStr) throws UserException, ProductException {
        int userID;
        if(!userIdStr.isEmpty()) {
            userID = Integer.parseInt(userIdStr);
        } else {
            throw new UserException("Debe proveerse un id para comprar los articulos del carrito");
        }
        if (!carrito.containsKey(userID)) {
            throw new UserException("'userID' incorrecto. El usuario no existe");
        }
        PayloadDTO payloadDTO = new PayloadDTO();
        payloadDTO.setArticles(carrito.get(userID));
        // al comprar debo vaciar el carrito
        ResponseDTO compraFinalizada = generarTicket(payloadDTO);
        carrito.remove(userID);
        return compraFinalizada;
    }

}
