package com.brewer.validation.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class AtributoConfirmacaoValidatorTest {

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void initialize() {
    }

    @Test
    public void isValid() {
    }
}