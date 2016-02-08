package com.br.checkponto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.br.checkponto.dao.Dia;
import com.br.checkponto.dao.DiaDAO;
import com.br.checkponto.dao.Periodo;
import com.br.checkponto.dao.PeriodoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NovoDiaActivity extends AppCompatActivity {

    private CalendarView diasCalendario;
    private int anoCalendario;
    private int mesCalendario;
    private int diaCalendario;
    private int pegaPeriodoSerializable;
    private String mesPeriodoAbreviado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_dia);

        pegaPeriodoSerializable = (int) getIntent().getSerializableExtra("periodo");
        if (pegaPeriodoSerializable == 1){
            setTitle("Dia Inicial");
        }else
        if (pegaPeriodoSerializable == 0){
            setTitle("Dia Final");
        }else {
            setTitle("Novo Dia");
        }
        diasCalendario = (CalendarView) findViewById(R.id.calendarView);

        diasCalendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                anoCalendario = year;
                mesCalendario = month;
                diaCalendario = dayOfMonth;
                mesPeriodoAbreviado = transformaMesEmStringAbreviado(month);

                salvarDia();

            }
        });

    }

    private void salvarDia() {
        if (pegaPeriodoSerializable == 1){
            Periodo pIni = new Periodo();
            PeriodoDAO pdao = new PeriodoDAO(NovoDiaActivity.this);

            List<Periodo> periodos = new ArrayList<Periodo>();
            periodos = pdao.getPeriodo();
            pIni.setPeriodoInicial(String.valueOf(diaCalendario) + "-" + mesPeriodoAbreviado + "-" + String.valueOf(anoCalendario));
            pIni.setpInicialDate(String.valueOf(anoCalendario) + "-" + String.valueOf(mesCalendario+1)+ "-" + String.valueOf(diaCalendario));
            if (periodos.size()==0){

                pdao.inserirPeriodo(pIni, 1);
                pdao.close();

            }else {
                periodos.get(0).setPeriodoInicial(pIni.getPeriodoInicial());
                periodos.get(0).setpInicialDate(pIni.getpInicialDate());
                pdao.alterarPeriodo(periodos.get(0), 1);
                pdao.close();
            }

            finish();
        }else
        if (pegaPeriodoSerializable == 0){
            Periodo pFim = new Periodo();
            PeriodoDAO pdao = new PeriodoDAO(NovoDiaActivity.this);

            List<Periodo> periodos = new ArrayList<Periodo>();
            periodos = pdao.getPeriodo();
            pFim.setPeriodoFinal(diaCalendario + "-" + mesPeriodoAbreviado + "-" + String.valueOf(anoCalendario));
            pFim.setpFinalDate(String.valueOf(anoCalendario) + "-" + String.valueOf(mesCalendario+1)+ "-" + String.valueOf(diaCalendario));
            if (periodos.size()==0){
                pdao.inserirPeriodo(pFim, 2);
                pdao.close();
            }else {
                periodos.get(0).setPeriodoFinal(pFim.getPeriodoFinal());
                periodos.get(0).setpFinalDate(pFim.getpFinalDate());
                pdao.alterarPeriodo(periodos.get(0),2);
                pdao.close();
            }
            finish();
        }else {
            DiaDAO dao = new DiaDAO(NovoDiaActivity.this);
            Dia novoDia = new Dia();
            novoDia.setDia(pegaDiaFormatado(diaCalendario));
            novoDia.setMes(transformaMesEmString(mesCalendario));
            novoDia.setAno(String.valueOf(anoCalendario));
            novoDia.setDiaNoFormatoDate( String.valueOf(anoCalendario)+ "-" + String.valueOf(mesCalendario+1)+ "-" + String.valueOf(diaCalendario));
            novoDia.setSaldoHorasDia("00:00:00");
            novoDia.setTotalHorasDia("00:00:00");
            dao.insere(novoDia);
            dao.close();
            finish();
        }
    }

    private String pegaDiaFormatado(int dayOfMonth) {
        String diaFormatado = new String();

        if (dayOfMonth <= 9){
            diaFormatado = "0"+String.valueOf(dayOfMonth);
        }else {
            diaFormatado = String.valueOf(dayOfMonth);
        }

        return diaFormatado;
    }

    private String transformaMesEmString(int month) {
        String nomeMes = new String();
        if (month == 0){
            nomeMes = "JANEIRO";
        }else
            if (month == 1){
                nomeMes = "FEVEREIRO";
            }
                if (month == 2){
                nomeMes = "MARÇO";
                }else
                     if (month == 3){
                        nomeMes = "ABRIL";
                        }else
                            if (month == 4){
                                nomeMes = "MAIO";
                            }else
                                if (month == 5){
                                    nomeMes = "JUNHO";
                                }else
                                    if (month == 6){
                                         nomeMes = "JULHO";
                                    }else
                                        if (month == 7){
                                            nomeMes = "AGOSTO";
                                        }else
                                            if (month == 8){
                                                nomeMes = "SETEMBRO";
                                            }else
                                                if (month == 9){
                                                    nomeMes = "OUTUBRO";
                                                }else
                                                    if (month == 10){
                                                        nomeMes = "NOVEMBRO";
                                                    }else
                                                        if (month == 11){
                                                            nomeMes = "DEZEMBRO";
                                                        }

        return nomeMes;
    }

    private String transformaMesEmStringAbreviado(int month) {
        String nomeMes = new String();
        if (month == 0){
            nomeMes = "JAN";
        }else
        if (month == 1){
            nomeMes = "FEV";
        }
        if (month == 2){
            nomeMes = "MAR";
        }else
        if (month == 3){
            nomeMes = "ABR";
        }else
        if (month == 4){
            nomeMes = "MAI";
        }else
        if (month == 5){
            nomeMes = "JUN";
        }else
        if (month == 6){
            nomeMes = "JUL";
        }else
        if (month == 7){
            nomeMes = "AGO";
        }else
        if (month == 8){
            nomeMes = "SET";
        }else
        if (month == 9){
            nomeMes = "OUT";
        }else
        if (month == 10){
            nomeMes = "NOV";
        }else
        if (month == 11){
            nomeMes = "DEZ";
        }

        return nomeMes;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_novo_dia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return false;
        }

        return super.onOptionsItemSelected(item);
    }

}
