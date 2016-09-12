package com.brewer.service.event.cerveja;

import org.springframework.util.StringUtils;

import com.brewer.model.Cerveja;

public class CervejaSalvaEvent {
	
	private Cerveja cerveja;	

	public CervejaSalvaEvent(Cerveja cerveja) {
		super();
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}
	
	public boolean isFoto(){
		return !StringUtils.isEmpty(cerveja.getFoto());
	}
	
	
}
