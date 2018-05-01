package com.brewer.builder;

import com.brewer.dto.CervejaDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class CervejaDTOBuilder {

    private CervejaDTO cervejaDTO;

    private CervejaDTOBuilder() {
        cervejaDTO = new CervejaDTO();
    }

    public static CervejaDTOBuilder get() {
        return new CervejaDTOBuilder();
    }

    public CervejaDTOBuilder codigo(Long codigo) {
        cervejaDTO.setCodigo(codigo);
        return this;
    }

    public CervejaDTOBuilder sku(String sku) {
        cervejaDTO.setSku(sku);
        return this;
    }

    public CervejaDTOBuilder nome(String nome) {
        cervejaDTO.setNome(nome);
        return this;
    }

    public CervejaDTOBuilder origem(String origem) {
        cervejaDTO.setOrigem(origem);
        return this;
    }

    public CervejaDTOBuilder valor(BigDecimal valor) {
        cervejaDTO.setValor(valor);
        return this;
    }

    public CervejaDTOBuilder foto(String foto) {
        cervejaDTO.setFoto(foto);
        return this;
    }

    public CervejaDTOBuilder urlThumbnailFoto(String urlThumbnailFoto) {
        cervejaDTO.setUrlThumbnailFoto(urlThumbnailFoto);
        return this;
    }

    public CervejaDTOBuilder but() {
        return get().codigo(cervejaDTO.getCodigo())
                .sku(cervejaDTO.getSku())
                .nome(cervejaDTO.getNome())
                .origem(cervejaDTO.getOrigem())
                .valor(cervejaDTO.getValor())
                .foto(cervejaDTO.getFoto())
                .urlThumbnailFoto(cervejaDTO.getUrlThumbnailFoto());
    }

    public CervejaDTO build() {
        return cervejaDTO;
    }


    public static boolean validarCervejaDTO(CervejaDTO dto1, CervejaDTO dto2) {
        return dto1.getCodigo().equals(dto2.getCodigo()) &&
                dto1.getNome().equals(dto2.getNome()) &&
                dto1.getSku().equals(dto2.getSku()) &&
                dto1.getOrigem().equals(dto2.getOrigem()) &&
                dto1.getValor().equals(dto2.getValor()) &&
                dto1.getFoto().equals(dto2.getFoto()) &&
                dto1.getUrlThumbnailFoto().equals(dto2.getUrlThumbnailFoto());
    }

    public static boolean validarListaCervejaDto(List<CervejaDTO> lista1, List<CervejaDTO> lista2) {
        return validarCervejaDTO(lista1.get(0), lista2.get(0));
    }

    public static List<CervejaDTO> criarListaCervejaDto() {
        List<CervejaDTO> cervejaDTOS = new ArrayList<>();
        cervejaDTOS.add(criarCervejaDto());
        return cervejaDTOS;
    }

    public static CervejaDTO criarCervejaDto() {
        return CervejaDTOBuilder.get()
                .codigo(new Long(1))
                .nome("nomeTest")
                .sku("skuTest")
                .origem("origemTest")
                .valor(new BigDecimal(123))
                .foto("fotoTest")
                .urlThumbnailFoto("urlFotoThumbnail")
                .build();
    }
}
