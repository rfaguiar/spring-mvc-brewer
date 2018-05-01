package com.brewer.storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.brewer.Constantes;
import com.brewer.storage.FotoStorage;
import com.brewer.storage.exception.FotoStorageException;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Profile("prod")
@Component
public class FotoStorageS3 implements FotoStorage {

    private static final Logger logger = LoggerFactory.getLogger(FotoStorageS3.class);

    private String bucketName;

    private AmazonS3 amazonS3;

    public FotoStorageS3() {
        this(System.getenv(Constantes.BUCKET_NAME));
    }

    public FotoStorageS3(String bucketName) {
        this.bucketName = bucketName;
    }

    public FotoStorageS3(String bucketName, AmazonS3 amazonS3) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
    }

    @Autowired
    public FotoStorageS3(AmazonS3 amazonS3) {
        this();
        this.amazonS3 = amazonS3;
    }


    @Override
    public String salvar(MultipartFile[] files) {
        String novoNome = null;
        if (files != null && files.length > 0) {
            MultipartFile arquivo = files[0];
            novoNome = renomearArquivo(arquivo.getOriginalFilename());

            try {
                AccessControlList acl = new AccessControlList();
                acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

                enviarFoto(novoNome, arquivo, acl);
                enviarThumbnail(novoNome, arquivo, acl);
            } catch (IOException e) {
                throw new FotoStorageException("Erro salvando arquivo no S3", e);
            }
        }

        return novoNome;
    }

    @Override
    public byte[] recuperar(String foto) {
        InputStream is = amazonS3.getObject(bucketName, foto).getObjectContent();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new FotoStorageException("Não conseguiu recuperar foto do S3", e);
        }
    }

    @Override
    public byte[] recuperarThumbnail(String foto) {
        return recuperar(Constantes.THUMBNAIL_PREFIX + foto);
    }

    @Override
    public boolean excluir(String foto) {
        logger.debug(foto);
        if (!StringUtils.isEmpty(foto)) {
            amazonS3.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(foto, Constantes.THUMBNAIL_PREFIX + foto));
            return true;
        }
        return false;
    }

    @Override
    public String getUrl(String foto) {
        if (!StringUtils.isEmpty(foto)) {
            return String.format("https://s3.amazonaws.com/%s/%s", bucketName, foto);
        }

        return null;
    }

    private ObjectMetadata enviarFoto(String novoNome, MultipartFile arquivo, AccessControlList acl)
            throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(arquivo.getContentType());
        metadata.setContentLength(arquivo.getSize());
        amazonS3.putObject(new PutObjectRequest(bucketName, novoNome, arquivo.getInputStream(), metadata)
                .withAccessControlList(acl));
        return metadata;
    }

    private void enviarThumbnail(String novoNome, MultipartFile arquivo, AccessControlList acl)	throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(arquivo.getInputStream()).size(40, 68).toOutputStream(os);
        byte[] array = os.toByteArray();
        InputStream is = new ByteArrayInputStream(array);
        ObjectMetadata thumbMetadata = new ObjectMetadata();
        thumbMetadata.setContentType(arquivo.getContentType());
        thumbMetadata.setContentLength(array.length);
        amazonS3.putObject(new PutObjectRequest(bucketName, Constantes.THUMBNAIL_PREFIX + novoNome, is, thumbMetadata)
                .withAccessControlList(acl));
    }

}
