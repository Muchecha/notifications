package br.com.vrteste.notifications.service;


import br.com.vrteste.notifications.config.RabbitMQConfig;
import br.com.vrteste.notifications.dto.NotificationRequest;
import br.com.vrteste.notifications.dto.ProcessMessage;
import br.com.vrteste.notifications.dto.StatusMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    public void sendToProcessingQueue(NotificationRequest request) {
        ProcessMessage message = new ProcessMessage(
            request.getMensagemId(),
            request.getConteudoMensagem()
        );
        rabbitTemplate.convertAndSend(rabbitMQConfig.queueEntradaName, message);
        System.out.println("Mensagem enviada para fila de entrada: " + request.getMensagemId());
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueEntradaName}")
    public void processNotification(ProcessMessage message) {
        try {
            System.out.println("Iniciando processamento da mensagem: " + message.getMensagemId());
            System.out.println("Conteúdo: " + message.getConteudoMensagem());

            Thread.sleep(1500);

            StatusMessage statusMessage = new StatusMessage(
                message.getMensagemId(),
                "PROCESSADO_SUCESSO"
            );

            rabbitTemplate.convertAndSend(rabbitMQConfig.queueStatusName, statusMessage);
            System.out.println("Mensagem processada com sucesso: " + message.getMensagemId());

        } catch (InterruptedException e) {
            System.err.println("Processamento interrompido: " + e.getMessage());

            StatusMessage statusMessage = new StatusMessage(
                message.getMensagemId(),
                "FALHA_PROCESSAMENTO"
            );

            rabbitTemplate.convertAndSend(rabbitMQConfig.queueStatusName, statusMessage);

        } catch (Exception e) {
            System.err.println("Erro durante processamento: " + e.getMessage());

            StatusMessage statusMessage = new StatusMessage(
                message.getMensagemId(),
                "FALHA_PROCESSAMENTO"
            );

            rabbitTemplate.convertAndSend(rabbitMQConfig.queueStatusName, statusMessage);
        }
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueStatusName}")
    public void handleStatusUpdate(StatusMessage statusMessage) {
        System.out.println("=== STATUS UPDATE ===");
        System.out.println("Mensagem ID: " + statusMessage.getMensagemId());
        System.out.println("Status: " + statusMessage.getStatus());
        System.out.println("Timestamp: " + statusMessage.getTimestamp());
        System.out.println("====================");

        // Aqui você pode implementar lógica adicional como:
        // - Salvar no banco de dados
        // - Notificar outros sistemas
        // - Enviar emails ou notificações push
        // - Atualizar cache/redis
    }
}