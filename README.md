# AquaSOS API Containers 💧

## 👥 Integrantes

| Nome              | RM     |
|-------------------|--------|
| Gustavo Lopes     | 556859 |
| Renato David      | 555627 |
| Gabriel Jablonski | 555452 |

## Visão Geral

A água potável é um recurso essencial à vida, e sua escassez em momentos de crise representa um dos maiores desafios enfrentados por comunidades vulneráveis. Em situações de emergência — como enchentes, secas prolongadas, deslizamentos ou colapsos na infraestrutura urbana — o acesso à água potável pode ser interrompido por dias ou até semanas. Nesses cenários, falta não apenas o recurso em si, mas também informações organizadas e em tempo real sobre onde e como ele pode ser distribuído, dificultando a tomada de decisão por gestores, voluntários e pela própria população.

O **AquaSOS** surge como uma solução integrada, voltada principalmente a ambientes urbanos afetados por desastres naturais que causam interrupção temporária no fornecimento de água, além de ser adaptável a comunidades com acesso limitado e recorrente à água potável.

## 🎯 Objetivo

Organizar, monitorar e otimizar a distribuição de água potável em situações críticas, utilizando tecnologia de ponta para oferecer informações em tempo real, centralizar pedidos de distribuição e fornecer ferramentas práticas para gestores, voluntários e cidadãos.

## 🚀 Principais Funcionalidades

- **API Java (Este Projeto):** Responsável por organizar os pedidos de água, armazenar dados em banco relacional PostgreSQL, e servir como backend central da solução.
- **Aplicativo Mobile:** Desenvolvido em React Native, permite aos usuários solicitar água, acompanhar pedidos e receber notificações.
- **Painel de Operadores/Voluntários:** Permite acompanhamento do status dos pontos de distribuição e tomada de decisão baseada em dados atualizados.

## 🏗️ Arquitetura

A arquitetura do AquaSOS foi pensada para englobar diferentes perfis de usuários, fluxos de informação e infraestrutura disponível:

- **API Java:** Gerenciamento centralizado de demandas, integração com banco PostgreSQL, e comunicação com o front-end mobile e dashboards.
- **Aplicativo Mobile:** Interface acessível para solicitações e acompanhamento.
- **Painéis Operacionais:** Ferramentas para usuários necessitando água e voluntários.

## 🌱 Impacto Social

Mais do que uma resposta técnica, o AquaSOS busca garantir que a água chegue a quem mais precisa, de forma justa, organizada e rápida. Oferece ainda uma ferramenta estratégica para gestores públicos e voluntários, facilitando decisões baseadas em dados reais.

O projeto demonstra como a integração entre sensores IoT, protocolos de comunicação, dashboards interativos e aplicativos mobile pode transformar realidades, sendo uma solução replicável e adaptável para diferentes regiões e situações.

## 🛠️ Como Executar

1. **Pré-requisitos**
   - ☕ Java 11+
   - 🐳 Docker (opcional, para execução em containers)
   - 🗄️ Banco PostgreSQL disponível

2. **Clonando o Projeto**
   ```sh
   git clone https://github.com/GuLopes14/aqua-sos-api-containers.git
   ```

3. **Configuração**
   - Configure as variáveis de ambiente ou edite o arquivo `src/main/resources/application.properties` para a conexão com o banco PostgreSQL e demais integrações.
   - Exemplo de configuração recomendada:
     ```properties
     spring.application.name=aqua-api

     # URL de conexão com PostgreSQL (ajuste o nome do banco se necessário)
     spring.datasource.url=jdbc:postgresql://${HOST:localhost}:5432/aqua_sos
     spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
     spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:gus1411!}

     # DDL AUTO: 'create' apaga tudo a cada inicialização, 'update' mantém dados e atualiza estrutura
     spring.jpa.hibernate.ddl-auto=update

     # Mostra SQLs no console para debug
     spring.jpa.show-sql=true

     # Dialeto do PostgreSQL
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

     # Configuração do Swagger/OpenAPI
     springdoc.swagger-ui.path=/swagger-ui.html
     springdoc.api-docs.path=/v3/api-docs
     springdoc.api-docs.enabled=true
     springdoc.swagger-ui.enabled=true
     ```

   - **Observação:**  
     - Para desenvolvimento rápido, você pode usar `spring.jpa.hibernate.ddl-auto=create`, mas isso irá apagar todos os dados ao reiniciar a aplicação.  
     - Em produção, utilize sempre `update` ou `validate` para preservar os dados.

4. **Execução**
   - Via Maven: 
     ```sh
     ./mvnw spring-boot:run
     ```
   - Via Docker:  
     ```sh
     docker compose up --build -d
     ```
     - Para derrubar o container:
       ```sh
       docker compose down -v 
       ```

---

## 📋 Exemplos de Requisições JSON

### Criar Pedido de Água

**POST** `/pedidos-agua`

**Corpo da requisição:**
```json
{
  "usuarioId": 1,
  "quantidadeLitros": 50,
  "nivelUrgencia": "ALTA",
  "endereco": "Rua das Flores, 123, Bairro Centro, Cidade Exemplo"
}
```

**Resposta (201 Created):**
```json
{
  "id": 3,
  "usuarioId": 1,
  "quantidadeLitros": 50,
  "nivelUrgencia": "ALTA",
  "endereco": "Rua das Flores, 123, Bairro Centro, Cidade Exemplo"
}
```

---

### Buscar Pedido de Água por ID

**GET** `/pedidos-agua/{id}`

**Resposta:**
```json
{
  "id": 3,
  "usuarioId": 1,
  "quantidadeLitros": 50,
  "nivelUrgencia": "ALTA",
  "endereco": "Rua das Flores, 123, Bairro Centro, Cidade Exemplo"
}
```

---

### Atualizar Pedido de Água

**PUT** `/pedidos-agua/{id}`

**Corpo da requisição:**
```json
{
  "usuarioId": 1,
  "quantidadeLitros": 100,
  "nivelUrgencia": "MEDIA",
  "endereco": "Rua das Flores, 123, Bairro Centro, Cidade Exemplo"
}
```

**Resposta:**
```json
{
  "id": 3,
  "usuarioId": 1,
  "quantidadeLitros": 100,
  "nivelUrgencia": "MEDIA",
  "endereco": "Rua das Flores, 123, Bairro Centro, Cidade Exemplo"
}
```

---

### Listar Pedidos de Água

**GET** `/pedidos-agua`

**Parâmetros de filtro (query params):**
- `usuarioId` (Long)  
- `quantidadeLitros` (Integer)  
- `endereco` (String, busca parcial, case-insensitive)  
- `nivelUrgencia` (String, valores: `ALTA`, `MEDIA`, `BAIXA`)  
- Suporta paginação padrão Spring (`page`, `size`, `sort`)

**Exemplo de requisição:**  
`GET /pedidos-agua?usuarioId=1&nivelUrgencia=ALTA&page=0&size=10`

**Exemplo de resposta (paginada):**
```json
{
  "content": [
    {
      "id": 3,
      "usuarioId": 1,
      "quantidadeLitros": 50,
      "nivelUrgencia": "ALTA",
      "endereco": "Rua das Flores, 123, Bairro Centro, Cidade Exemplo"
    }
  ],
  "pageable": { /* ...paginação... */ },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": { /* ... */ },
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### Deletar Pedido de Água

**DELETE** `/pedidos-agua/{id}`

**Resposta:**  
`204 No Content` (sem corpo)

---

## 🔎 Filtros Avançados

O endpoint de listagem suporta filtros combinados via query string, conforme a implementação da [Specification](src/main/java/br/com/aquasos/specification/PedidoAguaSpecification.java):

- `usuarioId`: Exato
- `quantidadeLitros`: Exato
- `endereco`: Parcial (substring, case-insensitive)
- `nivelUrgencia`: Exato, valores aceitos: `ALTA`, `MEDIA`, `BAIXA`

**Exemplo:**
```
GET /pedidos-agua?usuarioId=1&quantidadeLitros=50&endereco=Centro&nivelUrgencia=ALTA
```

---