package com.desafiospring.desafio.fixtures;

import com.desafiospring.desafio.dtos.ResponseDTO;

public class ResponseDTOFixture {

    public static ResponseDTO successResponse() {
        ResponseDTO response = new ResponseDTO();
        response.setTicket(TicketDTOFixture.successTicket());
        response.setStatusCode(StatusDTOFixture.successStatus());
        return response;
    }


}
