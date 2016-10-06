package com.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.brewer.brewer.storage.FotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class FotoStorageLocal implements FotoStorage {

	private static final Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);
	
	private Path local;
	private Path localTemporario;
	
	public FotoStorageLocal(){
		this(getDefault().getPath(System.getenv("USERPROFILE"), ".brewerfotos"));
	}

	public FotoStorageLocal(Path local) {
		super();
		this.local = local;
		criarPastas();
	}



	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
			
			if(logger.isDebugEnabled()){
				logger.debug("Pastas criadas para salvar fotos.");
				logger.debug("Pasta default: "+ this.local.toAbsolutePath());
				logger.debug("Pasta temporária: "+ this.localTemporario.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro criando pasta para salvar foto", e);
		}
		
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		if(files != null && files.length > 0){
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			try {
				arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome));
			
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando a foto na pasta temporatia", e);
			}			
		}
		return novoNome;
	}
	
	private String renomearArquivo(String nomeOriginal){
		String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
		if(logger.isDebugEnabled()){
			logger.debug(String.format("Nome original: %s, Novo nome do arquivo %s", nomeOriginal, novoNome));
		}
		return novoNome;
	}

	@Override
	public byte[] recuperarFotoTemporaria(String nomeFoto) {
		try {
			return Files.readAllBytes(this.localTemporario.resolve(nomeFoto));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto temporaria", e);
		}
	}

	@Override
	public void salvar(String foto) {
		try {
			Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
		} catch (IOException e) {
			throw new RuntimeException("Erro movendo a foto para destno final", e);
		}
		
		try {
			Thumbnails.of(this.local.resolve(foto).toString())
				.size(40, 68)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro gerando thumbnail", e);
		};
		
	}

	@Override
	public byte[] recuperarFoto(String foto) {
		try {
			return Files.readAllBytes(this.local.resolve(foto));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto", e);
		}
	}

	@Override
	public byte[] recuperarThumbnail(String fotoCerveja) {
		return recuperarFoto("thumbnail." + fotoCerveja);
	}
}
