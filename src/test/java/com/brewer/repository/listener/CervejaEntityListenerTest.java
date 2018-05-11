package com.brewer.repository.listener;

import com.brewer.Constantes;
import com.brewer.builder.CervejaBuilder;
import com.brewer.model.Cerveja;
import com.brewer.storage.FotoStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringBeanAutowiringSupport.class})
public class CervejaEntityListenerTest {

    private CervejaEntityListener listener;
    @Mock
    private FotoStorage mockFotoStorage;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(SpringBeanAutowiringSupport.class);
        this.listener = new CervejaEntityListener(mockFotoStorage);
    }

    @Test
    public void testeMetodoPostLoadDeveBuscarUrlFoto() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        Mockito.when(mockFotoStorage.getUrl(cerveja.getFotoOuMock())).thenReturn("foto");
        Mockito.when(mockFotoStorage.getUrl(Constantes.THUMBNAIL_PREFIX + cerveja.getFotoOuMock())).thenReturn("thumbnailFoto");
        listener.postLoad(cerveja);
        assertEquals("foto", cerveja.getUrlFoto());
        assertEquals("thumbnailFoto", cerveja.getUrlThumbnailFoto());
    }
}