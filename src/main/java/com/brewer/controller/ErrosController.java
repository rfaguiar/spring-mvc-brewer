package com.brewer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrosController {

	@GetMapping("404")
	public String paginaNaoEncontrada(){
		return String.valueOf(HttpStatus.NOT_FOUND.value());
	}
	
	@RequestMapping("/500")
	public String erroServidor() {
		return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
