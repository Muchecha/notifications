package br.com.vrteste.notifications.dto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationRequest {
    
    @NotBlank(message = "mensagemId é obrigatório")
    @JsonProperty("mensagemId")
    private String mensagemId;
    
    @NotBlank(message = "conteudoMensagem é obrigatório")
    @JsonProperty("conteudoMensagem")
    private String conteudoMensagem;
    
    // Construtores
    public NotificationRequest() {}
    
    public NotificationRequest(String mensagemId, String conteudoMensagem) {
        this.mensagemId = mensagemId;
        this.conteudoMensagem = conteudoMensagem;
    }
    
    // Getters e Setters
    public String getMensagemId() {
        return mensagemId;
    }
    
    public void setMensagemId(String mensagemId) {
        this.mensagemId = mensagemId;
    }
    
    public String getConteudoMensagem() {
        return conteudoMensagem;
    }
    
    public void setConteudoMensagem(String conteudoMensagem) {
        this.conteudoMensagem = conteudoMensagem;
    }
}