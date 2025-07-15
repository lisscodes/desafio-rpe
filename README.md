# Gestão de Clientes RPE – Fullstack

Projeto desenvolvido como parte do processo seletivo da RPE para a vaga de **Estágio Fullstack**. A aplicação simula o gerenciamento de clientes, faturas e pagamentos, oferecendo uma **interface web com React** e uma **API RESTful em Spring Boot**.

---

## Tecnologias Utilizadas

### Backend

* Java 21
* Spring Boot 3.5.3
* Spring Data JPA + Hibernate Validator
* PostgreSQL (via Docker)
* Swagger/OpenAPI para documentação
* JUnit + Mockito para testes
* JaCoCo para cobertura de testes

### Frontend

* React 18
* TypeScript
* Tailwind CSS
* React Router DOM
* Axios

---

## Como Executar com Docker

Para executar toda a aplicação com banco, backend e frontend:

### 1. Clone o repositório

```bash
git clone https://github.com/lisscodes/desafio-rpe.git
cd desafio-rpe
```

### 2. Execute via Docker Compose

```bash
docker-compose up --build
```

Isso irá:

* Subir o banco PostgreSQL na porta `5431`
* Subir o backend Java na porta `8080`
* Subir o frontend React na porta `3000`

---

## Regras de Negócio e Status das Faturas

O sistema trabalha com **três status** principais para faturas:

| Código | Descrição        |
| ------ | ---------------- |
| **B**  | Fatura em aberto |
| **A**  | Fatura atrasada  |
| **P**  | Fatura paga      |

**Lógica aplicada:**

* Faturas com mais de 3 dias de atraso marcam o cliente como **bloqueado**
* Clientes bloqueados têm o **limite de crédito ajustado para R\$ 0,00**
* Pagamentos atualizam a fatura para o status **Paga** e registram a data atual

---


## Testes e Cobertura

No backend, os testes podem ser executados com:

```bash
./mvnw test
```

O relatório de cobertura gerado com JaCoCo estará disponível em:

```
backend/target/site/jacoco/index.html
```

---

## Documentação da API

Ao executar o backend, a documentação Swagger estará disponível em:

📎 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
