package br.com.vrteste.notifications.controller;

import br.com.vrteste.notifications.dto.NotificationRequest;
import br.com.vrteste.notifications.dto.NotificationResponse;
import br.com.vrteste.notifications.service.NotificationService;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/notificar")
    public ResponseEntity<NotificationResponse> notificar(@Valid @RequestBody NotificationRequest request) {
        try {
            System.out.println("Recebida requisição para processar notificação: " + request.getMensagemId());
            
            notificationService.sendToProcessingQueue(request);
            
            NotificationResponse response = new NotificationResponse(
                request.getMensagemId(),
                "ACCEPTED",
                "Requisição recebida e será processada assincronamente"
            );
            
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar notificação: " + e.getMessage());
            
            NotificationResponse errorResponse = new NotificationResponse(
                request.getMensagemId(),
                "ERROR",
                "Erro interno do servidor: " + e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Sistema de Notificações funcionando!");
    }
}