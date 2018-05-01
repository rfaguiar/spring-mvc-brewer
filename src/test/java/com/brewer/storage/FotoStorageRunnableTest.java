package com.brewer.storage;

import com.brewer.dto.FotoDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class FotoStorageRunnableTest {

    private FotoStorageRunnable storage;
    private DeferredResult<FotoDTO> result;
    @Mock
    private FotoStorage mockFotoStorage;

    @Mock
    private MultipartFile mockMulpartFile;

    @Before
    public void metodoInicializadoCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        result = new DeferredResult<>();
        storage = new FotoStorageRunnable(new MultipartFile[]{mockMulpartFile}, result, mockFotoStorage);
    }

    @Test
    public void run() {
        Mockito.when(mockFotoStorage.salvar(Matchers.any(MultipartFile[].class))).thenReturn("teste");
        Mockito.when(mockMulpartFile.getContentType()).thenReturn("png");
        Mockito.when(mockFotoStorage.getUrl("teste")).thenReturn("url");
        storage.run();
        FotoDTO dto = (FotoDTO) result.getResult();
        assertEquals("teste", dto.getNome());
        assertEquals("png", dto.getContentType());
        assertEquals("url", dto.getUrl());
    }
}