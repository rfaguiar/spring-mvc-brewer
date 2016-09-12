package com.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.brewer.brewer.storage.FotoStorage;
import com.brewer.dto.FotoDTO;
import com.brewer.storage.FotoStorageRunnable;

@RestController
@RequestMapping("/fotos")
public class FotosController {

	@Autowired
	private FotoStorage fotoStorage;
	
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files){
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();
		Thread thread = new Thread(new FotoStorageRunnable(files, resultado, fotoStorage));
		thread.start();
		return resultado;
	}
	
	@GetMapping("/temp/{nome:.*}")
	public byte[] recuperarFotoTemporaria(@PathVariable("nome") String nomeFoto){
		return fotoStorage.recuperarFotoTemporaria(nomeFoto);
	}
	
	@GetMapping("/{nome:.*}")
	public byte[] recuperarFoto(@PathVariable("nome") String nomeFoto){
		return fotoStorage.recuperarFoto(nomeFoto);
	}
}
