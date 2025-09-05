package br.com.vrteste.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationResponse {
    
    @JsonProperty("mensagemId")
    private String mensagemId;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    // Construtores
    public NotificationResponse() {}
    
    public NotificationResponse(String mensagemId, String status, String message) {
        this.mensagemId = mensagemId;
        this.status = status;
        this.message = message;
    }
    
    // Getters e Setters
    public String getMensagemId() {
        return mensagemId;
    }
    
    public void setMensagemId(String mensagemId) {
        this.mensagemId = mensagemId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}