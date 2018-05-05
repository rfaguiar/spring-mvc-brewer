package com.brewer.controller;

import com.brewer.dto.FotoDTO;
import com.brewer.storage.FotoStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class FotosControllerTest {

    private FotosController controller;

    @Mock
    private FotoStorage mockFotoStorage;
    @Mock
    private MultipartFile mockMultPartFile;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.controller = new FotosController(mockFotoStorage);
    }

    @Test
    public void testeMetodoUploadDeveSalvarFotoERetornarObjetoFotoComNomeEUrlETipo() throws InterruptedException {
        MultipartFile[] files = new MultipartFile[]{mockMultPartFile};
        Mockito.when(mockFotoStorage.salvar(files)).thenReturn("foto");
        Mockito.when(mockFotoStorage.getUrl("foto")).thenReturn("urlFoto");
        Mockito.when(mockMultPartFile.getContentType()).thenReturn("png");

        DeferredResult<FotoDTO> result = controller.upload(files);
        //aguardaResultado
        while(!result.hasResult()) {
            Thread.sleep(100l);
        }

        FotoDTO fotoDTO = (FotoDTO) result.getResult();

        assertEquals("foto", fotoDTO.getNome());
        assertEquals("urlFoto", fotoDTO.getUrl());
        assertEquals("png", fotoDTO.getContentType());
    }

    @Test
    public void recuperar() {
        byte[] fotoBytes = new byte[]{'f', 'o', 't', 'o'};
        Mockito.when(mockFotoStorage.recuperar("foto")).thenReturn(fotoBytes);

        byte[] result = controller.recuperar("foto");

        assertArrayEquals(fotoBytes, result);
    }
}