package com.brewer.controller;

import com.brewer.Constantes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SegurancaController {

	@GetMapping("/login")
	public String login(@AuthenticationPrincipal User user){
		if(user != null){
			return Constantes.REDIRECT_CRVEJAS_VIEW;
		}
		return Constantes.LOGIN_VIEW;
	}
	
	@GetMapping("/403")
	public String acessoNegado(){
		return HttpStatus.UNAUTHORIZED.toString();
	}
}
