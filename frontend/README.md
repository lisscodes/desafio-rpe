# Gestão de Clientes RPE

Bem-vindo ao projeto **Gestão de Clientes RPE**!  
Este sistema foi desenvolvido como parte do processo seletivo da RPE para a vaga de estagiário fullstack. Se trata de uma aplicação para simular a gestão de clientes, faturas e pagamentos em uma interface web. A aplicação oferece visualização de clientes cadastrados, consulta de faturas individuais e registro de pagamentos de forma interativa.

---

## Pré-requisitos

Este projeto utiliza as seguintes tecnologias:

- **Node.js** – Recomendado: versão 18.x ou superior  
- **npm** – Recomendado: versão 10.8.3 ou superior  
- **React** – versão 18.2.0  
- **TypeScript** – versão 5.4.2  
- **React Router DOM** – versão 6.23.0  
- **Tailwind CSS** – versão 3.4.1  

---

### Verificando versões

```bash
node -v
npm -v
````

Para atualizar o npm:

```bash
npm install -g npm@latest
```

Para verificar as dependências instaladas:

```bash
npm list react react-dom react-router-dom typescript
```

---

## Instruções de Build e Execução

### 1. Clone o repositório

```bash
git clone https://github.com/lisscodes/desafio-rpe.git
```

### 2. Acesse o diretório do projeto

```bash
cd frontend
```

### 3. Instale as dependências

```bash
npm install
```

---

## Ambiente de Desenvolvimento

Para iniciar a aplicação localmente em modo de desenvolvimento:

```bash
npm start
```

O projeto será executado em: [http://localhost:3000](http://localhost:3000)

---

## Build de Produção

Para gerar o build otimizado da aplicação:

```bash
npm run build
```

---

## Estrutura Principal do Projeto

```bash
src/
├── components/           # Componentes reutilizáveis (ClientsList, InvoiceList, Header)
├── pages/                # Páginas principais (HomePage, ClientsPanel, InvoicesPage)
├── App.tsx               # Definição central das rotas
└── index.tsx             # Ponto de entrada da aplicação
```
