package com.brewer.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class VendaTest {

    @Test
    public void testeMetodosTransientAoBd() {
        Venda venda = new Venda();
        LocalDateTime dataCriacao = LocalDateTime.now();
        venda.setDataCriacao(dataCriacao);
        venda.setValorTotal(new BigDecimal(10));
        venda.setObservacao("obsTeste");
        LocalDateTime dataHoraEntrega = LocalDateTime.now();
        venda.setDataHoraEntrega(dataHoraEntrega);
        LocalDate dataEntrega = LocalDate.now();
        venda.setDataEntrega(dataEntrega);
        LocalTime horaEntrega = LocalTime.now();
        venda.setHorarioEntrega(horaEntrega);
        venda.setStatus(StatusVenda.EMITIDA);

        assertEquals(dataCriacao.toString(), venda.getDataCriacao().toString());
        assertEquals("10", venda.getValorTotal().toString());
        assertEquals("obsTeste", venda.getObservacao());
        assertEquals(dataHoraEntrega.toString(), venda.getDataHoraEntrega().toString());
        assertEquals(dataEntrega.toString(), venda.getDataEntrega().toString());
        assertEquals(horaEntrega.toString(), venda.getHorarioEntrega().toString());
        assertTrue(venda.isNova());
        assertEquals(new Long(0), venda.getDiasCriacao());
        assertTrue(venda.isSalvarPermitido());
        assertFalse(venda.isSalvarProibido());
        assertNotNull(venda.hashCode());
        assertNotNull(venda.toString());
    }

}