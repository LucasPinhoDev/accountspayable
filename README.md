
# Accounts Payable API

Esta é uma API REST desenvolvida para gerenciar contas a pagar. A aplicação é construída usando Java 17, Spring Boot, PostgreSQL e Docker, e segue os princípios de Domain-Driven Design (DDD).

---

## Resumo do Projeto

O desafio consistiu em desenvolver uma API REST para gerenciar um sistema de contas a pagar com requisitos específicos de implementação e melhores práticas de desenvolvimento em Java. A API permite a realização de operações CRUD, atualização de status de contas, cálculo de valores pagos, importação de contas via CSV e autenticação de usuários, tudo isso com suporte a paginação e integração com o banco de dados PostgreSQL em um ambiente Dockerizado.

---

## Funcionalidades

- **CRUD de Contas a Pagar**: Criação, leitura, atualização e exclusão de contas.
- **Alteração de Status**: Possibilidade de alterar o status de uma conta para "A Vencer", "Vencida" ou "Paga".
- **Consulta Filtrada**: Buscar contas por data de vencimento e descrição.
- **Cálculo de Total Pago**: Obter o valor total pago em um período específico.
- **Importação de Contas via CSV**: Importar contas em massa a partir de um arquivo CSV.
- **Documentação da API**: Documentação interativa com OpenAPI/Swagger.

---

## Arquitetura: Domain-Driven Design (DDD)

### Por que escolhemos DDD?

- **Organização e Clareza**: DDD nos permite organizar o projeto de forma que a lógica de negócios (domínio) seja separada das camadas de aplicação e infraestrutura. Isso torna o código mais limpo, fácil de manter e compreensível.
- **Foco no Domínio**: DDD ajuda a garantir que a lógica de negócios seja o centro do design, facilitando a colaboração com especialistas no domínio.
- **Escalabilidade e Flexibilidade**: Com DDD, é mais fácil adicionar novas funcionalidades sem introduzir complexidade desnecessária ou desorganizar o código.
- **Separação de Responsabilidades**: DDD promove a separação clara entre entidades de domínio, serviços de aplicação e infraestrutura, o que é essencial em projetos corporativos de médio e grande porte.

---

## Estrutura do Projeto

```
src
└── main
    ├── java
    │   └── com.example.accountspayable
    │       ├── application             // Lógica de negócio (serviços, casos de uso)
    │       ├── domain                  // Entidades, enums e interfaces de repositórios
    │       ├── infrastructure          // Configurações, persistência e importadores
    │       └── presentation            // Controladores REST
    └── resources
        ├── db/migration                // Migrações do Flyway
        ├── application.yml             // Configurações da aplicação
        └── Dockerfile                  // Configuração do Docker
```

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Flyway**: Para controle de migrações do banco de dados.
- **OpenAPI/Swagger**: Para documentação da API.
- **Lombok**: Para reduzir o código boilerplate.

---

## Requisitos

- **Docker** e **Docker Compose** instalados na sua máquina.
- **Java 17** (se desejar rodar a aplicação sem Docker).
- **Gradle** (caso queira construir o projeto manualmente).

---

## Como Executar a Aplicação

### 1. Clonar o Repositório

```bash
git clone https://github.com/LucasPinhoDev/accountspayable.git
cd accountspayable
```

### 2. Configuração com Docker

Certifique-se de que o Docker esteja rodando na sua máquina.

### 2.1 Construir e Rodar os Containers

```bash
docker-compose up --build
```

> **Explicação**: Este comando irá construir a imagem da aplicação e iniciar os containers do PostgreSQL e da aplicação.

### 3. Acessar a Aplicação

- **API**: Acesse `http://localhost:8080` para testar a API.
- **Swagger/OpenAPI**: Acesse `http://localhost:8080/swagger-ui.html` para a documentação interativa.

---

## Variáveis de Ambiente

A configuração do banco de dados e outras variáveis importantes estão definidas no `docker-compose.yml`. Aqui estão algumas variáveis principais:

```yaml
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/accounts_db
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=admin
```

---

## Como Importar Contas via CSV

1. Use o endpoint `POST /api/accounts/import` no Postman.
2. Envie um arquivo CSV com as colunas: `dueDate`, `paymentDate`, `value`, `description`, `status`.

---

## Como Rodar os Testes

### Usando Gradle

```bash
./gradlew test
```

---

## Problemas Conhecidos

- **Aviso `spring.jpa.open-in-view`**: Você pode desativar isso se não precisar de consultas abertas durante a renderização de visualizações.
- **Erros de Conexão com o Banco de Dados**: Certifique-se de que o container do PostgreSQL esteja rodando corretamente.

---

## Melhorias Futuras

- Adicionar mais testes unitários e de integração.
- Implementar segurança avançada com JWT.
- Adicionar suporte a monitoramento e logging.

---

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.

---

## Autor

Lucas Pinho - [GitHub](https://github.com/LucasPinhoDev)
