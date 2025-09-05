package br.com.vrteste.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class StatusMessage {
    
    @JsonProperty("mensagemId")
    private String mensagemId;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    // Construtores
    public StatusMessage() {}
    
    public StatusMessage(String mensagemId, String status) {
        this.mensagemId = mensagemId;
        this.status = status;
        this.timestamp = LocalDateTime.now();
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
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}