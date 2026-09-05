# 🚀 Guia de Execução Local, Configuração e Banco de Dados

Este guia fornece instruções passo a passo para configurar, compilar e executar o **Brewer** em ambiente local, seja via Maven puro, contêineres Docker, Docker Compose ou Minikube (Kubernetes).

---

## 1. Pré-requisitos de Ambiente

Para executar o projeto em sua máquina local, certifique-se de possuir instalado:
- **Java JDK 8** (Oracle JDK 8 ou OpenJDK 8)
- **Apache Maven 3.3+**
- **MySQL Server 5.6 ou 5.7** (ou Docker)
- **Docker e Docker Compose** (opcional, para execução via contêineres)
- **Minikube e Kubectl** (opcional, para execução em cluster Kubernetes local)

---

## 2. Preparação do Banco de Dados MySQL

### 2.1 Instalação do Cliente MySQL (Linux Ubuntu / Debian)
```bash
sudo apt-get update
sudo apt-get install mysql-client
```

### 2.2 Conexão ao Servidor MySQL
```bash
mysql -u root -p -h 127.0.0.1 -P 3306
```

### 2.3 Criação do Banco de Dados
No prompt do MySQL, crie a base de dados com codificação UTF-8:
```sql
CREATE DATABASE brewer DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
exit;
```

### 2.4 Execução das Migrações com Flyway via Maven
O projeto utiliza o Flyway para criar todas as tabelas e dados iniciais automaticamente:
```bash
mvn -Dflyway.user=root \
    -Dflyway.password=root \
    -Dflyway.url=jdbc:mysql://localhost:3306/brewer?useSSL=false \
    flyway:migrate
```

---

## 3. Formas de Execução Local

### Opção A: Execução via Docker Compose (Recomendada e Mais Rápida)
Com o Docker instalado, você não precisa configurar Java ou MySQL localmente:

```bash
# 1. Subir a aplicação em background
docker-compose up -d

# 2. Visualizar status dos containers em execução
docker-compose ps

# 3. Acompanhar os logs da aplicação em tempo real
docker-compose logs --tail="all" --follow

# 4. Parar e remover os containers ao encerrar
docker-compose down
```
Acesse a aplicação no navegador em: `http://localhost:8080`

---

### Opção B: Compilação e Execução com Maven e Webapp Runner
Você pode compilar o projeto gerando o pacote `.war` e executá-lo com o Tomcat embutido (`webapp-runner`):

#### 1. Compilação com Perfil Local:
```bash
# Compilação padrão (Perfil local)
mvn clean package -Plocal

# Ou com suporte a JNDI
mvn clean package -Plocal-jndi
```

#### 2. Execução com Webapp Runner:
```bash
java $JAVA_OPTS -Dspring.profiles.active=local -jar target/dependency/webapp-runner.jar target/brewer.war --port 8080
```

---

### Opção C: Automação via Makefile e Docker Isolado
Se preferir gerenciar banco e aplicação separadamente via contêineres:

```bash
# 1. Iniciar apenas o banco de dados MySQL em container
make run-db

# 2. Compilar a aplicação
make build-app

# 3. Subir o container da aplicação linkado ao banco
make run-app

# 4. Parar os serviços
make stop-app
make stop-db
```

---

### Opção D: Execução no Kubernetes Local (Minikube)
O projeto conta com automação completa via Makefile para provisionar e implantar tudo no Minikube:

```bash
# 1. Verificar versões das ferramentas instaladas (kubectl, minikube, virtualbox)
make check

# 2. Executar provisionamento completo (cria cluster, deploy do mysql, build da imagem e deploy da app)
make k-all

# 3. Ou executar passo a passo:
make k-setup        # Cria cluster com 2 CPUs, 4GB RAM e habilita ingress + metrics-server
make k-deploy-db    # Aplica manifests do MySQL no namespace dev-to
make k-build-image # Constrói imagem Docker diretamente no daemon do Minikube
make k-deploy-app   # Aplica manifests da aplicação Brewer
```

Para acessar via navegador no Kubernetes:
1. Obtenha o IP do Minikube: `minikube ip`
2. Adicione ao seu arquivo `hosts` (`/etc/hosts` ou `C:\Windows\System32\drivers\etc\hosts`):
   ```text
   <MINIKUBE_IP> dev.brewer.local
   ```
3. Abra `http://dev.brewer.local` no navegador.

---

## 4. Tabela de Variáveis de Ambiente

Abaixo estão todas as variáveis de ambiente aceitas pela aplicação nos diferentes perfis:

| Variável | Perfil | Obrigatória? | Descrição | Exemplo de Valor |
| :--- | :--- | :--- | :--- | :--- |
| `JAVA_OPTS` | Todos | Opcional | Parâmetros da JVM e ativação de perfis Spring | `-Dspring.profiles.active=docker-desenv` |
| `JDBC_URL` | `docker-desenv` | Sim | URL de conexão JDBC do banco de dados MySQL | `jdbc:mysql://mysql56:3306/brewer?useSSL=false` |
| `JDBC_USER` | `docker-desenv` | Sim | Nome do usuário para autenticação JDBC | `root` |
| `JDBC_PASS` | `docker-desenv` | Sim | Senha do usuário JDBC | `root` |
| `JAWSDB_URL` | `prod` | Sim | URL completa estilo cloud/Heroku/OpenShift com credenciais embutidas | `mysql://user:pass@hostname:3306/database` |
| `SEND_GRID_USERNAME` | `prod` | Sim | Nome de usuário / API Key do serviço SendGrid | `apikey` |
| `SEND_GRID_PASSWORD` | `prod` | Sim | Senha da API Key do SendGrid | `SG.xxxxxxxx...` |
| `BUCKET_NAME` | `prod` | Sim | Nome do Bucket no Amazon S3 para fotos | `brewer-s3-prod` |
| `AWS_ACESS_KEY_ID` | `prod` | Sim | ID da chave de acesso da conta AWS IAM | `AKIAIOSFODNN7EXAMPLE` |
| `AWS_SECRET_KEY_ID` | `prod` | Sim | Chave secreta de acesso AWS IAM | `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY` |

---

## 5. Credenciais de Acesso Padrão

Após a execução da migração `V10__inserir_usuario_administrador.sql` do Flyway, o usuário inicial pré-cadastrado é:

| Campo | Valor |
| :--- | :--- |
| **URL de Login** | `http://localhost:8080/login` |
| **E-mail / Usuário** | `admin@brewer.com` |
| **Senha** | `admin` |
| **Perfil / Papel** | Administrador (`ROLE_CADASTRAR_CIDADE`, `ROLE_CADASTRAR_USUARIO`, `ROLE_CANCELAR_VENDA`) |

---

## 6. Depuração Remota (Remote Debugging JPDA)

A imagem Docker oficial do projeto expõe o Tomcat já preparado para conexão remota de depuração:
- **Porta de Debug**: `8000`
- **Transporte**: `dt_socket`

### Como conectar pelo IntelliJ IDEA:
1. Vá em **Run** -> **Edit Configurations...**
2. Clique em **+** -> **Remote JVM Debug**
3. Configure:
   - **Host**: `localhost`
   - **Port**: `8000`
4. Inicie o contêiner e execute a configuração em modo Debug na IDE. Os breakpoints colocados em qualquer classe Java serão interceptados em tempo real!
