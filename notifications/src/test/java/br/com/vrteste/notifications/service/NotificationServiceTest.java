package br.com.vrteste.notifications.service;

import br.com.vrteste.notifications.config.RabbitMQConfig;
import br.com.vrteste.notifications.dto.NotificationRequest;
import br.com.vrteste.notifications.dto.ProcessMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private NotificationService service;
    private RabbitTemplate rabbitTemplate;
    private RabbitMQConfig rabbitMQConfig;

    @BeforeEach
    public void setup() {
        service = new NotificationService();
        rabbitTemplate = mock(RabbitTemplate.class);

        rabbitMQConfig = new RabbitMQConfig();
        ReflectionTestUtils.setField(rabbitMQConfig, "queueEntradaName", "fila.test.entrada");
        ReflectionTestUtils.setField(rabbitMQConfig, "queueStatusName", "fila.test.status");

        ReflectionTestUtils.setField(service, "rabbitTemplate", rabbitTemplate);
        ReflectionTestUtils.setField(service, "rabbitMQConfig", rabbitMQConfig);
    }

    @Test
    public void sendToProcessingQueue_publishesProcessMessage() {
        NotificationRequest req = new NotificationRequest();
        req.setMensagemId("12345-uuid");
        req.setConteudoMensagem("conteudo teste");

        service.sendToProcessingQueue(req);

        ArgumentCaptor<ProcessMessage> captor = ArgumentCaptor.forClass(ProcessMessage.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("fila.test.entrada"), captor.capture());

        ProcessMessage sent = captor.getValue();
        assertNotNull(sent);
        assertEquals("12345-uuid", sent.getMensagemId());
        assertEquals("conteudo teste", sent.getConteudoMensagem());
    }

}
