package com.desafiospring.desafio.services;

import com.desafiospring.desafio.dtos.ProductoDTO;
import com.desafiospring.desafio.dtos.ResponseDTO;
import com.desafiospring.desafio.exceptions.ProductException;
import com.desafiospring.desafio.fixtures.PayloadDTOFixture;
import com.desafiospring.desafio.fixtures.ProductoDTOFixture;
import com.desafiospring.desafio.fixtures.ResponseDTOFixture;
import com.desafiospring.desafio.repositories.ProductosRepositorio;
import org.junit.jupiter.api.BeforeEach;
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

    // TESTS REGARDING procesarConsulta() METHOD
    @Test
    void getAllItems() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "");
        assertEquals(defaultList, response);
        assertEquals(4, response.size());
    }

    @Test
    void handleTooManyFilters() {
        assertThrows(ProductException.class,
                () -> service.procesarConsulta("Herramientas", "***", "10000",
                        "false", "", ""));
    }

    @Test
    void getToolsItems() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("Herramientas", "", "",
                "", "", "");
        assertEquals(ProductoDTOFixture.defaultToolsList(), response);
    }

    @Test
    void getFilteredItems() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);

        List<ProductoDTO> response = service.procesarConsulta("Herramientas", "", "",
                "true", "", "");

        assertEquals(ProductoDTOFixture.defaultFilteredList(), response);
    }

    @Test
    void getAlphabeticalOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "0");
        assertEquals(ProductoDTOFixture.defaultOrderedList(0), response);
    }

    @Test
    void getReversedAlphabeticalOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "1");
        assertEquals(ProductoDTOFixture.defaultOrderedList(1), response);
    }

    @Test
    void getPriceOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "2");
        assertEquals(ProductoDTOFixture.defaultOrderedList(2), response);
    }

    @Test
    void getReversedPriceOrder() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        List<ProductoDTO> response = service.procesarConsulta("", "", "",
                "", "", "3");
        assertEquals(ProductoDTOFixture.defaultOrderedList(3), response);
    }

    // TESTS REGARDING generarTicket() METHOD

    @Test
    void successPurchase() throws ProductException {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);
        ResponseDTO actual = service.generarTicket(PayloadDTOFixture.successPayload());
        ResponseDTO expected = ResponseDTOFixture.successResponse();

        assertEquals(expected.getTicket().getArticles(), actual.getTicket().getArticles());
        assertEquals(expected.getTicket().getTotal(), actual.getTicket().getTotal());
        assertEquals(expected.getStatusCode().getCode(), actual.getStatusCode().getCode());
    }

    @Test
    void wrongPurchase() {
        when(repositoryMock.generarRepositorio()).thenReturn(defaultList);

        assertThrows(ProductException.class,
                () -> service.generarTicket(PayloadDTOFixture.wrongPayload()));
    }



}