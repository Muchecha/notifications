# Notification System

Projeto de exemplo que expõe uma API HTTP para aceitar notificações, publica mensagens em uma fila RabbitMQ para processamento assíncrono e fornece um endpoint para consultar o status da mensagem.

## Resumo

- Linguagem: Java (Spring Boot)
- Comunicação assíncrona: RabbitMQ (Spring AMQP)
- Endpoints HTTP para envio e consulta de status
- Armazenamento de status: memória (ConcurrentHashMap) — adequado para testes/demonstração

## Requisitos

- JDK 17 ou superior (recomendado). Spring Boot 3.x requer Java 17+.
- Maven (o projeto fornece o wrapper `mvnw`).
- Broker RabbitMQ (pode usar CloudAMQP, ou subir local via Docker Compose em `./docker/docker-compose.yml`).

## Configuração

As configurações principais estão em `src/main/resources/application.properties`.
Principais propriedades:

- `spring.rabbitmq.addresses` — host(s) do RabbitMQ (ex.: CloudAMQP URI)
- `spring.rabbitmq.username` / `spring.rabbitmq.password` — credenciais
- `app.queue.entrada` — nome da fila de entrada (ex.: `fila.notificacao.entrada.seu-nome`)
- `app.queue.status` — nome da fila de status (ex.: `fila.notificacao.status.seu-nome`)

Substitua as credenciais/URIs por valores do seu ambiente antes de rodar em produção.

## Como executar (local)

1. Garanta que o JDK 17+ está instalado e `JAVA_HOME` aponta para ele.
2. No diretório do projeto (onde está o `mvnw`), execute:

```bash
./mvnw spring-boot:run
```

3. A aplicação por padrão inicia em `http://localhost:8081` (ver `application.properties` para a porta configurada).

## Como executar com Docker (RabbitMQ local)

Se preferir rodar um RabbitMQ localmente via Docker Compose, há um arquivo em `docker/docker-compose.yml`.

1. Inicie o compose:

```bash
docker compose -f docker/docker-compose.yml up -d
```

2. Ajuste `application.properties` para apontar para `localhost` ou para as credenciais do container e execute a aplicação como acima.

## Testes

Executar os testes unitários:

```bash
./mvnw test
```

Nota: se receber erros relacionados a "class file version" ou compilação, certifique-se de usar JDK 17+ (muitos artefatos do Spring Boot 3.x são compilados para Java 17).

## Como testar com Postman ou curl

1) Enviar notificação (POST)

POST http://localhost:8081/api/notificar

Exemplo de payload:

```json
{
	"mensagemId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
	"conteudoMensagem": "Mensagem de teste"
}
```

Resposta esperada: HTTP 202 com um corpo contendo `mensagemId`.

2) Consultar status (GET)

GET http://localhost:8081/api/status/{mensagemId}

Resposta esperada: texto com um dos status:

- `RECEBIDO` — mensagem recebida e publicada na fila de entrada
- `PROCESSADO_SUCESSO` — processamento concluído com sucesso
- `FALHA_PROCESSAMENTO` — ocorreu erro no processamento
- `NÃO_ENCONTRADO` — mensagemId desconhecido

## Observações e limitações

- O armazenamento de status é em memória (não persistente). Se a aplicação reiniciar, todas as entradas de status são perdidas. Para produção, troque para um armazenamento persistente (Redis, banco de dados, etc.).
- Há um consumidor que simula processamento com atraso (1–2s). Ele publica um `StatusMessage` na fila de status após processar.
- A aplicação inclui um *startup health check* que tenta abrir uma conexão com o RabbitMQ e loga o resultado — ver `RabbitHealthChecker`.

## Estrutura de arquivos relevante

- `src/main/java/.../config/RabbitMQConfig.java` — configuração de filas, conversor JSON e RabbitTemplate
- `src/main/java/.../service/NotificationService.java` — lógica de envio, listeners e mapa de status
- `src/main/java/.../controller/NotificationController.java` — endpoints HTTP
- `src/main/resources/application.properties` — variáveis de conexão e nomes de fila

## Próximos passos recomendados

- Trocar o armazenamento de status para persistente
- Adicionar testes de integração que rodem contra um RabbitMQ de testes (p. ex. Testcontainers)
- Proteger endpoints com autenticação/autorizaçã0 quando for necessário

---
Gerado automaticamente — ajuste a documentação conforme regras/nomes de filas e ambiente.
```
