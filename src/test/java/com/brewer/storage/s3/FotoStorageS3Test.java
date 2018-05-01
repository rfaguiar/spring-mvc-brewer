package com.brewer.storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.brewer.storage.exception.FotoStorageException;
import net.coobird.thumbnailator.Thumbnails;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Thumbnails.class, Thumbnails.Builder.class, AmazonS3.class, Logger.class, LoggerFactory.class,
        S3Object.class, S3ObjectInputStream.class, IOUtils.class, DeleteObjectsResult.class, System.class})
public class FotoStorageS3Test {

    private FotoStorageS3 storage;

    @Mock
    private AmazonS3 mockAmazonS3;
    @Mock
    private Thumbnails.Builder mockThumbBuild;
    @Mock
    private S3Object mock3Object;
    @Mock
    private S3ObjectInputStream s3ObjStream;
    @Mock
    private DeleteObjectsResult mockDeleteObj;

    @Mock
    private Logger mockLogger;

    @Before
    public void metodoIniciandoCenariosDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Thumbnails.class);
        PowerMockito.mockStatic(Logger.class);
        PowerMockito.mockStatic(LoggerFactory.class);
        PowerMockito.mockStatic(IOUtils.class);
        PowerMockito.mockStatic(System.class);
        Mockito.when(LoggerFactory.getLogger(FotoStorageS3.class)).thenReturn(mockLogger);
        this.storage = new FotoStorageS3("bucketTeste", mockAmazonS3);

    }

    @Test
    public void testeConstrutorComArgumentoAmazonS3() {
        this.storage = new FotoStorageS3(mockAmazonS3);
        PowerMockito.verifyStatic(System.class);
    }

    @Test
    public void testeMetodoSalvarDeveSalvarCorretamenteAFotoNaAmazonS3() throws IOException {
        MultipartFile[] multPart = {new MockMultipartFile("teste", new byte[]{'t', 'e', 's', 't', 'e'})};
        Mockito.when(Thumbnails.of(Matchers.any(InputStream.class))).thenReturn(mockThumbBuild);
        Mockito.when(mockThumbBuild.size(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockThumbBuild);
        Mockito.doNothing().when(mockThumbBuild).toOutputStream(Matchers.any(OutputStream.class));
        String result = storage.salvar(multPart);
        assertNotNull(result);
    }

    @Test(expected = FotoStorageException.class)
    public void testeMetodoSalvarQuandoOcorreExcecaoDeveLancarMsgAdequada() throws IOException {
        try {
            MultipartFile[] multPart = {new MockMultipartFile("teste", new byte[]{'t', 'e', 's', 't', 'e'})};
            Mockito.when(Thumbnails.of(Matchers.any(InputStream.class))).thenReturn(mockThumbBuild);
            Mockito.when(mockThumbBuild.size(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockThumbBuild);
            Mockito.doThrow(new IOException()).when(mockThumbBuild).toOutputStream(Matchers.any(OutputStream.class));
            storage.salvar(multPart);
        } catch (IOException e) {
            assertEquals("Erro salvando arquivo no S3", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoRecuperarAFotoArmazenadaNaAmazonS3() throws IOException {

        Mockito.when(mockAmazonS3.getObject(Matchers.anyString(), Matchers.anyString())).thenReturn(mock3Object);
        Mockito.when(mock3Object.getObjectContent()).thenReturn(s3ObjStream);
        Mockito.when(IOUtils.toByteArray(s3ObjStream)).thenReturn(new byte[]{'t', 'e', 's', 't', 'e'});
        byte[] result = storage.recuperar("teste");
        assertNotNull(result);

    }

    @Test(expected = FotoStorageException.class)
    public void testeMetodoRecuperarQuandoOcorrerErroDeveLancarExcecaoComMensagenAdequada() throws IOException {
        try {
            Mockito.when(mockAmazonS3.getObject(Matchers.anyString(), Matchers.anyString())).thenReturn(mock3Object);
            Mockito.when(mock3Object.getObjectContent()).thenReturn(s3ObjStream);
            Mockito.when(IOUtils.toByteArray(s3ObjStream)).thenThrow(new IOException());
            storage.recuperar("teste");
        }catch (IOException e) {
            assertEquals("Não conseguiu recuperar foto do S3", e.getMessage());
            throw e;
        }
        fail();

    }

    @Test
    public void testeMetodoRecuperarThumbnailDeveRetornarAFotoPequena() throws IOException {

        Mockito.when(mockAmazonS3.getObject(Matchers.anyString(), Matchers.anyString())).thenReturn(mock3Object);
        Mockito.when(mock3Object.getObjectContent()).thenReturn(s3ObjStream);
        Mockito.when(IOUtils.toByteArray(s3ObjStream)).thenReturn(new byte[]{'t', 'e', 's', 't', 'e'});
        byte[] result = storage.recuperarThumbnail("teste");
        assertNotNull(result);
    }

    @Test
    public void testeMetodoExcluirDeveDeletarArquivoNaAmazon() {
        Mockito.when(mockAmazonS3.deleteObjects(Matchers.any(DeleteObjectsRequest.class))).thenReturn(mockDeleteObj);
        storage.excluir("teste");
        Mockito.verify(mockAmazonS3).deleteObjects(Matchers.any(DeleteObjectsRequest.class));
    }

    @Test
    public void testeMetodoGetUrlDeveRetornarAmazonUrlDaFoto() {

        String result = storage.getUrl("fotoTeste");
        assertEquals("https://s3.amazonaws.com/bucketTeste/fotoTeste", result);
    }

    @Test
    public void testeMetodoGetUrlVaziaDeveRetornarNull() {

        String result = storage.getUrl("");
        assertNull(result);
    }
}