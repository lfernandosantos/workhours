package com.br.checkponto.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ferna on 02/11/2015.
 */
public class Periodo implements Serializable{
    private Integer id;
    private String periodoInicial;
    private String periodoFinal;
    private String pInicialDate;
    private String pFinalDate;

    public String getpInicialDate() {
        return pInicialDate;
    }

    public void setpInicialDate(String pInicialDate) {
        this.pInicialDate = pInicialDate;
    }

    public String getpFinalDate() {
        return pFinalDate;
    }

    public void setpFinalDate(String pFinalDate) {
        this.pFinalDate = pFinalDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriodoInicial() {
        return periodoInicial;
    }

    public void setPeriodoInicial(String periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public String getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoFinal(String periodoFinal) {
        this.periodoFinal = periodoFinal;
    }
}
