package com.brewer.controller.page;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({UriComponentsBuilder.class})
public class PageWrapperTest {

    private PageWrapper<Object> pageWrapper;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private Page<Object> mockPage;
    @Mock
    private UriComponentsBuilder mockUriBuilder;
    @Mock
    private Sort mockSort;
    @Mock
    private Sort.Order mockOrder;

    @Before
    public void metodoInicializaCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UriComponentsBuilder.class);
        Mockito.when(mockHttpRequest.getRequestURL()).thenReturn(new StringBuffer("url"));
        Mockito.when(mockHttpRequest.getQueryString()).thenReturn("?");
        Mockito.when(UriComponentsBuilder.fromHttpUrl(Matchers.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockPage.getSort()).thenReturn(mockSort);
        Mockito.when(mockSort.getOrderFor(Matchers.anyString())).thenReturn(mockOrder);

        this.pageWrapper = new PageWrapper<>(mockPage, mockHttpRequest);
    }

    @Test
    public void testeMetodoIsVazia() {
        assertTrue(pageWrapper.isVazia());
    }

    @Test
    public void testeMetodoGetAtual() {
        assertNotNull(pageWrapper.getAtual());
    }

    @Test
    public void testeMetodoisPrimeira(){
        assertNotNull(pageWrapper.isPrimeira());
    }

    @Test
    public void testeMetodoisUltima(){
        assertNotNull(pageWrapper.isUltima());
    }

    @Test
    public void testeMetodogetTotal(){
        assertNotNull(pageWrapper.getTotal());
    }

    @Test
    public void testeMetodourlParaPagina() {
        Mockito.when(mockUriBuilder.replaceQueryParam("page", 0)).thenReturn(mockUriBuilder);
        UriComponents mockuriBuilder = Mockito.mock(UriComponents.class);
        Mockito.when(mockUriBuilder.build(true)).thenReturn(mockuriBuilder);
        Mockito.when(mockuriBuilder.encode()).thenReturn(mockuriBuilder);
        Mockito.when(mockuriBuilder.toUriString()).thenReturn("uriEncoded");
        assertNotNull(pageWrapper.urlParaPagina(0));
    }

    @Test
    public void testeMetodoUrlOrdenada() {
        Mockito.when(UriComponentsBuilder.fromUriString(Matchers.anyString())).thenReturn(mockUriBuilder);
        Mockito.when(mockUriBuilder.replaceQueryParam(Matchers.anyString(), Matchers.anyString())).thenReturn(mockUriBuilder);
        UriComponents mockuriBuilder = Mockito.mock(UriComponents.class);
        Mockito.when(mockUriBuilder.build(true)).thenReturn(mockuriBuilder);
        Mockito.when(mockuriBuilder.encode()).thenReturn(mockuriBuilder);
        Mockito.when(mockuriBuilder.toUriString()).thenReturn("uriEncoded");
        assertNotNull(pageWrapper.urlOrdenada("codigo"));
    }

    @Test
    public void testeMetodoDescedente() {
        assertNotNull(pageWrapper.descendente("codigo"));
    }

    @Test
    public void testeMetodoOrdenada() {
        Mockito.when(mockSort.getOrderFor("codigo")).thenReturn(mockOrder);
        assertTrue(pageWrapper.ordenada("codigo"));
    }

    @Test
    public void testeMetodoOrdenadaQuandoNenhumaDeveRetornarFalse() {
        Mockito.when(mockSort.getOrderFor("codigo")).thenReturn(null);
        assertFalse(pageWrapper.ordenada("codigo"));
    }

    /*
	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;

		if (order == null) {
			return false;
		}

		return page.getSort().getOrderFor(propriedade) != null;
	}
     */

}