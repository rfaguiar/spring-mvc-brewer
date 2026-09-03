# 📚 Catálogo de Tecnologias, Padrões e Exemplos Práticos

Este documento funciona como um **guia de consulta rápida** e catálogo de soluções arquiteturais e padrões de código implementados no **Brewer**, servindo como referência prática para engenheiros de software no dia a dia.

---

## 📑 Sumário do Catálogo

1. [Spring MVC 5 - Controladores, Validação e Conversão](#1-spring-mvc-5---controladores-validação-e-conversão)
2. [Spring Security 4 - Autenticação, Autorização e Sessão](#2-spring-security-4---autenticação-autorização-e-sessão)
3. [Spring Data JPA & Hibernate 5 - Repositórios, Queries Dinâmicas e SQL Nativo](#3-spring-data-jpa--hibernate-5---repositórios-queries-dinâmicas-e-sql-nativo)
4. [Flyway - Migrações Versionadas de Banco de Dados](#4-flyway---migrações-versionadas-de-banco-de-dados)
5. [EhCache 3 & JCache (JSR-107) - Estratégia de Caching](#5-ehcache-3--jcache-jsr-107---estratégia-de-caching)
6. [Storage Strategy Pattern - Armazenamento Local vs AWS S3](#6-storage-strategy-pattern---armazenamento-local-vs-aws-s3)
7. [Thumbnailator - Processamento e Redimensionamento de Imagens](#7-thumbnailator---processamento-e-redimensionamento-de-imagens)
8. [Processamento Assíncrono e Envio de E-mails com Thymeleaf](#8-processamento-assíncrono-e-envio-de-e-mails-com-thymeleaf)
9. [Dialeto Customizado Thymeleaf 3 - Tags e Atributos Próprios](#9-dialeto-customizado-thymeleaf-3---tags-e-atributos-próprios)
10. [Bean Validation & Custom Constraints - Validações Avançadas](#10-bean-validation--custom-constraints---validações-avançadas)
11. [Isolamento de Estado em Sessão por Aba (UUID TabelaHash)](#11-isolamento-de-estado-em-sessão-por-aba-uuid-tabelahash)
12. [Integração Front-end: Handlebars, Chart.js, UIKit e SweetAlert](#12-integração-front-end-handlebars-chartjs-uikit-e-sweetalert)

---

## 1. Spring MVC 5 - Controladores, Validação e Conversão

### 1.1 Conversão Automática de Entidades na URL (`DomainClassConverter`)
Permite injetar a entidade JPA diretamente no parâmetro do método do controller apenas passando seu ID na URL:

- **Configuração**: [`WebConfig.java`](../src/main/java/com/brewer/config/WebConfig.java)
  ```java
  @Bean
  public DomainClassConverter<FormattingConversionService> domainClassConverter(){
      return new DomainClassConverter<>(mvcConversionService());
  }
  ```
- **Uso no Controller**: [`CervejasController.java`](../src/main/java/com/brewer/controller/CervejasController.java)
  ```java
  @GetMapping("/{codigo}")
  public ModelAndView editar(@PathVariable("codigo") Cerveja cerveja) {
      // O Spring Data busca automaticamente a entidade Cerveja no banco pelo ID {codigo}!
      ModelAndView mv = novo(cerveja);
      mv.addObject(cerveja);
      return mv;
  }
  ```

### 1.2 Formatação e Conversão de Tipos Customizados
- **Configuração**: [`WebConfig.java`](../src/main/java/com/brewer/config/WebConfig.java)
  ```java
  @Bean
  public FormattingConversionService mvcConversionService(){
      DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
      conversionService.addConverter(new EstiloConverter());
      conversionService.addConverter(new CidadeConverter());
      conversionService.addConverter(new EstadoConverter());
      conversionService.addConverter(new GrupoConverter());

      BigDecimalFormatter bigDecimalFormatter = new BigDecimalFormatter("#,##0.00");
      conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
      
      DateTimeFormatterRegistrar dateTimeFormatter = new DateTimeFormatterRegistrar();
      dateTimeFormatter.setDateFormatter(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      dateTimeFormatter.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm"));
      dateTimeFormatter.registerFormatters(conversionService);
      return conversionService;
  }
  ```

### 1.3 Tratamento Global de Exceções (`@ControllerAdvice`)
- **Implementação**: [`ControllerAdviceExceptionHandler.java`](../src/main/java/com/brewer/controller/handler/ControllerAdviceExceptionHandler.java)
  ```java
  @ControllerAdvice
  public class ControllerAdviceExceptionHandler {
      @ExceptionHandler(NomeEstiloJaCadastradoException.class)
      public ResponseEntity<String> handleNomeEstiloJaCadastradoException(NomeEstiloJaCadastradoException e) {
          return ResponseEntity.badRequest().body(e.getMessage());
      }
  }
  ```

---

## 2. Spring Security 4 - Autenticação, Autorização e Sessão

### 2.1 Configuração de Segurança Web
- **Arquivo**: [`SecurityConfig.java`](../src/main/java/com/brewer/config/SecurityConfig.java)

#### Destaques de Implementação:
1. **Criptografia de Senha com BCrypt**:
   ```java
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   ```
2. **Segurança em Nível de Método (Method Security)**:
   ```java
   @EnableGlobalMethodSecurity(prePostEnabled = true)
   ```
   Utilizado em [`CadastroVendaService.java`](../src/main/java/com/brewer/service/CadastroVendaService.java) com SpEL:
   ```java
   @PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
   @Transactional
   public void cancelar(Venda venda) { ... }
   ```
3. **Controle de Concorrência de Sessões (Single Session per User)**:
   ```java
   http.sessionManagement()
       .maximumSessions(1)
       .expiredUrl("/login");
   ```
4. **Custom UserDetailsService**:
   - [`AppUserDetailsService.java`](../src/main/java/com/brewer/security/AppUserDetailsService.java): carrega usuário ativo do banco e mapeia suas permissões para `SimpleGrantedAuthority`.
   - [`UsuarioSistema.java`](../src/main/java/com/brewer/security/UsuarioSistema.java): estende `User` do Spring Security carregando o objeto `Usuario` completo no contexto principal.

---

## 3. Spring Data JPA & Hibernate 5 - Repositórios, Queries Dinâmicas e SQL Nativo

### 3.1 Padrão Repository com Custom Implementations (`*Queries` e `*Impl`)
O Spring Data JPA permite mesclar métodos CRUD automáticos com consultas complexas personalizadas usando a convenção de nomenclatura `<Interface>Impl`:

- Interface Repository: [`Cervejas.java`](../src/main/java/com/brewer/repository/Cervejas.java)
  ```java
  public interface Cervejas extends JpaRepository<Cerveja, Long>, CervejasQueries {
  }
  ```
- Interface de Contrato Customizado: [`CervejasQueries.java`](../src/main/java/com/brewer/repository/helper/cerveja/CervejasQueries.java)
- Implementação com Hibernate Criteria: [`CervejasImpl.java`](../src/main/java/com/brewer/repository/helper/cerveja/CervejasImpl.java)
  ```java
  public class CervejasImpl implements CervejasQueries {
      @PersistenceContext
      private EntityManager manager;

      @Transactional(readOnly = true)
      public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
          Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
          paginacaoUtil.preparar(criteria, pageable);
          adicionarFiltro(filtro, criteria);
          return new PageImpl<>(criteria.list(), pageable, total(filtro));
      }
  }
  ```

### 3.2 Consultas Nativas e Mapeamento para DTOs via XML
Consultas com subqueries complexas ou funções específicas de SQL ficam separadas do código Java em [`consultas-nativas.xml`](../src/main/resources/sql/consultas-nativas.xml):

```xml
<named-native-query name="Vendas.totalPorMes" result-set-mapping="totalPorMesMapping">
    <query>
        select date_format(data_criacao, '%Y/%m') mes
             , count(*) total
        from venda
        where data_criacao > DATE_SUB(NOW(), INTERVAL 6 MONTH)
          and status = 'EMITIDA'
        group by date_format(data_criacao, '%Y/%m')   
        order by date_format(data_criacao, '%Y/%m') desc
    </query>
</named-native-query>

<sql-result-set-mapping name="totalPorMesMapping">
    <constructor-result target-class="com.brewer.dto.VendaMes">
        <column name="mes" class="java.lang.String"/>
        <column name="total" class="java.lang.Integer"/>
    </constructor-result>
</sql-result-set-mapping>
```

### 3.3 JPA Entity Listener para Carregamento de URLs Dinâmicas
- **Classe**: [`CervejaEntityListener.java`](../src/main/java/com/brewer/repository/listener/CervejaEntityListener.java)
  ```java
  public class CervejaEntityListener {
      @Autowired
      private FotoStorage fotoStorage;

      @PostLoad
      public void postLoad(final Cerveja cerveja) {
          SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
          cerveja.setUrlFoto(fotoStorage.getUrl(cerveja.getFotoOuMock()));
          cerveja.setUrlThumbnailFoto(fotoStorage.getUrl(Constantes.THUMBNAIL_PREFIX + cerveja.getFotoOuMock()));
      }
  }
  ```

---

## 4. Flyway - Migrações Versionadas de Banco de Dados

Localização dos scripts: [`src/main/resources/db/migration/`](../src/main/resources/db/migration)

### Ciclo de Migrações:
- `V01`: Criação de tabelas `estilo` e `cerveja` + carga de estilos iniciais.
- `V02`: Adição da coluna `quantidade_estoque` na tabela `cerveja`.
- `V03`: Adição das colunas `foto` e `content_type` em `cerveja`.
- `V04`: Criação das tabelas `estado` e `cidade` + carga de dados dos estados e capitais.
- `V05`: Criação da tabela `cliente`.
- `V06`: Restrição `NOT NULL` nas colunas de CPF/CNPJ.
- `V07`: Tabelas de controle de acesso: `usuario`, `grupo`, `permissao`, `usuario_grupo`, `grupo_permissao`.
- `V08`: Restrição `NOT NULL` para status `ativo` de usuário.
- `V09`: Carga de grupos padrão (`Administrador` e `Vendedor`).
- `V10`: Criação do usuário administrador default com hash BCrypt.
- `V11`: Mapeamento de permissões e associação do administrador.
- `V12`: Criação de `venda` e `item_venda`.
- `V13`: Criação da role `ROLE_CANCELAR_VENDA`.

### Execução via Maven:
```bash
mvn -Dflyway.user=root -Dflyway.password=root -Dflyway.url=jdbc:mysql://localhost:3306/brewer?useSSL=false flyway:migrate
```

---

## 5. EhCache 3 & JCache (JSR-107) - Estratégia de Caching

### 5.1 Configuração XML
- **Arquivo**: [`ehcache.xml`](../src/main/resources/cache/ehcache.xml)
  ```xml
  <ehcache:cache alias="cidades">
      <ehcache:expiry>
          <ehcache:tti unit="seconds">10</ehcache:tti>
      </ehcache:expiry>
      <ehcache:resources>
          <ehcache:heap unit="entries">3</ehcache:heap>
      </ehcache:resources>
  </ehcache:cache>
  ```

### 5.2 Uso nas Classes
- **Leitura com Cache**: [`Cidades.java`](../src/main/java/com/brewer/repository/Cidades.java)
  ```java
  @Cacheable(value = "cidades", key = "#codigoEstado")
  public List<Cidade> findByEstadoCodigo(Long codigoEstado);
  ```
- **Invalidação com `@CacheEvict`**: [`CadastroCidadeService.java`](../src/main/java/com/brewer/service/CadastroCidadeService.java)
  ```java
  @Transactional
  @CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()")
  public void salvar(Cidade cidade) {
      cidadesRepo.save(cidade);
  }
  ```

---

## 6. Storage Strategy Pattern - Armazenamento Local vs AWS S3

### 6.1 A Interface Comum
- **Contrato**: [`FotoStorage.java`](../src/main/java/com/brewer/storage/FotoStorage.java)

### 6.2 Implementação Local (Perfil `local` / `docker-desenv`)
- **Classe**: [`FotoStorageLocal.java`](../src/main/java/com/brewer/storage/local/FotoStorageLocal.java)
  - Salva fotos em `$HOME/.brewerfotos`.
  - Gera thumbnails de `40x68` px com prefixo `thumbnail.`.
  - Retorna URL do controller local: `http://localhost:8080/fotos/{foto}`.

### 6.3 Implementação Cloud AWS S3 (Perfil `prod`)
- **Classe**: [`FotoStorageS3.java`](../src/main/java/com/brewer/storage/s3/FotoStorageS3.java)
  - Faz upload para Amazon S3 via `AmazonS3.putObject()`.
  - Configura ACL pública `GroupGrantee.AllUsers` com permissão de leitura.
  - Gera URL direta na AWS: `https://s3.amazonaws.com/{bucketName}/{foto}`.

---

## 7. Thumbnailator - Processamento e Redimensionamento de Imagens

O redimensionamento de imagens para otimização de performance na listagem é feito com a biblioteca **Thumbnailator**:

```java
Thumbnails.of(this.path.resolve(novoNome).toString())
          .size(40, 68)
          .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
```

---

## 8. Processamento Assíncrono e Envio de E-mails com Thymeleaf

- **Mailer**: [`Mailer.java`](../src/main/java/com/brewer/mail/Mailer.java)
- **Template de E-mail**: [`ResumoVenda.html`](../src/main/resources/templates/mail/ResumoVenda.html)

### Destaques:
- Anotado com `@Async`: a chamada ao método não bloqueia a resposta HTTP ao usuário.
- Renderização do HTML do e-mail usando o motor do Thymeleaf em background:
  ```java
  Context context = new Context(new Locale("pt", "BR"));
  context.setVariable("venda", venda);
  String email = thymeleaf.process("mail/ResumoVenda", context);
  ```
- Anexos inline CID (Content-ID) para exibir imagens de cervejas e logomarca diretamente dentro do corpo da mensagem sem depender de bloqueios de imagens externas dos leitores de e-mail.

---

## 9. Dialeto Customizado Thymeleaf 3 - Tags e Atributos Próprios

O projeto registra um dialeto próprio em [`BrewerDialect.java`](../src/main/java/com/brewer/thymeleaf/BrewerDialect.java) com prefixo `brewer:`:

| Tag / Atributo | Classe Processadora | Exemplo de Uso no HTML | O que é Renderizado |
| :--- | :--- | :--- | :--- |
| `<brewer:message/>` | [`MessageElementTagProcessor`](../src/main/java/com/brewer/thymeleaf/processor/MessageElementTagProcessor.java) | `<brewer:message/>` | Inclui fragmentos de alertas de sucesso (`MensagemSucesso.html`) ou erros de validação (`MensagensErroValidacao.html`). |
| `<brewer:order/>` | [`OrderElementTagProcessor`](../src/main/java/com/brewer/thymeleaf/processor/OrderElementTagProcessor.java) | `<brewer:order page="${pagina}" field="sku" text="SKU"/>` | Link no cabeçalho da tabela com ícones de ordenação ascendente/descendente (`glyphicon-sort`, `glyphicon-sort-by-attributes`). |
| `<brewer:pagination/>` | [`PaginationElementTagProcessor`](../src/main/java/com/brewer/thymeleaf/processor/PaginationElementTagProcessor.java) | `<brewer:pagination page="${pagina}"/>` | Barra de paginação completa Bootstrap com controle de página inicial, anterior, próximas e última. |
| `brewer:classforerror` | [`ClassForErrorAttributeTagProcessor`](../src/main/java/com/brewer/thymeleaf/processor/ClassForErrorAttributeTagProcessor.java) | `<div class="form-group" brewer:classforerror="nome">` | Adiciona a classe CSS `has-error` automaticamente se houver violação de validação no campo. |
| `brewer:menu` | [`MenuAttributeTagProcessor`](../src/main/java/com/brewer/thymeleaf/processor/MenuAttributeTagProcessor.java) | `<li brewer:menu="@{/cervejas}">` | Adiciona a classe CSS `is-active` caso a URI atual coincida com a rota informada. |

---

## 10. Bean Validation & Custom Constraints - Validações Avançadas

### 10.1 Validação de Formato SKU (`@SKU`)
- **Anotação**: [`SKU.java`](../src/main/java/com/brewer/validation/SKU.java)
- Valida se o código de SKU obedece ao padrão de 2 letras seguido de 4 números usando regex:
  ```java
  @Pattern(regexp = "([a-zA-Z]{2}\\d{4})?")
  ```

### 10.2 Validação Cruzada de Confirmação de Senha (`@AtributoConfirmacao`)
- **Anotação**: [`AtributoConfirmacao.java`](../src/main/java/com/brewer/validation/AtributoConfirmacao.java)
- **Validador**: [`AtributoConfirmacaoValidator.java`](../src/main/java/com/brewer/validation/validator/AtributoConfirmacaoValidator.java)
- Inspeciona dinamicamente dois atributos de uma classe via reflexão (`BeanUtils.getProperty`) garantindo que coincidam (ex: `senha` e `confirmacaoSenha`).

---

## 11. Isolamento de Estado em Sessão por Aba (UUID TabelaHash)

Para evitar que duas abas abertas pelo mesmo usuário no navegador interfiram no carrinho de compras uma da outra:

- **Classes**: [`TabelasItensSession.java`](../src/main/java/com/brewer/session/TabelasItensSession.java) e [`TabelaItensVenda.java`](../src/main/java/com/brewer/session/TabelaItensVenda.java)
- Ao abrir uma nova tela de venda, o front-end gera um UUID aleatório (`uuid`) e o atribui ao campo oculto `uuid`.
- No backend, o componente gerenciado pelo Spring armazena uma coleção de carrinhos indexados pelo hash:
  ```java
  @Component
  @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public class TabelasItensSession {
      private Set<TabelaItensVenda> tabelas = new HashSet<>();

      public void adicionarItem(String uuid, Cerveja cerveja, int quantidade) {
          TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
          tabela.adicionarItem(cerveja, quantidade);
          tabelas.add(tabela);
      }
  }
  ```

---

## 12. Integração Front-end: Handlebars, Chart.js, UIKit e SweetAlert

O front-end utiliza uma arquitetura modular em arquivos JavaScript puros no diretório [`src/main/resources/static/javascripts/`](../src/main/resources/static/javascripts):

- **Modularização via Namespace `Brewer`**: Todos os scripts declaram objetos dentro do namespace global `Brewer = Brewer || {};`.
- **Upload com UIKit e Handlebars**: Ao selecionar uma imagem de cerveja, o UIKit envia via AJAX para o `FotosController`, que devolve um JSON com o nome temporário. O script compila o template Handlebars [`hbs/FotoCerveja.html`](../src/main/resources/templates/hbs/FotoCerveja.html) e renderiza a prévia visual sem recarregar a página.
- **Diálogos de Exclusão com SweetAlert**: [`brewer.dialogo-excluir.js`](../src/main/resources/static/javascripts/brewer.dialogo-excluir.js) intercepta o clique em botões de exclusão, exibe modal de confirmação SweetAlert e envia requisição HTTP `DELETE` incluindo o cabeçalho de proteção CSRF (`X-CSRF-TOKEN`).
- **Dashboard e Gráficos**: [`dashboard.graficos.js`](../src/main/resources/static/javascripts/dashboard.graficos.js) consome endpoints REST `/vendas/totalPorMes` e `/vendas/porOrigem` e renderiza gráficos responsivos com Chart.js.
