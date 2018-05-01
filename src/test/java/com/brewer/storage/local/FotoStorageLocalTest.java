package com.brewer.storage.local;

import com.brewer.Constantes;
import com.brewer.storage.exception.FotoStorageException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Thumbnails.class, Thumbnails.Builder.class, Logger.class, LoggerFactory.class, System.class,
        Files.class, MultipartFile.class})
public class FotoStorageLocalTest {

    private FotoStorageLocal storage;
    @Mock
    private Logger mockLogger;
    @Mock
    private Thumbnails.Builder mockThumbBuild;
    @Mock
    private MultipartFile mockMultipartFile;
    private Path path;

    @Before
    public void metodoIniciandoCenariosDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(LoggerFactory.class);
        PowerMockito.mockStatic(Thumbnails.class);
        Mockito.when(LoggerFactory.getLogger(FotoStorageLocal.class)).thenReturn(mockLogger);
        this.path = Paths.get("./target/unitTestFotoStorageLocal");
        this.storage = new FotoStorageLocal(path);
    }

    @After
    public void metodofinalizandoCenariodeTeste() {
        deleteDir(path.toFile());
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Test
    public void testConstrutorSemArgumentos() {
        this.storage = new FotoStorageLocal();
        PowerMockito.verifyStatic(System.class);
    }

    @Test
    public void testeMetodoSalvarDeveSalvarArquivoLocal() throws IOException {
        MultipartFile[] multPart = {new MockMultipartFile("teste", new byte[]{'t', 'e', 's', 't', 'e'})};
        Mockito.when(Thumbnails.of(Matchers.anyString())).thenReturn(mockThumbBuild);
        Mockito.when(mockThumbBuild.size(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockThumbBuild);
        Mockito.doNothing().when(mockThumbBuild).toOutputStream(Matchers.any(OutputStream.class));
        String result = storage.salvar(multPart);
        assertNotNull(result);
    }

    @Test(expected = FotoStorageException.class)
    public void testeMetodoSalvarDeveLancarExcecaoComMsgAdequadaQuandoOcorrerErroNoThumbNail() throws IOException {
        try {
            MultipartFile[] multPart = {new MockMultipartFile("teste", new byte[]{'t', 'e', 's', 't', 'e'})};
            Mockito.when(Thumbnails.of(Matchers.anyString())).thenReturn(mockThumbBuild);
            Mockito.when(mockThumbBuild.size(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockThumbBuild);
            Mockito.doThrow(new IOException()).when(mockThumbBuild).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
            storage.salvar(multPart);
        } catch (IOException e) {
            assertEquals("Erro gerando thumbnail", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test(expected = FotoStorageException.class)
    public void testeMetodoSalvarDeveLancarExcecaoComMsgAdequadaQuandoOcorrerErroNoThumbNailNaoConseguiurSalvarNoDiretorioLocal() throws IOException {
        try {
            MultipartFile[] multPart = {mockMultipartFile};
            Mockito.when(Thumbnails.of(Matchers.anyString())).thenReturn(mockThumbBuild);
            Mockito.when(mockThumbBuild.size(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockThumbBuild);
            Mockito.doNothing().when(mockThumbBuild).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
            Mockito.doThrow(new IOException()).when(mockMultipartFile).transferTo(Matchers.any(File.class));
            storage.salvar(multPart);
        } catch (IOException e) {
            assertEquals("Erro salvando a foto", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoRecuperarQuandoOArquivoExisteDeveRetornar() throws IOException {
        Files.createFile(Paths.get("./target/unitTestFotoStorageLocal/teste.png"));
        byte[] result = storage.recuperar("teste.png");
        assertNotNull(result);
    }

    @Test(expected = FotoStorageException.class)
    public void tesMetodoRecuperarAoOcorrerExcecaoDeveLancarComMsgAdequada() {
        try {
            storage.recuperar("teste.png");
        } catch (Exception e) {
            assertEquals("Erro lendo a foto", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void recuperarThumbnail() throws IOException {
        Files.createFile(Paths.get("./target/unitTestFotoStorageLocal/"+Constantes.THUMBNAIL_PREFIX +"teste.png"));
        byte[] result = storage.recuperarThumbnail("teste.png");
        assertNotNull(result);
    }

    @Test
    public void excluir() throws IOException {
        Files.createFile(Paths.get("./target/unitTestFotoStorageLocal/teste.png"));
        boolean result = storage.excluir("teste.png");
        assertTrue(result);
    }

    @Test
    public void getUrl() {
        String result = storage.getUrl("");
        assertEquals("http://localhost:8080/fotos/", result);
    }
}