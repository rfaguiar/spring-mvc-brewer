package com.brewer.repository.helper.cerveja;

import com.brewer.Constantes;
import com.brewer.dto.CervejaDTO;
import com.brewer.dto.ValorItensEstoque;
import com.brewer.model.Cerveja;
import com.brewer.repository.filter.CervejaFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;
import com.brewer.storage.FotoStorage;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CervejasImpl implements CervejasQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
    @Autowired
    private FotoStorage fotoStorage;

	public CervejasImpl() {}

	public CervejasImpl(EntityManager manager, PaginacaoUtil paginacaoUtil, FotoStorage fotoStorage) {
		this.manager = manager;
		this.paginacaoUtil = paginacaoUtil;
		this.fotoStorage = fotoStorage;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable){
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		
		//filtros e consulta
		adicionarFiltro(filtro, criteria);
		List<Cerveja> list = criteria.list();
		return new PageImpl<>(list, pageable, total(filtro));
	}

	@Override
	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		
		String jpql = "select new com.brewer.dto.CervejaDTO(codigo, sku, nome, origem, valor, foto) "
				+ "from Cerveja where lower(sku) like :skuOuNome or lower(nome) like :skuOuNome";
        List<CervejaDTO> cervejasFiltradas = manager.createQuery(jpql, CervejaDTO.class)
				.setParameter("skuOuNome", skuOuNome.toLowerCase() + "%")
				.getResultList();
        cervejasFiltradas.forEach(c -> c.setUrlThumbnailFoto(fotoStorage.getUrl(Constantes.THUMBNAIL_PREFIX + c.getFoto())));
        return cervejasFiltradas;

    }
	
	@Override
	public ValorItensEstoque valorItensEstoque() {
		String query = "select new com.brewer.dto.ValorItensEstoque(sum(valor * quantidadeEstoque), sum(quantidadeEstoque)) from Cerveja";
		return manager.createQuery(query, ValorItensEstoque.class).getSingleResult();
	}

    private Long total(CervejaFilter filtro) {
        Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(CervejaFilter filtro, Criteria criteria) {
        if(filtro != null){
            if(!StringUtils.isEmpty(filtro.getSku())){
                criteria.add(Restrictions.eq("sku", filtro.getSku()));
            }

            if(!StringUtils.isEmpty(filtro.getNome())){
                criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
            }

            if(isEstiloPresente(filtro)){
                criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));
            }

            if(filtro.getSabor() != null){
                criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
            }

            if(filtro.getOrigem() != null){
                criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
            }

            if(filtro.getValorDe() != null){
                criteria.add(Restrictions.ge("valor", filtro.getValorDe()));
            }

            if(filtro.getValorAte() != null){
                criteria.add(Restrictions.le("valor", filtro.getValorAte()));
            }
        }
    }

    private boolean isEstiloPresente(CervejaFilter filtro) {
        return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
    }
}
