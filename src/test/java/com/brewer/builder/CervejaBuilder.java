package com.brewer.builder;

import com.brewer.model.Cerveja;
import com.brewer.model.Estilo;
import com.brewer.model.Origem;
import com.brewer.model.Sabor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class CervejaBuilder {

    private Cerveja cerveja;

    private CervejaBuilder() {
        cerveja = new Cerveja();
    }

    public static CervejaBuilder get() {
        return new CervejaBuilder();
    }

    public CervejaBuilder codigo(Long codigo) {
        cerveja.setCodigo(codigo);
        return this;
    }

    public CervejaBuilder sku(String sku) {
        cerveja.setSku(sku);
        return this;
    }

    public CervejaBuilder nome(String nome) {
        cerveja.setNome(nome);
        return this;
    }

    public CervejaBuilder descricao(String descricao) {
        cerveja.setDescricao(descricao);
        return this;
    }

    public CervejaBuilder valor(BigDecimal valor) {
        cerveja.setValor(valor);
        return this;
    }

    public CervejaBuilder teorAlcoolico(BigDecimal teorAlcoolico) {
        cerveja.setTeorAlcoolico(teorAlcoolico);
        return this;
    }

    public CervejaBuilder comissao(BigDecimal comissao) {
        cerveja.setComissao(comissao);
        return this;
    }

    public CervejaBuilder quantidadeEstoque(Integer quantidadeEstoque) {
        cerveja.setQuantidadeEstoque(quantidadeEstoque);
        return this;
    }

    public CervejaBuilder origem(Origem origem) {
        cerveja.setOrigem(origem);
        return this;
    }

    public CervejaBuilder sabor(Sabor sabor) {
        cerveja.setSabor(sabor);
        return this;
    }

    public CervejaBuilder estilo(Estilo estilo) {
        cerveja.setEstilo(estilo);
        return this;
    }

    public CervejaBuilder foto(String foto) {
        cerveja.setFoto(foto);
        return this;
    }

    public CervejaBuilder contentType(String contentType) {
        cerveja.setContentType(contentType);
        return this;
    }

    public CervejaBuilder urlFoto(String urlFoto) {
        cerveja.setUrlFoto(urlFoto);
        return this;
    }

    public CervejaBuilder urlThumbnailFoto(String urlThumbnailFoto) {
        cerveja.setUrlThumbnailFoto(urlThumbnailFoto);
        return this;
    }

    public CervejaBuilder novaFoto(Boolean novaFoto) {
        cerveja.setNovaFoto(novaFoto);
        return this;
    }

    public CervejaBuilder but() {
        return get()
                .codigo(cerveja.getCodigo())
                .sku(cerveja.getSku())
                .nome(cerveja.getNome())
                .descricao(cerveja.getDescricao())
                .valor(cerveja.getValor())
                .teorAlcoolico(cerveja.getTeorAlcoolico())
                .comissao(cerveja.getComissao())
                .quantidadeEstoque(cerveja.getQuantidadeEstoque())
                .origem(cerveja.getOrigem())
                .sabor(cerveja.getSabor())
                .estilo(cerveja.getEstilo())
                .foto(cerveja.getFoto())
                .contentType(cerveja.getContentType())
                .urlFoto(cerveja.getUrlFoto())
                .urlThumbnailFoto(cerveja.getUrlThumbnailFoto())
                .novaFoto(cerveja.isNovaFoto());
    }

    public Cerveja build() {
        return cerveja;
    }

    public static boolean validarCerveja(Cerveja cerveja1, Cerveja cerveja2) {
        return cerveja1.getCodigo().equals(cerveja2.getCodigo()) &&
                cerveja1.getSku().equals(cerveja2.getSku()) &&
                cerveja1.getNome().equals(cerveja2.getNome()) &&
                cerveja1.getDescricao().equals(cerveja2.getDescricao()) &&
                cerveja1.getValor().equals(cerveja2.getValor()) &&
                cerveja1.getTeorAlcoolico().equals(cerveja2.getTeorAlcoolico()) &&
                cerveja1.getComissao().equals(cerveja2.getComissao()) &&
                cerveja1.getQuantidadeEstoque().equals(cerveja2.getQuantidadeEstoque()) &&
                cerveja1.getOrigem().equals(cerveja2.getOrigem()) &&
                cerveja1.getSabor().equals(cerveja2.getSabor()) &&
                cerveja1.getEstilo().equals(cerveja2.getEstilo()) &&
                cerveja1.getFoto().equals(cerveja2.getFoto()) &&
                cerveja1.getUrlFoto().equals(cerveja2.getUrlFoto()) &&
                cerveja1.getUrlThumbnailFoto().equals(cerveja2.getUrlThumbnailFoto());
    }

    public static boolean validarListaCerveja(List<Cerveja> lista1, List<Cerveja> lista2) {
        return validarCerveja(lista1.get(0), lista2.get(0));
    }


    public static List<Cerveja> criarListacervejas() {
        List<Cerveja> cervejas = new ArrayList<>();
        cervejas.add(criarCerveja());
        return cervejas;
    }

    public static Cerveja criarCerveja() {
        return CervejaBuilder.get()
                .codigo(new Long(1))
                .sku("xx9999")
                .nome("cervTeste")
                .descricao("descrTeste")
                .valor(new BigDecimal(123))
                .teorAlcoolico(new BigDecimal(2))
                .comissao(new BigDecimal(2))
                .quantidadeEstoque(500)
                .origem(Origem.NACIONAL)
                .sabor(Sabor.ADOCICADA)
                .estilo(new Estilo())
                .foto("fotTeste")
                .contentType("png")
                .novaFoto(true)
                .urlFoto("urlFotoTest")
                .urlThumbnailFoto("UrlFotoThumb")
                .build();
    }
}
