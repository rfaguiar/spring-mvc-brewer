# 🚢 Infraestrutura, Contêineres e Orquestração

Este guia documenta toda a infraestrutura do **Brewer**, detalhando as definições de contêineres Docker, automação via Makefile e manifestos de orquestração para **Kubernetes (Minikube)** e **Red Hat OpenShift**.

---

## 1. Visão Geral da Topologia de Infraestrutura

```mermaid
flowchart TB
    subgraph LocalDocker ["🐳 Ambiente Docker / Docker-Compose"]
        D_App["brewer-app (Tomcat 8 + JPDA :8000)"]
        D_DB["mysql56 (MySQL 5.6 :3306)"]
        D_Net["Rede Bridge (brewer-network)"]
        D_App <--> D_Net <--> D_DB
    end

    subgraph K8s ["☸️ Kubernetes Cluster (Minikube / Namespace: dev-to)"]
        Ingress["Ingress Controller (dev.brewer.local:80)"]
        SvcApp["Service: brewer (Headless)"]
        PodApp["Deployment: brewer (rfaguiar/brewer:latest)"]
        SvcDB["Service: mysql (Headless)"]
        PodDB["Deployment: mysql (mysql:5.6)"]

        Ingress --> SvcApp --> PodApp
        PodApp -->|Conexao JDBC: mysql:3306| SvcDB --> PodDB
    end

    subgraph OCP ["🔴 Red Hat OpenShift"]
        Route["OpenShift Route (brewer.apps.*)"]
        OCPSvc["Service: brewer (:8080)"]
        DC["DeploymentConfig: brewer (Rolling)"]
        Secrets["K8s Secrets (aws-secret, send-grid, jawsdb)"]

        Route --> OCPSvc --> DC
        Secrets -.->|Injeção de Env| DC
    end
```

---

## 2. Docker e Multi-Stage Build

O arquivo [`Dockerfile`](../Dockerfile) utiliza a estratégia de **Multi-Stage Build** para manter a imagem final enxuta, separando a compilação do runtime.

### Estrutura do Dockerfile Explicada

```dockerfile
# Estágio 1: Compilação com Maven e OpenJDK 8
FROM maven:3.3-jdk-8 as build
COPY . ./app
WORKDIR /app
RUN mvn package

# Estágio 2: Runtime com Apache Tomcat 8 JRE8
FROM tomcat:8.0-jre8
MAINTAINER Rogerio Aguiar < rfaguiar1@gmail.com>

# Adiciona credenciais administrativas ao Tomcat
ADD tomcat8/tomcat-users.xml $CATALINA_HOME/conf/

# Remove aplicações padrão do Tomcat (examples, manager, docs)
RUN rm -Rf $CATALINA_HOME/webapps/*

# Copia o artefato WAR gerado no estágio 1 renomeando para ROOT.war (contexto raiz /)
COPY --from=build app/target/*.war $CATALINA_HOME/webapps/ROOT.war

VOLUME $CATALINA_HOME/webapps

# Habilita depuração remota JPDA na porta 8000
ENV JPDA_ADDRESS="8000"
ENV JPDA_TRANSPORT="dt_socket"

# Expõe porta da aplicação (8080) e de remote debug (8000)
EXPOSE 8080 8000

# Ponto de entrada iniciando o Tomcat em modo JPDA Debug
ENTRYPOINT ["catalina.sh", "jpda", "run"]
```

### Principais Características:
- **Zero Dependência do Host**: O build ocorre dentro do primeiro contêiner Maven.
- **ROOT.war**: O aplicativo responde na raiz HTTP (`/`), eliminando a necessidade de prefixos como `/brewer`.
- **Remote Debug Ready**: O Tomcat é iniciado com `catalina.sh jpda run`, permitindo conectar IDEs (IntelliJ IDEA, Eclipse, VS Code) diretamente na porta `8000`.

---

## 3. Docker Compose

O arquivo [`docker-compose.yml`](../docker-compose.yml) orquestra a aplicação e a rede de desenvolvimento local.

```yaml
version: '3'

networks:
  brewer-network:
    driver: bridge

services:
  brewer-app:
    ports:
      - "8080:8080"
      - "8000:8000" # Porta de remote debug
    image: rfaguiar/brewer
    container_name: brewer-app
    networks:
      - brewer-network
    environment:
      - JAVA_OPTS='-Dspring.profiles.active=docker-desenv'
      - JDBC_URL=jdbc:mysql://172.17.0.1:3306/brewer?useSSL=false
      - JDBC_USER=root
      - JDBC_PASS=root
```

### Comandos Úteis do Docker Compose:
```bash
# Subir ambiente em background
docker-compose up -d

# Visualizar status dos containers
docker-compose ps

# Acompanhar logs em tempo real
docker-compose logs --tail="all" --follow

# Parar e remover os containers
docker-compose down
```

---

## 4. Orquestração com Kubernetes (Minikube)

Os manifestos Kubernetes encontram-se na pasta [`kubernetes/`](../kubernetes) e utilizam o namespace dedicado `dev-to`.

### 4.1 Estrutura de Manifestos

```
kubernetes/
├── app/
│   ├── brewer-deployment.yaml  # Deployment do container da aplicação
│   ├── brewer-service.yaml     # Headless Service interno (clusterIP: None)
│   └── brewer-ingress.yaml     # Ingress com host dev.brewer.local
└── mysql/
    ├── mysql-deployment.yaml   # Deployment do MySQL 5.6
    └── mysql-service.yaml      # Headless Service interno para o banco
```

### 4.2 Detalhes dos Objetos Kubernetes

#### 1. Banco de Dados MySQL ([`mysql/`](../kubernetes/mysql/))
- **Deployment**: Utiliza a imagem `mysql:5.6`, estratégia `Recreate`, com variáveis `MYSQL_ROOT_PASSWORD=root_pwd`, `MYSQL_USER=myapp`, `MYSQL_PASSWORD=myapp_pwd`, `MYSQL_DATABASE=brewer`.
- **Service**: Cria um serviço com nome `mysql` no namespace `dev-to`, permitindo que outros pods resolvam o banco internamente via DNS por `mysql:3306`.

#### 2. Aplicação Brewer ([`app/`](../kubernetes/app/))
- **Deployment**: Define réplicas da imagem `rfaguiar/brewer`, injetando:
  ```yaml
  env:
    - name: JAVA_OPTS
      value: '-Dspring.profiles.active=docker-desenv'
    - name: JDBC_URL
      value: 'jdbc:mysql://mysql:3306/brewer?useSSL=false'
    - name: JDBC_USER
      value: root
    - name: JDBC_PASS
      value: root_pwd
  ```
- **Service**: Service headless (`clusterIP: None`) na porta 8080 com selector `app: brewer`.
- **Ingress**: Roteia tráfego HTTP com base no hostname virtual:
  ```yaml
  rules:
    - host: dev.brewer.local
      http:
        paths:
          - path: /
            backend:
              serviceName: brewer
              servicePort: 8080
  ```

> [!TIP]
> Para acessar `http://dev.brewer.local` localmente pelo navegador, adicione o IP do Minikube ao seu arquivo `hosts` (`/etc/hosts` no Linux/macOS ou `C:\Windows\System32\drivers\etc\hosts` no Windows):
> ```text
> <MINIKUBE_IP> dev.brewer.local
> ```

---

## 5. Red Hat OpenShift

A pasta [`openshift/`](../openshift) traz recursos nativos de produção para a plataforma PaaS OpenShift da Red Hat.

### 5.1 Recursos OpenShift

| Arquivo | Kind / Recurso | Descrição Técnica |
| :--- | :--- | :--- |
| [`deployment.yaml`](../openshift/deployment.yaml) | `DeploymentConfig` (apps.openshift.io/v1) | Gerencia a estratégia de rollout `Rolling` (maxSurge: 25%, maxUnavailable: 25%) e injeção segura de Secrets. |
| [`service.yaml`](../openshift/service.yaml) | `Service` (v1) | Expõe a porta interna `8080-tcp` com balanceamento `ClusterIP`. |
| [`route.yaml`](../openshift/route.yaml) | `Route` (route.openshift.io/v1) | Roteador ingress nativo do OpenShift com terminação e roteamento com peso 100%. |

### 5.2 Gerenciamento de Secrets no OpenShift

O manifesto do `DeploymentConfig` consome segredos sensíveis sem expô-los no controle de versão:
- `aws-secret`: chaves de API da AWS S3 (`AWS_ACESS_KEY_ID`, `AWS_SECRET_KEY_ID`).
- `send-grid`: credenciais do serviço SMTP SendGrid (`SEND_GRID_USERNAME`, `SEND_GRID_PASSWORD`).
- `jawsdb`: URL JDBC da instância de banco de dados na nuvem (`JAWSDB_URL`).

```bash
# Exemplo de criação dos segredos antes do deploy no OpenShift:
oc create secret generic aws-secret \
  --from-literal=AWS_ACESS_KEY_ID=sua_access_key \
  --from-literal=AWS_SECRET_KEY_ID=sua_secret_key

oc create secret generic send-grid \
  --from-literal=SEND_GRID_USERNAME=apikey \
  --from-literal=SEND_GRID_PASSWORD=sua_api_key_sendgrid

oc create secret generic jawsdb \
  --from-literal=JAWSDB_URL=mysql://usuario:senha@host:3306/brewer
```

---

## 6. Automação com Makefile

O arquivo [`Makefile`](../Makefile) centraliza tarefas comuns de compilação, containers e orquestração.

### Catálogo Completo de Regras do Makefile

| Comando | Descrição da Ação |
| :--- | :--- |
| `make help` | Exibe a lista formatada de todas as regras disponíveis. |
| `make check` | Verifica versões instaladas de `make`, `minikube`, `kubectl` e `vboxmanage`. |
| `make build-app` | Executa o build da aplicação Java via `mvn clean package`. |
| `make run-db` | Sobe um container isolado do MySQL 5.6 na porta 3306 com senhas padrão. |
| `make run-app` | Sobe o contêiner `rfaguiar/brewer:latest` linkado ao contêiner `mysql56`. |
| `make stop-app` | Para a execução do contêiner da aplicação. |
| `make stop-db` | Para o contêiner do banco de dados MySQL. |
| `make rm-app` | Para e remove o contêiner da aplicação. |
| `make rm-db` | Para e remove o contêiner do MySQL. |
| `make k-setup` | Inicializa o cluster Minikube (`2 CPUs`, `4096MB RAM`), habilita addons `ingress` e `metrics-server`, e cria o namespace `dev-to`. |
| `make k-start` | Inicia a máquina virtual existente do Minikube. |
| `make k-deploy-db` | Aplica os manifestos do MySQL no cluster Kubernetes (`kubectl apply -f kubernetes/mysql/`). |
| `make k-build-image` | Compila a aplicação e constrói a imagem Docker diretamente dentro do daemon do Minikube (`eval $(minikube docker-env)`). |
| `make k-deploy-app` | Aplica os manifestos da aplicação no cluster (`kubectl apply -f kubernetes/app/`). |
| `make k-all` | Executa a sequência completa de automação: `k-setup` -> `k-deploy-db` -> `k-build-image` -> `k-deploy-app`. |
| `make k-stop` | Para a máquina virtual do Minikube. |
| `make k-delete` | Para e destrói completamente o ambiente Minikube. |
| `make oc-deploy-app` | Aplica todos os manifestos do diretório OpenShift (`oc apply -f openshift/`). |
