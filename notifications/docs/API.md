# API

Referência dos endpoints HTTP e contratos de mensagem usados pela aplicação.

Base URL (local): http://localhost:8081

## Endpoints HTTP

### 1) Enviar notificação

- Método: POST
- Path: /api/notificar
- Descrição: Recebe um payload com `mensagemId` (UUID) e `conteudoMensagem` (texto). A API publica uma `ProcessMessage` na fila de entrada e retorna 202 Accepted com o mesmo `mensagemId`.

Request JSON:

```json
{
	"mensagemId": "<uuid>",
	"conteudoMensagem": "<texto>"
}
```

Response (HTTP 202):

```json
{
	"mensagemId": "<uuid>"
}
```

Erros possíveis:

- 400 Bad Request — payload inválido (campos ausentes ou formato errado)

### 2) Consultar status

- Método: GET
- Path: /api/status/{mensagemId}
- Descrição: Retorna o status atual associado ao `mensagemId`. Se a mensagem não for conhecida, retorna `NÃO_ENCONTRADO`.

Response (HTTP 200):

```
RECEBIDO
```

Status possíveis (retornados como string simples):

- `RECEBIDO` — message publicado na fila de entrada
- `PROCESSADO_SUCESSO` — processamento finalizado com sucesso
- `FALHA_PROCESSAMENTO` — erro durante processamento
- `NÃO_ENCONTRADO` — mensagemId desconhecido

## Contratos de mensagens (RabbitMQ)

As filas usam objetos JSON serializados.

1) ProcessMessage — publicado na fila de entrada (`app.queue.entrada`)

Exemplo:

```json
{
	"mensagemId": "<uuid>",
	"conteudoMensagem": "<texto>"
}
```

2) StatusMessage — publicado na fila de status (`app.queue.status`) após processamento

Exemplo:

```json
{
	"mensagemId": "<uuid>",
	"status": "PROCESSADO_SUCESSO"
}
```

## Observações

- As filas são configuradas em `application.properties` via `app.queue.entrada` e `app.queue.status`.
- O consumidor de `ProcessMessage` simula um trabalho síncrono e publica um `StatusMessage` quando termina.
- Se desejar integrar com outro serviço, consuma as mensagens `StatusMessage` da fila de status.

---
Documentação gerada automaticamente — atualize conforme evolução do contrato.
