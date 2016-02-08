package com.br.checkponto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.br.checkponto.adapter.AdapterListaDias;
import com.br.checkponto.dao.Dia;
import com.br.checkponto.dao.DiaDAO;
import com.br.checkponto.dao.Periodo;
import com.br.checkponto.dao.PeriodoDAO;
import com.br.checkponto.dao.Ponto;
import com.br.checkponto.dao.PontoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListaDiasActivity extends AppCompatActivity {

    private ListView listaDias;
    private Button btnADCDias;
    private List<Dia> dias;
    private List<Periodo> periodos;
    private int serializableDiaPeriodo;
    private EditText periodoDiaIni;
    private EditText periodoDiaFim;
    private EditText edtSaldoMes;
    private int sinalDoSaldo;
    private Integer novoDia;
    private Integer verificaALista;
    private Dia diaSelecionado;
   // private String pegaData;
   // private int pegaTipoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dias);

        findViews();

        periodoDiaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serializableDiaPeriodo = 1;
                Intent intentPeriodoIni = new Intent(ListaDiasActivity.this, NovoDiaActivity.class);
                intentPeriodoIni.putExtra("periodo", serializableDiaPeriodo);
                startActivity(intentPeriodoIni);
            }
        });


        periodoDiaFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serializableDiaPeriodo = 0;
                Intent intentPeriodoFim = new Intent(ListaDiasActivity.this, NovoDiaActivity.class);
                intentPeriodoFim.putExtra("periodo", serializableDiaPeriodo);
                startActivity(intentPeriodoFim);
            }
        });

        verificaLista();


        registerForContextMenu(listaDias);

        btnADCDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentADC = new Intent(ListaDiasActivity.this, NovoDiaActivity.class);
                serializableDiaPeriodo = 3;
                intentADC.putExtra("periodo", serializableDiaPeriodo);
                startActivity(intentADC);
            }
        });

        listaDias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dia diaSelecionado = (Dia) parent.getAdapter().getItem(position);
                Intent irPontos = new Intent(ListaDiasActivity.this, ListaPontosActivity.class);
                novoDia = 0;
                irPontos.putExtra("diaSelecionado", diaSelecionado);
                irPontos.putExtra("novoDia", novoDia);
                startActivity(irPontos);
            }
        });



    }

    private void verificaLista() {
        diaSelecionado = (Dia) getIntent().getSerializableExtra("diaSelecionado");

        verificaALista = (Integer) getIntent().getSerializableExtra("verificaLista");
        if (verificaALista==1){
            //dia cadastrado incluir ponto
            novoDia = 1;
            Intent irPontos = new Intent(ListaDiasActivity.this, ListaPontosActivity.class);
            irPontos.putExtra("novoDia", novoDia);
            irPontos.putExtra("diaSelecionado",diaSelecionado);
            startActivity(irPontos);
        }else
            if (verificaALista==2){
                //cadastra novo dia e novo ponto
                new AlertDialog.Builder(this).setTitle("Novo Dia").setMessage("O dia de hoje ainda não foi cadastrado." +
                        "Deseja incluir o dia e registar o ponto?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new Date();
                        SimpleDateFormat converteDia = new SimpleDateFormat("dd");
                        SimpleDateFormat converteMes = new SimpleDateFormat("MM");
                        SimpleDateFormat converteAno = new SimpleDateFormat("yyyy");
                        String stringDia = converteDia.format(date);
                        String stringMes = converteMes.format(date);
                        String stringAno= converteAno.format(date);

                        DiaDAO dao = new DiaDAO(ListaDiasActivity.this);

                        Dia nDia = new Dia();
                        nDia.setDia(stringDia);
                        nDia.setMes(transformaMesEmString(Integer.valueOf(stringMes)));
                        nDia.setAno(stringAno);
                        nDia.setDiaNoFormatoDate(stringAno + "-" + stringMes+"-"+stringDia);
                        nDia.setSaldoHorasDia("00:00:00");
                        nDia.setTotalHorasDia("00:00:00");
                        dao.insere(nDia);
                        dao.close();
                        Toast.makeText(ListaDiasActivity.this, nDia.getDiaNoFormatoDate(), Toast.LENGTH_LONG).show();

                        List<Dia> pegaNovoDia = new ArrayList<Dia>();
                        pegaNovoDia = dao.getListaDias();
                        dao.close();
                        Dia passaNovoDia = new Dia();

                        if (pegaNovoDia.size() == 1) {
                            passaNovoDia = pegaNovoDia.get(0);
                        } else {
                            passaNovoDia = pegaNovoDia.get(pegaNovoDia.size()-1);
                        }

                        novoDia = 1;
                        Intent irPontos = new Intent(ListaDiasActivity.this, ListaPontosActivity.class);
                        irPontos.putExtra("diaSelecionado", passaNovoDia);
                        irPontos.putExtra("novoDia",novoDia);
                        startActivity(irPontos);
                    }
                }).setNegativeButton("Não", null).show();
            }
        else {
                carregaLista();
            }
    }

    private void findViews() {
        listaDias = (ListView) findViewById(R.id.listViewDias);
        btnADCDias = (Button) findViewById(R.id.btnNovo);
        periodoDiaIni = (EditText) findViewById(R.id.edt_data_inicial_periodo);
        periodoDiaFim = (EditText) findViewById(R.id.edt_data_fim_periodo);
        edtSaldoMes = (EditText) findViewById(R.id.edt_saldo_mes);
    }


    private String calculaTempoPeriodo() throws ParseException {
        DiaDAO dao = new DiaDAO(ListaDiasActivity.this);
        List<Dia> listaDiasPegaSaldo = dao.getListaDias();

        List<Dia> listaDiasPeriodo = new ArrayList<Dia>();

        //pegando periodo para calculo
        PeriodoDAO periodoDAO = new PeriodoDAO(ListaDiasActivity.this);
        List<Periodo> periodoList = new ArrayList<Periodo>();
        periodoList = periodoDAO.getPeriodo();

        Calendar periodoInicial = Calendar.getInstance();
        Calendar periodoFinal = Calendar.getInstance();

        SimpleDateFormat formatarPeriodo = new SimpleDateFormat("yyyy-MM-dd");

        if (periodoList.size()>=1){

            if (periodoList.get(0).getpInicialDate()!=null){
                periodoInicial.setTime(formatarPeriodo.parse(periodoList.get(0).getpInicialDate()));
            }else{
                String semPeriodoInicial = "0001-1-01";
                periodoInicial.setTime(formatarPeriodo.parse(semPeriodoInicial));
            }

            if (periodoList.get(0).getpFinalDate()!=null){
                periodoFinal.setTime(formatarPeriodo.parse(periodoList.get(0).getpFinalDate()));
            }else {
                String semPeriodoFinal = "9999-12-31";
                periodoFinal.setTime(formatarPeriodo.parse(semPeriodoFinal));
            }
        }else {
            String semPeriodoInicial = "0001-1-01";
            periodoInicial.setTime(formatarPeriodo.parse(semPeriodoInicial));

            String semPeriodoFinal = "9999-12-31";
            periodoFinal.setTime(formatarPeriodo.parse(semPeriodoFinal));
        }

        String saldoPeriodo = "00:00:00";
        String horaPositivaPeriodo = "00:00:00";
        String horaNegativaPeriodo = "00:00:00";

        for (int i=0; i<listaDiasPegaSaldo.size(); i++){
            String dataLista = listaDiasPegaSaldo.get(i).getDiaNoFormatoDate();

            Calendar dataCal = Calendar.getInstance();
            dataCal.setTime(formatarPeriodo.parse(dataLista));
            if ( dataCal.getTimeInMillis() >= periodoInicial.getTimeInMillis() && dataCal.getTimeInMillis() <= periodoFinal.getTimeInMillis()){
                if (listaDiasPegaSaldo.get(i).getTotalHorasDia()!= null) {
                    listaDiasPeriodo.add(listaDiasPegaSaldo.get(i));
                }
            }
        }
        if (listaDiasPeriodo.size()==0){
            Toast.makeText(ListaDiasActivity.this, "Sem lançamentos para o periodo!",Toast.LENGTH_SHORT).show();
        }else
        if(listaDiasPeriodo.size()==1){
            int verSinal = verificaSeHoraNegativa(listaDiasPeriodo.get(0).getTotalHorasDia());
            if (verSinal==0){
                saldoPeriodo = "+"+listaDiasPeriodo.get(0).getSaldoHorasDia();
                sinalDoSaldo = 1;
            }else
                if (verSinal==1){
                    saldoPeriodo = "-"+listaDiasPeriodo.get(0).getSaldoHorasDia();
                    sinalDoSaldo = 2;
                }
        }else
        if (listaDiasPeriodo.size()>1){
            for (int i=0; i< listaDiasPeriodo.size(); i++){
                String saldoHoraI = listaDiasPeriodo.get(i).getSaldoHorasDia();
                String saldoTotalI = listaDiasPeriodo.get(i).getTotalHorasDia();

                //verificar sinal do saldo
                int testeDeSinaldoSaldo = verificaSeHoraNegativa(saldoTotalI);

                if (testeDeSinaldoSaldo == 0){
                    horaPositivaPeriodo = somaHoras(horaPositivaPeriodo, saldoHoraI);
                }else
                if (testeDeSinaldoSaldo==1){
                    horaNegativaPeriodo = somaHoras(horaNegativaPeriodo, saldoHoraI);
                }
            }
            int verificaQuemEMaior = verificaMaiorMenor2(horaPositivaPeriodo, horaNegativaPeriodo);
            if (verificaQuemEMaior==1){
                Date dateTimePositivo = jogaStringParaDate(horaPositivaPeriodo);
                Date dateTimeNegativo = jogaStringParaDate(horaNegativaPeriodo);

                String resultadoPeriodo = calculaHorasDate(dateTimeNegativo,dateTimePositivo);

                saldoPeriodo = "+"+resultadoPeriodo;

                sinalDoSaldo = 1;


            }else
                if (verificaQuemEMaior==2){
                    Date dateTimePositivo = jogaStringParaDate(horaPositivaPeriodo);
                    Date dateTimeNegativo = jogaStringParaDate(horaNegativaPeriodo);

                    //o maior vai em segundo
                    String resultadoPeriodo = calculaHorasDate(dateTimePositivo, dateTimeNegativo);

                    saldoPeriodo = "-"+resultadoPeriodo;
                    sinalDoSaldo= 2;
                }else
                    if (verificaQuemEMaior==0){
                        saldoPeriodo = "00:00:00";
                        sinalDoSaldo = 0;
                    }
        }
        return saldoPeriodo;
    }


    private Date jogaStringParaDate(String horaPeriodo) throws ParseException {
        SimpleDateFormat formatar = new SimpleDateFormat("HH:mm:ss");
        Date xTempoPeriodo = formatar.parse(horaPeriodo);
        return xTempoPeriodo;
    }

    private int verificaMaiorMenor(String saldoPositivo, String saldoNegativo) {
        int testeMaiorMenor = 0;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        Calendar calSaldoPositivo = Calendar.getInstance();
        Calendar calSaldoNegativo = Calendar.getInstance();

        try {
            calSaldoPositivo.setTime(format.parse(saldoPositivo));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            calSaldoNegativo.setTime(format.parse(saldoNegativo));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (calSaldoPositivo.getTime().getHours() > calSaldoNegativo.getTime().getHours()){
            testeMaiorMenor = 1;
        }else {
            testeMaiorMenor = 2;
        }

        return testeMaiorMenor;
    }
    private int verificaMaiorMenor2(String saldoPositivo, String saldoNegativo) {
        int testeMaiorMenor = 0;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        Calendar calSaldoPositivo = Calendar.getInstance();
        Calendar calSaldoNegativo = Calendar.getInstance();

        try {
            calSaldoPositivo.setTime(format.parse(saldoPositivo));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            calSaldoNegativo.setTime(format.parse(saldoNegativo));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (calSaldoPositivo.getTime().getHours() > calSaldoNegativo.getTime().getHours()){
            testeMaiorMenor = 1;
        }else
        if (calSaldoPositivo.getTime().getHours() < calSaldoNegativo.getTime().getHours()){
            testeMaiorMenor = 2;
        }else
        if (calSaldoPositivo.getTime().getHours() == calSaldoNegativo.getTime().getHours()){
            if (calSaldoPositivo.getTime().getMinutes() > calSaldoNegativo.getTime().getMinutes()){
                testeMaiorMenor = 1;
            }else
            if (calSaldoPositivo.getTime().getMinutes() < calSaldoNegativo.getTime().getMinutes()){
                testeMaiorMenor = 2;
            } else {
                if (calSaldoPositivo.getTime().getSeconds() > calSaldoNegativo.getTime().getSeconds()){
                    testeMaiorMenor = 1;
                }else
                if (calSaldoPositivo.getTime().getSeconds() < calSaldoNegativo.getTime().getSeconds()){
                    testeMaiorMenor = 2;
                }else {
                    testeMaiorMenor = 0;
                }
            }
        }

        return testeMaiorMenor;
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumindoLista();
    }

    private int verificaSeHoraNegativa(String horaFechada) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        int testeHora;
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(horaFechada));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int tt = cal.getTime().getHours();

        if (tt >= 8){
            testeHora = 0;
        }else {
            testeHora = 1;
        }
        return testeHora;
    }

    private String somaHoras(String x, String y){
        String string = x;
        String string2 = y;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        //usando Calendar
        Calendar cal1 = Calendar.getInstance();
        try {
            cal1.setTime(format.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal2 = Calendar.getInstance();
        try {
            cal2.setTime(format.parse(string2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //pegando horas minutos e segundos para int
        int hora1 = cal1.getTime().getHours();
        int minutos1 = cal1.getTime().getMinutes();
        int segundos1 = cal1.getTime().getSeconds();

        int hora2 = cal2.getTime().getHours();
        int minutos2 = cal2.getTime().getMinutes();
        int segundos2 = cal2.getTime().getSeconds();

        int saldoHora = hora2 + hora1;
        int saldoMinutos = minutos2 + minutos1;
        int saldoSegundos = segundos2 + segundos1;

        //somando horas minutos e segundos separadamente
        if(saldoMinutos >=60 ){
            saldoHora = saldoHora + 1;
            saldoMinutos = saldoMinutos - 60;
        }

        if (saldoSegundos >= 60){
            saldoMinutos = saldoMinutos + 1;
            saldoSegundos = saldoSegundos - 60;

            if(saldoMinutos <= 60 ){
                saldoHora = saldoHora + 1;
                saldoMinutos = saldoMinutos - 60;
            }
        }

        Date saldoDiaDate = new Date();
        saldoDiaDate.setHours(saldoHora);
        saldoDiaDate.setMinutes(saldoMinutos);
        saldoDiaDate.setSeconds(saldoSegundos);

        String saldoDia = format.format(saldoDiaDate);

        return saldoDia;

    }


    private String calculaHoras(String x, String y)  {


        Toast.makeText(ListaDiasActivity.this, " Hora 1: "+String.valueOf(y),Toast.LENGTH_LONG).show();
        Toast.makeText(ListaDiasActivity.this, " Hora 2: "+String.valueOf(x),Toast.LENGTH_LONG).show();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        //usando Calendar
        Calendar cal1 = Calendar.getInstance();
        try {
            cal1.setTime(format.parse(y));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal2 = Calendar.getInstance();
        try {
            cal2.setTime(format.parse(x));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //usando Date
        /*Date data1 = new Date();
        try {
            data1 = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date data2 = new Date();
        try {
            data2 = format.parse(string2);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        int hora1 = cal1.getTime().getHours();
        int minutos1 = cal1.getTime().getMinutes();
        int segundos1 = cal1.getTime().getSeconds();

        int hora2 = cal2.getTime().getHours();
        int minutos2 = cal2.getTime().getMinutes();
        int segundos2 = cal2.getTime().getSeconds();


        int saldoHora = hora2 - hora1;
        int saldoMinutos = minutos2 - minutos1;
        int saldoSegundos = segundos2 - segundos1;

        Toast.makeText(ListaDiasActivity.this, "Saldo Hora: "+String.valueOf(saldoHora),Toast.LENGTH_LONG).show();
        if(saldoMinutos < 0 ){
            saldoHora = saldoHora - 1;
            saldoMinutos = 60 + saldoMinutos;
        }

        if (saldoSegundos < 0){
            saldoMinutos = saldoMinutos - 1;
            saldoSegundos = 60 + saldoSegundos;

            if(saldoMinutos < 0 ){
                saldoHora = saldoHora - 1;
                saldoMinutos = 60 + saldoMinutos;
            }
        }


        Date saldoDiaDate = new Date();
        saldoDiaDate.setHours(saldoHora);
        saldoDiaDate.setMinutes(saldoMinutos);
        saldoDiaDate.setSeconds(saldoSegundos);

        String saldoDia = format.format(saldoDiaDate);

        return saldoDia;
    }

    private String calculaHorasDate(Date x, Date y)  {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");


        int hora1 = x.getHours();
        int minutos1 = x.getMinutes();
        int segundos1 = x.getSeconds();

        int hora2 = y.getHours();
        int minutos2 = y.getMinutes();
        int segundos2 = y.getSeconds();


        int saldoHora = hora2 - hora1;
        int saldoMinutos = minutos2 - minutos1;
        int saldoSegundos = segundos2 - segundos1;

        if(saldoMinutos < 0 ){
            saldoHora = saldoHora - 1;
            saldoMinutos = 60 + saldoMinutos;
        }

        if (saldoSegundos < 0){
            saldoMinutos = saldoMinutos - 1;
            saldoSegundos = 60 + saldoSegundos;

            if(saldoMinutos < 0 ){
                saldoHora = saldoHora - 1;
                saldoMinutos = 60 + saldoMinutos;
            }
        }


        Date saldoDiaDate = new Date();
        saldoDiaDate.setHours(saldoHora);
        saldoDiaDate.setMinutes(saldoMinutos);
        saldoDiaDate.setSeconds(saldoSegundos);

        String saldoDia = format.format(saldoDiaDate);

        return saldoDia;
    }

    private void carregaLista() {
        DiaDAO dao = new DiaDAO(this);
        dias = dao.getListaDias();
        dao.close();


        atualizaPeriodo();

        if (dias.size() == 0){
            new AlertDialog.Builder(ListaDiasActivity.this).setIcon(R.drawable.ic_alert)
                    .setTitle("Lista Vazia!").setMessage("Voçê ainda não possui nenhum dia cadastrado.")
                    .setPositiveButton("Incluir Dia", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentADC = new Intent(ListaDiasActivity.this, NovoDiaActivity.class);
                            serializableDiaPeriodo = 3;
                            intentADC.putExtra("periodo", serializableDiaPeriodo);
                            startActivity(intentADC);
                        }
                    }).setNegativeButton("Fechar", null).show();
        }else {
            List<Dia> listaOrdenada = new ArrayList<>();

            for (int i =0; i < dias.size();i++){


            }
            AdapterListaDias adapter = new AdapterListaDias(this, dias);
            listaDias.setAdapter(adapter);

            periodoDiaIni.setTextColor(getResources().getColor(R.color.background_floating_material_light));
            periodoDiaFim.setTextColor(getResources().getColor(R.color.background_floating_material_light));

            String saldoTextPeriodo = "00:00:00";
            try {
                saldoTextPeriodo = calculaTempoPeriodo();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (sinalDoSaldo == 1) {
                edtSaldoMes.setText(saldoTextPeriodo);
                edtSaldoMes.setTextColor(getResources().getColor(R.color.cor_positiva));
            } else
            if (sinalDoSaldo==2){
                edtSaldoMes.setText(saldoTextPeriodo);
                edtSaldoMes.setTextColor(getResources().getColor(R.color.cor_negativa));
            }else{
                edtSaldoMes.setText(saldoTextPeriodo);
                edtSaldoMes.setTextColor(getResources().getColor(R.color.background_floating_material_light));
            }
        }
    }

    private void atualizaPeriodo() {
        PeriodoDAO periodoDAO = new PeriodoDAO(this);
        periodos = periodoDAO.getPeriodo();
        periodoDAO.close();

        if (periodos.size()>=1){

            if (periodos.get(0).getPeriodoInicial() != null) {
                String pInicial = periodos.get(0).getPeriodoInicial();
                periodoDiaIni.setText(pInicial);
            } else {
                periodoDiaIni.setText("00");
            }

            if (periodos.get(0).getPeriodoFinal() != null) {
                String pFinal = periodos.get(0).getPeriodoFinal();
                periodoDiaFim.setText(pFinal);
            } else {
                periodoDiaFim.setText("00");
            }

        }else{
            periodoDiaIni.setText("00");
            periodoDiaFim.setText("00");
        }

    }

    private void resumindoLista() {
        DiaDAO dao = new DiaDAO(this);
        dias = dao.getListaDias();
        dao.close();

        atualizaPeriodo();

        if (dias.size() == 0){
           Toast.makeText(ListaDiasActivity.this, "Lista ainda vazia!", Toast.LENGTH_SHORT).show();
        }else {
            AdapterListaDias adapter = new AdapterListaDias(this, dias);
            listaDias.setAdapter(adapter);
            //periodoDiaIni.setText(dias.get(0).getDia());

            periodoDiaIni.setTextColor(getResources().getColor(R.color.background_floating_material_light));
            periodoDiaFim.setTextColor(getResources().getColor(R.color.background_floating_material_light));


            String saldoTextPeriodo = null;
            try {
                saldoTextPeriodo = calculaTempoPeriodo();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (sinalDoSaldo == 1) {
                edtSaldoMes.setText(saldoTextPeriodo);
                edtSaldoMes.setTextColor(getResources().getColor(R.color.cor_positiva));
            } else if (sinalDoSaldo == 2) {
                edtSaldoMes.setText(saldoTextPeriodo);
                edtSaldoMes.setTextColor(getResources().getColor(R.color.cor_negativa));
            } else if (sinalDoSaldo == 0) {
                edtSaldoMes.setText(saldoTextPeriodo);
                edtSaldoMes.setTextColor(getResources().getColor(R.color.background_floating_material_light));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_dias, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ListaDiasActivity.this)
                        .setIcon(R.drawable.ic_deletar).setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar ?").setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dia diaSelecionado = (Dia) listaDias.getAdapter().getItem(info.position);

                        DiaDAO diaDao = new DiaDAO(ListaDiasActivity.this);
                        PontoDAO pontosDao = new PontoDAO(ListaDiasActivity.this);

                        Ponto pontosDoDiaSelecionado = new Ponto();
                        pontosDoDiaSelecionado.setIdPonto(diaSelecionado.getId());

                        diaDao.deletar(diaSelecionado);
                        diaDao.close();

                        pontosDao.deletarPorId(pontosDoDiaSelecionado);
                        pontosDao.close();
                        carregaLista();


                        resumindoLista();
                    }
                }).setNegativeButton("NÂO", null).show();
                return false;

            }
        });


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
        if (month == 1){
            nomeMes = "JANEIRO";
        }else
        if (month == 2){
            nomeMes = "FEVEREIRO";
        }
        if (month == 3){
            nomeMes = "MARÇO";
        }else
        if (month == 4){
            nomeMes = "ABRIL";
        }else
        if (month == 5){
            nomeMes = "MAIO";
        }else
        if (month == 6){
            nomeMes = "JUNHO";
        }else
        if (month == 7){
            nomeMes = "JULHO";
        }else
        if (month == 8){
            nomeMes = "AGOSTO";
        }else
        if (month == 9){
            nomeMes = "SETEMBRO";
        }else
        if (month == 10){
            nomeMes = "OUTUBRO";
        }else
        if (month == 11){
            nomeMes = "NOVEMBRO";
        }else
        if (month == 12){
            nomeMes = "DEZEMBRO";
        }

        return nomeMes;
    }
}
