package com.desafiospring.desafio.controllers;

import com.desafiospring.desafio.fixtures.CarritoDTOFixture;
import com.desafiospring.desafio.fixtures.PayloadDTOFixture;
import com.desafiospring.desafio.fixtures.ProductoDTOFixture;
import com.desafiospring.desafio.fixtures.ResponseDTOFixture;
import com.desafiospring.desafio.services.ProductosServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductosControlador.class)
class ProductosControladorTest {

    @MockBean
    private ProductosServicio serviceMock;

    @Autowired
    private MockMvc mockMvc;

    // method to convert Object to Json String
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // load json file to get mocked products
    @Test
    @DisplayName("Get all articles")
    void getArticles() throws Exception {
        when(serviceMock.procesarConsulta(any(), any(), any(), any(), any(), any()))
                .thenReturn(ProductoDTOFixture.loadJson("classpath:allProducts.json"));
        MvcResult result = mockMvc
                .perform(get("/api/v1/articles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        int statusResponse = result.getResponse().getStatus();
        assertNotNull(jsonResponse);
        assertEquals(200, statusResponse);
    }

    // build mocked products through fixtures classes
    @Test
    @DisplayName("Purchase request")
    void purchaseRequest() throws Exception {
        when(serviceMock.generarTicket(any()))
                .thenReturn(ResponseDTOFixture.successResponse());
        MvcResult result = mockMvc
                .perform(post("/api/v1/purchase-request")
                        .content(asJsonString(PayloadDTOFixture.successPayload()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        int statusResponse = result.getResponse().getStatus();
        assertNotNull(jsonResponse);
        assertEquals(200, statusResponse);
    }

    @Test
    @DisplayName("Add to shopping chart")
    void addToChart() throws Exception {
        when(serviceMock.agregarAlCarrito(any(), any()))
                .thenReturn(CarritoDTOFixture.crearCarrito());
        MvcResult result = mockMvc
                .perform(post("/api/v1/carrito")
                        .content(asJsonString(PayloadDTOFixture.successPayload()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        int statusResponse = result.getResponse().getStatus();
        assertNotNull(jsonResponse);
        assertEquals(200, statusResponse);
    }

    @Test
    @DisplayName("Buy articles in shopping chart")
    void buyChart() throws Exception {
        when(serviceMock.comprarCarrito(any()))
                .thenReturn(ResponseDTOFixture.successResponse());
        MvcResult result = mockMvc
                .perform(post("/api/v1/carrito/comprar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        int statusResponse = result.getResponse().getStatus();
        assertNotNull(jsonResponse);
        assertEquals(200, statusResponse);
    }
}