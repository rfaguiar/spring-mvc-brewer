package com.brewer.controller.validator;

import com.brewer.builder.VendaBuilder;
import com.brewer.model.Cerveja;
import com.brewer.model.Venda;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class VendaValidatorTest {

    private VendaValidator validator;
    private Venda venda;
    @Mock
    private Errors mockErrors;

    @Before
    public void metodoInicializaCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);

        this.venda = VendaBuilder.criarVenda();
        this.validator = new VendaValidator();
    }

    @Test
    public void testeMetodoSupports() {
        boolean result = validator.supports(Venda.class);
        assertTrue(result);
    }

    @Test
    public void testeMetodoSupportsQuandoClasseIncorretaDeveRetornarFalse() {
        boolean result = validator.supports(Cerveja.class);
        assertFalse(result);
    }

    @Test
    public void testeMetodoValidate() {
        validator.validate(venda, mockErrors);
    }

    @Test
    public void testeMetodoValidateQuandoValorTotalMenorQueZeroDeveRejeitarComMsgAdequada() {
        venda.setValorTotal(new BigDecimal(-10));
        validator.validate(venda, mockErrors);
        Mockito.verify(mockErrors).reject("", "Valor total não pode ser negativo");
    }

    @Test
    public void testeMetodoValidateQuandoVendaSemItensDeveRejeitarComMsgAdequada() {
        venda.setItens(new ArrayList<>());
        validator.validate(venda, mockErrors);
        Mockito.verify(mockErrors).reject("", "Adicione pelo menos uma cerveja na venda");
    }

    @Test
    public void testeMetodoValidateQuandoVendaComHorarioDeEntregaESemDataDeEntregaDeveRejeitarComMsgAdequada() {
        venda.setHorarioEntrega(LocalTime.now());
        venda.setDataEntrega(null);
        validator.validate(venda, mockErrors);
        Mockito.verify(mockErrors).rejectValue("dataEntrega", "", "Informe uma data da entrega para um horário");
    }
}