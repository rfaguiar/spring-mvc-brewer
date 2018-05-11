package com.brewer.repository.listener;

import com.brewer.Constantes;
import com.brewer.model.Cerveja;
import com.brewer.storage.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PostLoad;

public class CervejaEntityListener {

    private FotoStorage fotoStorage;

    @Autowired
    public CervejaEntityListener(FotoStorage fotoStorage) {
        this.fotoStorage = fotoStorage;
    }

    @PostLoad
    public void postLoad(final Cerveja cerveja) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        cerveja.setUrlFoto(fotoStorage.getUrl(cerveja.getFotoOuMock()));
        cerveja.setUrlThumbnailFoto(fotoStorage.getUrl(Constantes.THUMBNAIL_PREFIX + cerveja.getFotoOuMock()));
    }
}
