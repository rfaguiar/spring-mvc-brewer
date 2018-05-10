package com.brewer.service;

import com.brewer.builder.ClienteBuilder;
import com.brewer.model.Cliente;
import com.brewer.repository.Clientes;
import com.brewer.service.exception.CpfCnpjClienteJaCadastradoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CadastroClienteServiceTest {

    private CadastroClienteService service;
    @Mock
    private Clientes mockClientesRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.service = new CadastroClienteService(mockClientesRepo);
    }

    @Test(expected = CpfCnpjClienteJaCadastradoException.class)
    public void testeMetodoSalvarQuandoClienteComMesmoCpfOuCnpjDeveLancarExcecaoComMsgAdequada() {
        try {
            Cliente cliente = ClienteBuilder.criarCliente();
            Optional<Cliente> clienteOpt = Optional.of(cliente);
            Mockito.when(mockClientesRepo.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao())).thenReturn(clienteOpt);
            service.salvar(cliente);
        }catch (Exception e) {
            assertEquals("CPF/CNPJ já cadastrado", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoSalvarQuandoNovoClienteDeveSalvar() {
        Cliente cliente = ClienteBuilder.criarCliente();
        Optional<Cliente> clienteOpt = Optional.empty();
        Mockito.when(mockClientesRepo.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao())).thenReturn(clienteOpt);
        service.salvar(cliente);
    }
}