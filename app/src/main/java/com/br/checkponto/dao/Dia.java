package com.br.checkponto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferna on 06/09/2015.
 */
public class Dia implements Serializable{
    private Long id;
    private String dia;
    private String mes;
    private String ano;
    private String totalHorasDia;
    private String saldoHorasDia;
    private String diaNoFormatoDate;

    public String getDiaNoFormatoDate() {
        return diaNoFormatoDate;
    }

    public void setDiaNoFormatoDate(String diaNoFormatoDate) {
        this.diaNoFormatoDate = diaNoFormatoDate;
    }

    public String getTotalHorasDia() {
        return totalHorasDia;
    }

    public void setTotalHorasDia(String totalHorasDia) {
        this.totalHorasDia = totalHorasDia;
    }

    public String getSaldoHorasDia() {
        return saldoHorasDia;
    }

    public void setSaldoHorasDia(String saldoHorasDia) {
        this.saldoHorasDia = saldoHorasDia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return dia;
    }


}
