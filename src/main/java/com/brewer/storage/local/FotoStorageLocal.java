package com.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.brewer.Constantes;
import com.brewer.storage.FotoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@Profile({"local", "local-jndi"})
@Component
public class FotoStorageLocal implements FotoStorage {

    private static final Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);

    private Path local;

    public FotoStorageLocal() {
        this(getDefault().getPath(System.getenv("HOME"), ".brewerfotos"));
    }

    public FotoStorageLocal(Path path) {
        this.local = path;
        criarPastas();
    }

    @Override
    public String salvar(MultipartFile[] files) {
        String novoNome = null;
        if (files != null && files.length > 0) {
            MultipartFile arquivo = files[0];
            novoNome = renomearArquivo(arquivo.getOriginalFilename());
            try {
                arquivo.transferTo(new File(this.local.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome));
            } catch (IOException e) {
                throw new RuntimeException("Erro salvando a foto", e);
            }
        }

        try {
            Thumbnails.of(this.local.resolve(novoNome).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
        } catch (IOException e) {
            throw new RuntimeException("Erro gerando thumbnail", e);
        }

        return novoNome;
    }

    @Override
    public byte[] recuperar(String nome) {
        try {
            Path path_foto = this.local.resolve(nome);
            logger.debug("recuperar FOTO URL: " + nome);
            logger.debug("recuperar PATH URL: " + path_foto);
            return Files.readAllBytes(path_foto);
        } catch (IOException e) {
            throw new RuntimeException("Erro lendo a foto", e);
        }
    }

    @Override
    public byte[] recuperarThumbnail(String fotoCerveja) {
        return recuperar(Constantes.THUMBNAIL_PREFIX + fotoCerveja);
    }

    @Override
    public void excluir(String foto) {
        try {
            Files.deleteIfExists(this.local.resolve(foto));
            Files.deleteIfExists(this.local.resolve(Constantes.THUMBNAIL_PREFIX + foto));
        } catch (IOException e) {
            logger.warn(String.format("Erro apagando foto '%s'. Mensagem: %s", foto, e.getMessage()));
        }

    }

    @Override
    public String getUrl(String foto) {
        logger.debug("getUrl FOTO URL: " + foto);
        return "http://localhost:8080/brewer/fotos/" + foto;
    }

    private void criarPastas() {
        try {
            Files.createDirectories(this.local);

            if (logger.isDebugEnabled()) {
                logger.debug("Pastas criadas para salvar fotos.");
                logger.debug("Pasta default: " + this.local.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta para salvar foto", e);
        }
    }

}
