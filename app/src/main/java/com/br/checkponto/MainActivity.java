package com.br.checkponto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.br.checkponto.dao.Dia;
import com.br.checkponto.dao.DiaDAO;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private ImageView imagem;
    private Intent aposSplash;
    private Integer novoDia;
    private Dia diaSelecionado;
    private Integer verificaLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagem = (ImageView) findViewById(R.id.imageView);
        Glide.with(MainActivity.this).load(R.drawable.ajax_loader2).into(imagem);

        SimpleDateFormat converterdata = new SimpleDateFormat("yyyy-MM-dd");

        Calendar data = Calendar.getInstance();
        Calendar ultimaData = Calendar.getInstance();

        List<Dia> dia = new ArrayList<>();
        DiaDAO dao = new DiaDAO(this);
        dia = dao.getListaDias();

        if (dia.size()==1){
            try {
                ultimaData.setTime(converterdata.parse(dia.get(0).getDiaNoFormatoDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int diaInt = ultimaData.getTime().getDay();
            int diaAtualInt = data.getTime().getDay();

            int mesInt = ultimaData.getTime().getMonth();
            int mesAtualInt = data.getTime().getMonth();

            int anoInt = ultimaData.getTime().getYear();
            int anoAtual = data.getTime().getYear();

            if (diaInt==diaAtualInt && mesInt==mesAtualInt && anoInt==anoAtual){

                diaSelecionado = dia.get(0);
                //dia ja cadastrado e não foi fechado, cadastra novo ponto automatico
                if (diaSelecionado.getSaldoHorasDia() != null ||diaSelecionado.getSaldoHorasDia() != "00:00:00" ) {

                    verificaLista = 1;
                    aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
                    aposSplash.putExtra("verificaLista", verificaLista);
                    aposSplash.putExtra("diaSelecionado", diaSelecionado);

                }else {
                    verificaLista = 0;
                    aposSplash.putExtra("verificaLista", verificaLista);
                    aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
                }

            }else {
                //perguntar se deseja cadastrar novo dia e marcar ponto
                verificaLista = 2;
                aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
                aposSplash.putExtra("verificaLista", verificaLista);

            }

        }else
        if (dia.size()>1){

            try {
                ultimaData.setTime(converterdata.parse(dia.get(dia.size()-1).getDiaNoFormatoDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int diaInt = ultimaData.getTime().getDay();
            int diaAtualInt = data.getTime().getDay();

            int mesInt = ultimaData.getTime().getMonth();
            int mesAtualInt = data.getTime().getMonth();

            int anoInt = ultimaData.getTime().getYear();
            int anoAtual = data.getTime().getYear();

            if (diaInt==diaAtualInt && mesInt==mesAtualInt && anoInt==anoAtual){

                diaSelecionado = dia.get(dia.size()-1);
                if (diaSelecionado.getSaldoHorasDia() != null ||diaSelecionado.getSaldoHorasDia() != "00:00:00" ) {

                    verificaLista = 1;
                    aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
                    aposSplash.putExtra("verificaLista", verificaLista);
                    aposSplash.putExtra("diaSelecionado", diaSelecionado);
                }else {
                    verificaLista = 0;
                    aposSplash.putExtra("verificaLista", verificaLista);
                    aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
                }

            }else {
                //perguntar se deseja cadastrar novo dia e marcar ponto
                verificaLista = 2;
                aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
                aposSplash.putExtra("verificaLista", verificaLista);

            }
        }
        else {




            verificaLista = 2;
            aposSplash = new Intent(MainActivity.this, ListaDiasActivity.class);
            aposSplash.putExtra("verificaLista", verificaLista);

        }
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    startActivity(aposSplash);
                }
            }
        };
        timerThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
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
}
