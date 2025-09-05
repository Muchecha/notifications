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
    
    /**
     * Envia mensagem para a fila de entrada para processamento
     */
    public void sendToProcessingQueue(NotificationRequest request) {
        ProcessMessage message = new ProcessMessage(
            request.getMensagemId(),
            request.getConteudoMensagem()
        );
        
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_ENTRADA, message);
        System.out.println("Mensagem enviada para fila de entrada: " + request.getMensagemId());
    }
    
    /**
     * Consumidor da fila de entrada - processa as mensagens
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ENTRADA)
    public void processNotification(ProcessMessage message) {
        try {
            System.out.println("Iniciando processamento da mensagem: " + message.getMensagemId());
            System.out.println("Conteúdo: " + message.getConteudoMensagem());
            
            // Simula processamento (1-2 segundos)
            Thread.sleep(1500);
            
            // Envia status de sucesso
            StatusMessage statusMessage = new StatusMessage(
                message.getMensagemId(),
                "PROCESSADO_SUCESSO"
            );
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_STATUS, statusMessage);
            System.out.println("Mensagem processada com sucesso: " + message.getMensagemId());
            
        } catch (InterruptedException e) {
            System.err.println("Processamento interrompido: " + e.getMessage());
            
            // Envia status de falha
            StatusMessage statusMessage = new StatusMessage(
                message.getMensagemId(),
                "FALHA_PROCESSAMENTO"
            );
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_STATUS, statusMessage);
            
        } catch (Exception e) {
            System.err.println("Erro durante processamento: " + e.getMessage());
            
            // Envia status de falha
            StatusMessage statusMessage = new StatusMessage(
                message.getMensagemId(),
                "FALHA_PROCESSAMENTO"
            );
            
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_STATUS, statusMessage);
        }
    }
    
    /**
     * Consumidor da fila de status - monitora os resultados
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_STATUS)
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