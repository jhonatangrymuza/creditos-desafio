# Sistema de Consulta de Créditos

Sistema completo para consulta de créditos constituídos, desenvolvido com **Spring Boot** (backend) e **Angular** (frontend), utilizando **Docker** para containerização.

## 🏗️ Arquitetura

- **Backend**: Spring Boot 2.x + Java 11
- **Frontend**: Angular 18
- **Banco de Dados**: PostgreSQL 16
- **Proxy**: Nginx
- **Containerização**: Docker + Docker Compose

## 📁 Estrutura do Projeto

```
creditos-desafio/
├── README.md                     # Este arquivo
├── docker-compose.yml            # Orquestração dos containers
├── api-credito/                  # Backend Spring Boot
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── scripts/
│       └── init-db.sql           # Script de inicialização do banco
└── front-credito/               # Frontend Angular
    ├── src/
    ├── package.json
    ├── Dockerfile
    └── nginx.conf               # Configuração do Nginx
```

## 🚀 Como Executar o Projeto

### Pré-requisitos

- **Docker** instalado (versão 20.10+)
- **Docker Compose** instalado (versão 2.0+)
- **Git** para clonar o repositório

### 1. Clonar o Repositório

```bash
git clone <url-do-repositorio>
cd credito
```

### 2. Executar com Docker Compose

```bash
# Subir todos os containers
docker-compose up --build -d

# Verificar status dos containers
docker-compose ps
```

### 3. Aguardar Inicialização

O sistema pode levar alguns minutos para inicializar completamente. Aguarde até que todos os healthchecks estejam funcionando:

```bash
# Acompanhar logs em tempo real
docker-compose logs -f

# Verificar logs específicos se houver problema
docker-compose logs backend
docker-compose logs frontend
docker-compose logs postgres
```

### 4. Acessar a Aplicação

Após a inicialização completa:

- **Frontend (Aplicação Principal)**: http://localhost
- **Backend API**: http://localhost:8080/api
- **PostgreSQL**: localhost:5432
- **KafkaUI interface grafica**: localhost:8090


## 🧪 Como Testar

### 1. Teste via Interface Web
1. Acesse http://localhost
2. Selecione o tipo de busca (NFS-e ou Crédito)
3. Digite um dos números de exemplo
4. Clique em "Buscar"



## 🔍 Monitoramento e Logs

### Verificar Status dos Serviços

```bash
# Status geral
docker-compose ps

# Logs em tempo real
docker-compose logs -f

# Usar apenas um serviço
docker-compose logs -f backend

# Ver logs das últimas 100 linhas
docker-compose logs --tail=100 backend
```


## ⚠️ Troubleshooting

### Problemas Comuns

#### 1. Porta já em uso
```bash
# Verificar o que está usando a porta
lsof -i :80    # Frontend
lsof -i :8080  # Backend  
lsof -i :5432  # PostgreSQL

# Parar processo se necessário
sudo kill -9 <PID>
```

#### 2. Erro de build no frontend
```bash
# Limpar cache do Docker
docker system prune -f

# Rebuild do frontend
docker-compose up --build frontend
```

#### 3. Banco não inicializa
```bash
# Remover volumes e recriar
docker-compose down -v
docker-compose up postgres -d
docker-compose logs postgres
```

#### 4. Backend não conecta no banco
```bash
# Verificar se o PostgreSQL está rodando
docker-compose ps postgres

# Ver logs do backend
docker-compose logs backend

# Testar conexão manual
docker-compose exec backend curl postgres:5432
```

### Limpeza Completa

Se tudo der errado, reset completo:

```bash
# Parar tudo
docker-compose down -v

# Limpar Docker
docker system prune -a --volumes

# Reconstruir tudo
docker-compose up --build -d
```

## 🛠️ Desenvolvimento

### Estrutura do Backend (Spring Boot)

- **Controller**: Endpoints da API REST
- **Service**: Lógica de negócio
- **Repository**: Acesso aos dados (JPA)
- **Model**: Entidades do banco de dados
- **Config**: Configurações (CORS, Security, etc.)

### Estrutura do Frontend (Angular)

- **Components**: Componentes da interface
- **Services**: Comunicação com a API
- **Models**: Interfaces TypeScript
- **Styles**: CSS/SCSS globais

### Variáveis de Ambiente

O sistema utiliza estas variáveis principais:

```yaml
# Backend
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/creditos_db
SPRING_DATASOURCE_USERNAME: postgres
SPRING_DATASOURCE_PASSWORD: 1234
SPRING_PROFILES_ACTIVE: docker

# PostgreSQL
POSTGRES_DB: creditos_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: 1234

```

## 📚 API Documentation

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/creditos/nfse/{numero}` | Buscar créditos por NFS-e |
| GET | `/api/creditos/{numero}` | Buscar crédito por número |
| GET | `/actuator/health` | Health check da aplicação |

### Exemplo de Resposta

```json
[
  {
    "id": 1,
    "numeroCredito": "123456",
    "numeroNfse": "7891011",
    "dataConstituicao": "2024-02-25",
    "valorIssqn": 1500.75,
    "tipoCredito": "ISSQN",
    "simplesNacional": true,
    "aliquota": 5.0,
    "valorFaturado": 30000.00,
    "valorDeducao": 5000.00,
    "baseCalculo": 25000.00
  }
]
```

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo LICENSE para mais detalhes.

---

**Desenvolvido usando Spring Boot + Angular + Docker**