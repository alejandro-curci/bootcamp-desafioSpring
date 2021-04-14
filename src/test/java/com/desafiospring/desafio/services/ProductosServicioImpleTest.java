package com.desafiospring.desafio.services;

import com.desafiospring.desafio.dtos.CarritoDTO;
import com.desafiospring.desafio.dtos.ProductoDTO;
import com.desafiospring.desafio.dtos.ResponseDTO;
import com.desafiospring.desafio.exceptions.ProductException;
import com.desafiospring.desafio.exceptions.UserException;
import com.desafiospring.desafio.fixtures.CarritoDTOFixture;
import com.desafiospring.desafio.fixtures.PayloadDTOFixture;
import com.desafiospring.desafio.fixtures.ProductoDTOFixture;
import com.desafiospring.desafio.fixtures.ResponseDTOFixture;
import com.desafiospring.desafio.repositories.ProductosRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductosServicioImpleTest {

    private ProductosServicioImple service;
    private List<ProductoDTO> defaultList;

    @Mock
    private ProductosRepositorio repositoryMock;

    @BeforeEach
    void setUp() {
        service = new ProductosServicioImple(repositoryMock);
        defaultList = ProductoDTOFixture.defaultList();
    }

    @Test
    @DisplayName("Get all articles")
    void getAllItems() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "");
        assertEquals(defaultList, response);
        assertEquals(4, response.size());
    }

    @Test
    @DisplayName("Exception when filtering with too many parameters")
    void handleTooManyFilters() {
        assertThrows(ProductException.class,
                () -> service.procesarConsulta("Herramientas", "***", "10000",
                        "false", "", ""));
    }

    @Test
    @DisplayName("Filter items by category")
    void getToolsItems() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("Herramientas", "", "",
                "", "", "");
        assertEquals(ProductoDTOFixture.defaultToolsList(), response);
    }

    @Test
    @DisplayName("Filter by two parameters")
    void getFilteredItems() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);

        List<ProductoDTO> response = service.procesarConsulta("Herramientas", "", "",
                "true", "", "");

        assertEquals(ProductoDTOFixture.defaultFilteredList(), response);
    }

    @Test
    @DisplayName("Alphabetical order (asc)")
    void getAlphabeticalOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "0");
        assertEquals(ProductoDTOFixture.defaultOrderedList(0), response);
    }

    @Test
    @DisplayName("Alphabetical order (desc)")
    void getReversedAlphabeticalOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "1");
        assertEquals(ProductoDTOFixture.defaultOrderedList(1), response);
    }

    @Test
    @DisplayName("Price order (asc)")
    void getPriceOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "2");
        assertEquals(ProductoDTOFixture.defaultOrderedList(2), response);
    }

    @Test
    @DisplayName("Price order (desc)")
    void getReversedPriceOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "3");
        assertEquals(ProductoDTOFixture.defaultOrderedList(3), response);
    }

    // TESTS REGARDING generarTicket() METHOD

    @Test
    @DisplayName("Success ticket generation")
    void successPurchase() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        ResponseDTO actual = service.generarTicket(PayloadDTOFixture.successPayload());
        ResponseDTO expected = ResponseDTOFixture.successResponse();

        assertEquals(expected.getTicket().getArticles(), actual.getTicket().getArticles());
        assertEquals(expected.getTicket().getTotal(), actual.getTicket().getTotal());
        assertEquals(expected.getStatusCode().getCode(), actual.getStatusCode().getCode());
    }

    @Test
    @DisplayName("Wrong purchase request")
    void wrongPurchase() {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);

        assertThrows(ProductException.class,
                () -> service.generarTicket(PayloadDTOFixture.wrongPayload()));
    }

    // TESTS REGARDING agregarAlCarrito() METHOD

    @Test
    @DisplayName("Add items to shopping chart")
    void addToChart() throws UserException, ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        CarritoDTO actual = service.agregarAlCarrito("1", PayloadDTOFixture.successPayload());
        CarritoDTO expected = CarritoDTOFixture.crearCarrito();

        assertEquals(actual.getArticulosEnCarrito(), expected.getArticulosEnCarrito());
        assertEquals(actual.getTotalAcumulado(), expected.getTotalAcumulado());
    }

    @Test
    @DisplayName("Buy items in shopping chart")
    void buyChart() throws ProductException, UserException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        CarritoDTO currentChart = service.agregarAlCarrito("1", PayloadDTOFixture.successPayload());
        ResponseDTO actual = service.comprarCarrito("1");
        ResponseDTO expected = ResponseDTOFixture.successResponse();

        assertEquals(actual.getTicket().getTotal(), expected.getTicket().getTotal());
        assertEquals(actual.getTicket().getArticles(), expected.getTicket().getArticles());
    }

    @Test
    @DisplayName("No items in shopping chart")
    void buyEmptyChart() {

        assertThrows(UserException.class,
                () -> service.comprarCarrito("1"));
    }

}