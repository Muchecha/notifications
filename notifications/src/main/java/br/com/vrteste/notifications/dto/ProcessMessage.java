package br.com.vrteste.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessMessage {
    
    @JsonProperty("mensagemId")
    private String mensagemId;
    
    @JsonProperty("conteudoMensagem")
    private String conteudoMensagem;
    
    // Construtores
    public ProcessMessage() {}
    
    public ProcessMessage(String mensagemId, String conteudoMensagem) {
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