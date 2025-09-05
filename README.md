# Notification System

Projeto de exemplo em Java (Spring Boot) que recebe notificações via HTTP, publica mensagens em uma fila RabbitMQ para processamento assíncrono e oferece um endpoint para consulta de status das mensagens.

<!-- Badges: build | tests | license (substitua os links pelos seus) -->

- Status: Em desenvolvimento
- Linguagem: Java 17+
- Framework: Spring Boot 3.x
- Mensageria: RabbitMQ (Spring AMQP)

## Visão geral

O serviço expõe dois endpoints HTTP principais:

- POST /api/notificar — aceita JSON com `mensagemId` e `conteudoMensagem`, publica um `ProcessMessage` na fila de entrada e retorna 202 Accepted.
- GET /api/status/{mensagemId} — retorna o status atual da mensagem (em memória).

Internamente, há um consumidor que processa mensagens da fila de entrada e publica um `StatusMessage` na fila de status. O armazenamento de status é em memória (ConcurrentHashMap) — adequado para testes e demonstração apenas.

## Tecnologias

- Java 17+
- Spring Boot 3.x
- Spring AMQP (RabbitMQ)
- Maven (mvnw wrapper)

## Requisitos

- JDK 17 ou superior (obrigatório)
- Maven (o projeto já inclui o wrapper `mvnw`)
- RabbitMQ acessível (CloudAMQP ou Docker local)

## Quickstart

1. Clone o repositório:

```bash
git clone https://github.com/Muchecha/notifications.git
cd notifications/notifications
```

2. Copie o arquivo de exemplo de configuração e preencha suas credenciais:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
# edite application.properties e preencha as credenciais do RabbitMQ
```

3. Execute a aplicação (assegure que `JAVA_HOME` aponta para JDK 17+):

```bash
./mvnw spring-boot:run
```

4. Teste com curl ou Postman (exemplos estão em `docs/API.md`):

Enviar notificação:

```bash
curl -X POST http://localhost:8081/api/notificar \
  -H 'Content-Type: application/json' \
  -d '{"mensagemId":"uuid-exemplo","conteudoMensagem":"teste"}'
```

Consultar status:

```bash
curl http://localhost:8081/api/status/uuid-exemplo
```

## Rodando um RabbitMQ local (opcional)

Existe um `docker/docker-compose.yml` para subir um RabbitMQ com painel de gerenciamento:

```bash
docker compose -f docker/docker-compose.yml up -d
```

Atualize `application.properties` para apontar para `localhost` se usar o container.

## Testes

Executar os testes unitários:

```bash
./mvnw test
```

Observação: se houver erro "release version 17 not supported", a JVM usada pelo Maven não é Java 17. Ajuste seu `JAVA_HOME` para um JDK 17+.

## Segurança e boas práticas

- Não comite `src/main/resources/application.properties` com credenciais reais. Use o arquivo `application.properties.example` como template.
- Se credenciais foram acidentalmente publicadas, rotacione-as imediatamente (CloudAMQP, etc.).
- Para produção, troque o armazenamento de status em memória por um armazenamento persistente (Redis, banco de dados) e proteja os endpoints com autenticação.

## Estrutura importante

- `src/main/java/.../config/RabbitMQConfig.java` — filas e conversor JSON
- `src/main/java/.../service/NotificationService.java` — envio, listeners e armazenamento de status
- `src/main/java/.../controller/NotificationController.java` — endpoints HTTP
- `src/main/resources/application.properties.example` — template de configuração
- `docker/docker-compose.yml` — compose para RabbitMQ local

## Contribuição

1. Abra issue descrevendo a sugestão.
2. Crie um branch com a feature/fix e abra um pull request.

## Licença

Escolha uma licença (por exemplo MIT) e adicione `LICENSE` se desejar abrir o projeto.

---
Arquivo gerado automaticamente — ajuste texto, badges e links conforme necessário antes de publicar no GitHub.

Feito com carrinho <3
