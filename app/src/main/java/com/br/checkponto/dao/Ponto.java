package com.br.checkponto.dao;

import java.io.Serializable;

/**
 * Created by ferna on 20/09/2015.
 */
public class Ponto implements Serializable {
    private Long idPonto;
    private String ponto;
    private String tarefa;

    public String getTarefa() {
        return tarefa;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public Long getIdPonto() {
        return idPonto;
    }

    public void setIdPonto(Long idPonto) {
        this.idPonto = idPonto;
    }

    public String getPonto() {
        return ponto;
    }

    public void setPonto(String ponto) {
        this.ponto = ponto;
    }

    @Override
    public String toString() {
        return ponto;
    }
}
