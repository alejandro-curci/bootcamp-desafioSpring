package com.desafiospring.desafio.fixtures;

import com.desafiospring.desafio.dtos.TicketDTO;

public class TicketDTOFixture {

    // success ticket
    public static TicketDTO successTicket() {
        TicketDTO ticket = new TicketDTO();
        ticket.setId(1);
        ticket.setArticles(ProductoCompraDTOFixture.defaultSuccessfulList());
        ticket.setTotal(6*6000+3*4300);
        return ticket;
    }

}
