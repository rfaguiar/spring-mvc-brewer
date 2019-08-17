package com.brewer.validation.validator;

import com.brewer.builder.UsuarioBuilder;
import com.brewer.model.Usuario;
import com.brewer.validation.AtributoConfirmacao;
import com.brewer.validation.validator.exception.ConfirmacaoValidatorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class AtributoConfirmacaoValidatorTest {

    private AtributoConfirmacaoValidator validator;
    @Mock
    private AtributoConfirmacao mockAtributoConfirmacao;
    @Mock
    private ConstraintValidatorContext mockConstraintValidatorContext;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder mockConstraintBuilationBuilder;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext mockNodeBuilderCustomizeContext;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.validator = new AtributoConfirmacaoValidator();
    }

    @Test
    public void testeMetodoIsValidQuandoValoresCorretosDeveRetornarTrue() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        usuario.setSenha("senha");
        usuario.setConfirmacaoSenha("senha");
        Mockito.when(mockAtributoConfirmacao.atributo()).thenReturn("senha");
        Mockito.when(mockAtributoConfirmacao.atributoConfirmacao()).thenReturn("confirmacaoSenha");
        Mockito.when(mockAtributoConfirmacao.message()).thenReturn("Confirmação da senha não confere");
        validator.initialize(mockAtributoConfirmacao);

        boolean result = this.validator.isValid(usuario, mockConstraintValidatorContext);

        assertTrue(result);
    }

    @Test
    public void testeMetodoIsValidQuandoValoresSenhaNaoIgualComSenhaDeConfirmacaoDeveRetornarFalse() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        usuario.setSenha(null);
        usuario.setConfirmacaoSenha(null);
        Mockito.when(mockAtributoConfirmacao.atributo()).thenReturn("senha");
        Mockito.when(mockAtributoConfirmacao.atributoConfirmacao()).thenReturn("confirmacaoSenha");
        Mockito.when(mockAtributoConfirmacao.message()).thenReturn("Confirmação da senha não confere");
        Mockito.when(mockConstraintValidatorContext.getDefaultConstraintMessageTemplate()).thenReturn("mensagemMock");
        Mockito.when(mockConstraintValidatorContext.buildConstraintViolationWithTemplate(Matchers.any())).thenReturn(mockConstraintBuilationBuilder);
        Mockito.when(mockConstraintBuilationBuilder.addPropertyNode(Matchers.any())).thenReturn(mockNodeBuilderCustomizeContext);
        Mockito.when(mockNodeBuilderCustomizeContext.addConstraintViolation()).thenReturn(mockConstraintValidatorContext);
        validator.initialize(mockAtributoConfirmacao);

        boolean result = this.validator.isValid(usuario, mockConstraintValidatorContext);

        assertFalse(result);
    }

    @Test
    public void testeMetodoIsValidQuandoValoresSenhaNaoIgualComSenhaDeConfirmacaoDeveRetornarFalse2() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        usuario.setSenha("senha");
        usuario.setConfirmacaoSenha(null);
        Mockito.when(mockAtributoConfirmacao.atributo()).thenReturn("senha");
        Mockito.when(mockAtributoConfirmacao.atributoConfirmacao()).thenReturn("confirmacaoSenha");
        Mockito.when(mockAtributoConfirmacao.message()).thenReturn("Confirmação da senha não confere");
        Mockito.when(mockConstraintValidatorContext.getDefaultConstraintMessageTemplate()).thenReturn("mensagemMock");
        Mockito.when(mockConstraintValidatorContext.buildConstraintViolationWithTemplate(Matchers.any())).thenReturn(mockConstraintBuilationBuilder);
        Mockito.when(mockConstraintBuilationBuilder.addPropertyNode(Matchers.any())).thenReturn(mockNodeBuilderCustomizeContext);
        Mockito.when(mockNodeBuilderCustomizeContext.addConstraintViolation()).thenReturn(mockConstraintValidatorContext);
        validator.initialize(mockAtributoConfirmacao);

        boolean result = this.validator.isValid(usuario, mockConstraintValidatorContext);

        assertFalse(result);
    }

    @Test
    public void testeMetodoIsValidQuandoValoresSenhaNaoIgualComSenhaDeConfirmacaoDeveRetornarFalse3() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        usuario.setSenha(null);
        usuario.setConfirmacaoSenha("senha");
        Mockito.when(mockAtributoConfirmacao.atributo()).thenReturn("senha");
        Mockito.when(mockAtributoConfirmacao.atributoConfirmacao()).thenReturn("confirmacaoSenha");
        Mockito.when(mockAtributoConfirmacao.message()).thenReturn("Confirmação da senha não confere");
        Mockito.when(mockConstraintValidatorContext.getDefaultConstraintMessageTemplate()).thenReturn("mensagemMock");
        Mockito.when(mockConstraintValidatorContext.buildConstraintViolationWithTemplate(Matchers.any())).thenReturn(mockConstraintBuilationBuilder);
        Mockito.when(mockConstraintBuilationBuilder.addPropertyNode(Matchers.any())).thenReturn(mockNodeBuilderCustomizeContext);
        Mockito.when(mockNodeBuilderCustomizeContext.addConstraintViolation()).thenReturn(mockConstraintValidatorContext);
        validator.initialize(mockAtributoConfirmacao);

        boolean result = this.validator.isValid(usuario, mockConstraintValidatorContext);

        assertFalse(result);
    }

    @Test(expected = ConfirmacaoValidatorException.class)
    public void testeMetodoIsValidQuandoUsadoDeFormaIncorretoDeveLancarExcepcaoComMsgAdequada() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        try {
            Usuario usuario = UsuarioBuilder.criarUsuario();
            Mockito.when(mockAtributoConfirmacao.atributo()).thenReturn("senha1");
            Mockito.when(mockAtributoConfirmacao.atributoConfirmacao()).thenReturn("confirmacaoSenha1");
            Mockito.when(mockAtributoConfirmacao.message()).thenReturn("Confirmação da senha não confere");
            validator.initialize(mockAtributoConfirmacao);

            this.validator.isValid(usuario, mockConstraintValidatorContext);
        } catch (Exception e) {
            assertEquals("Erro Recuperando valores dos atributos", e.getMessage());
            throw e;
        }
        fail();
    }
}