package com.br.checkponto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.br.checkponto.adapter.AdapterListaPonto;
import com.br.checkponto.dao.Dia;
import com.br.checkponto.dao.DiaDAO;
import com.br.checkponto.dao.Ponto;
import com.br.checkponto.dao.PontoDAO;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListaPontosActivity extends AppCompatActivity {

    private Button btnCheckPonto;
    private ListView listViewPonto;
    private SwipeMenuListView listViewSwipe;
    private List<Ponto> pontos = new ArrayList<>();
    private List<Ponto> listaPontos;
    private EditText edtNovoPonto;
    private EditText edtTarefa;
    private Ponto novoPonto;
    private Dia diaSelecionado;
    private Button btnRefresh;
    private Button btnFechar;
    private Button btnSimularDia;
    private EditText totalHorasDia;
    private EditText saldoDia;
    private Integer novoDia;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_lista_pontos);

        novoDia = (Integer) getIntent().getSerializableExtra("novoDia");
        diaSelecionado = (Dia) getIntent().getSerializableExtra("diaSelecionado");


        diaSelecionado = (Dia) getIntent().getSerializableExtra("diaSelecionado");
        setTitle(diaSelecionado.getDia() + " - " + diaSelecionado.getMes() + " - " + diaSelecionado.getAno());


        final String hora_atual = pegaHoraEmString();

        totalHorasDia = (EditText) findViewById(R.id.editTextTotalDia);
        saldoDia = (EditText) findViewById(R.id.edt_saldo);

        edtNovoPonto = (EditText) findViewById(R.id.edtNovoPonto);
        edtNovoPonto.setText(hora_atual);
        edtTarefa = (EditText) findViewById(R.id.edt_tarefa_ponto);

        //esconder teclado ao abrir tela
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtNovoPonto.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hora_atual = pegaHoraEmString();

                edtNovoPonto = (EditText) findViewById(R.id.edtNovoPonto);
                edtNovoPonto.setText(hora_atual);
            }
        });

        btnCheckPonto = (Button) findViewById(R.id.btnCheckPonto);
        btnCheckPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                novoPonto = salvaponto();

                PontoDAO pontoDAO = new PontoDAO(ListaPontosActivity.this);
                pontoDAO.insere(novoPonto);
                pontoDAO.close();
                pontos.add(novoPonto);
                carregaLista();

            }
        });
        carregaLista();

        //lista swipe

        listViewSwipe = (SwipeMenuListView) findViewById(R.id.lisViewPontos);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.WHITE));

                // set item width
                deleteItem.setWidth(230);

                // set a icon
                deleteItem.setIcon(R.drawable.erase);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        if (diaSelecionado.getSaldoHorasDia() == null) {
            listViewSwipe.setMenuCreator(creator);
        }
        listViewSwipe.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {

                final Ponto pontoSelecionado = (Ponto) listViewSwipe.getAdapter().getItem(position);
                PontoDAO daoDeletar = new PontoDAO(ListaPontosActivity.this);
                daoDeletar.deletar(pontoSelecionado);
                daoDeletar.close();
                carregaLista();
                return false;
            }
        });

        btnFechar = (Button) findViewById(R.id.btnFecharDia);
        btnSimularDia = (Button) findViewById(R.id.btn_simular_dia);

        if (diaSelecionado.getSaldoHorasDia() != null) {
            btnFechar.setEnabled(false);
            btnSimularDia.setEnabled(false);
            listViewPonto.setEnabled(false);
            btnCheckPonto.setEnabled(false);
            new AlertDialog.Builder(ListaPontosActivity.this).setIcon(R.drawable.ic_day_closed)
                    .setTitle("Dia Fechado").setMessage("O dia já foi fechado, você não pode simular horas nem fechá-lo novamente!").setPositiveButton("OK", null).show();

        }else if (novoDia==1){

            new AlertDialog.Builder(ListaPontosActivity.this).setMessage("Deseja registrar o Ponto agora?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            novoPonto = salvaponto();

                            PontoDAO pontoDAO = new PontoDAO(ListaPontosActivity.this);
                            pontoDAO.insere(novoPonto);
                            pontoDAO.close();
                            pontos.add(novoPonto);
                            carregaLista();
                        }
                    }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent voltarParaHome = new Intent(ListaPontosActivity.this, ListaDiasActivity.class);
                    Integer verificaLista = 0;
                    voltarParaHome.putExtra("verificaLista", verificaLista);
                    startActivity(voltarParaHome);
                }
            }).show();
        }

        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ListaPontosActivity.this).setTitle("Fechar Dia")
                        .setIcon(R.drawable.icon_close).setMessage("Após fechar o dia, não será mais possível adcionar outro ponto!")
                        .setPositiveButton("FECHAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fecharDia();
                                carregaLista();
                            }
                        }).setNegativeButton("Cancelar", null).show();
            }
        });


        btnSimularDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simularDia();

            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void salvaTotalHoraDia(String horaFechada, String saldoDia) {

        Dia diaSalvaHoradia = new Dia();
        diaSalvaHoradia.setId(diaSelecionado.getId());
        diaSalvaHoradia.setTotalHorasDia(horaFechada);
        diaSalvaHoradia.setSaldoHorasDia(saldoDia);
        DiaDAO dao = new DiaDAO(ListaPontosActivity.this);
        dao.salvaHoraDia(diaSalvaHoradia);
        dao.close();
    }

    private int verificaSeHoraNegativa(String horaFechada) {
        String string = horaFechada;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        int testeHora;
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int tt = cal.getTime().getHours();

        if (tt >= 8) {
            testeHora = 0;
        } else {
            testeHora = 1;
        }
        return testeHora;
    }

    @NonNull
    private String pegaHoraEmString() {
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        Date hora = cal.getTime();

        return dateFormat_hora.format(hora);
    }

    private void fecharDia() {

        //pegando lista de pontos
        PontoDAO daoFechar = new PontoDAO(ListaPontosActivity.this);
        Ponto pontoFechar = new Ponto();
        pontoFechar.setIdPonto(diaSelecionado.getId());
        List<Ponto> listaPontoFechar = daoFechar.getListaPonto(pontoFechar);

        //tamanho da lista
        String size = String.valueOf(listaPontoFechar.size());
        if (listaPontoFechar.size() < 4) {
            new AlertDialog.Builder(ListaPontosActivity.this).setIcon(R.drawable.ic_alert)
                    .setTitle("Alerta").setMessage("Para fechar o dia, você precisa ter no mínimo 4 marcações!").setPositiveButton("OK", null).show();
        } else if (listaPontoFechar.size() >= 4 && listaPontoFechar.size() % 2 == 0) {

            String horaFechada = calcularHorasDia();

            String oitoHoras = "08:00:00";
            String saldoDoDia;

            //verifica a hora total do dia se é positivo ou negativo
            int verificarHoraNegativa2 = verificaSeHoraNegativa(horaFechada);

            //positiva
            if (verificarHoraNegativa2 == 0) {
                saldoDoDia = calculaHoras(oitoHoras, horaFechada);

                totalHorasDia.setText(horaFechada);
                totalHorasDia.setTextColor(getResources().getColor(R.color.cor_positiva));

                saldoDia.setText("+" + saldoDoDia);
                saldoDia.setTextColor(getResources().getColor(R.color.cor_positiva));

                //negativa
            } else {

                saldoDoDia = calculaHoras(horaFechada, oitoHoras);

                totalHorasDia.setText(horaFechada);
                totalHorasDia.setTextColor(getResources().getColor(R.color.cor_negativa));

                saldoDia.setText("-" + saldoDoDia);
                saldoDia.setTextColor(getResources().getColor(R.color.cor_negativa));
            }

            salvaTotalHoraDia(horaFechada, saldoDoDia);

            new AlertDialog.Builder(ListaPontosActivity.this).setIcon(R.drawable.ic_alert)
                    .setTitle("Dia Fehcado").setMessage("Este dia foi fechado!").setPositiveButton("OK", null).show();

        } else {
            new AlertDialog.Builder(ListaPontosActivity.this).setIcon(R.drawable.ic_alert)
                    .setTitle("Alerta").setMessage("Ponto não pode ser fechado por falta de marcações." +
                    " Verifique novamente as marcações, o total de marcações deve ser par!").setPositiveButton("OK", null).show();
        }
    }

    private void simularDia() {

        //pegando lista de pontos
        PontoDAO daoSimular = new PontoDAO(ListaPontosActivity.this);
        Ponto pontoFechar = new Ponto();
        pontoFechar.setIdPonto(diaSelecionado.getId());
        List<Ponto> listaPontoFechar = daoSimular.getListaPonto(pontoFechar);

        //tamanho da lista
        if (listaPontoFechar.size() < 4) {
            new AlertDialog.Builder(ListaPontosActivity.this).setIcon(R.drawable.notification_error)
                    .setTitle("Alerta").setMessage("Para simular o dia, você precisa ter no mínimo 4 marcações!").setPositiveButton("OK", null).show();
        } else if (listaPontoFechar.size() >= 4 && listaPontoFechar.size() % 2 == 0) {

            String horaFechada = calcularHorasDia();

            String oitoHoras = "08:00:00";
            String saldoDoDia;

            //verifica a hora total do dia se é positivo ou negativo
            int verificarHoraNegativa2 = verificaSeHoraNegativa(horaFechada);


            //positiva
            if (verificarHoraNegativa2 == 0) {
                saldoDoDia = calculaHoras(oitoHoras, horaFechada);

                totalHorasDia.setText(horaFechada);
                totalHorasDia.setTextColor(getResources().getColor(R.color.cor_positiva));

                saldoDia.setText("+" + saldoDoDia);
                saldoDia.setTextColor(getResources().getColor(R.color.cor_positiva));

                new AlertDialog.Builder(ListaPontosActivity.this).setTitle("Simulação realizada!")
                        .setMessage("Seu saldo já se está Positivo." +
                                "Você já tem :"+saldoDoDia+" de crédito. Não esqueça de verificar o tempo de tolerância.").show();
                //negativa
            } else {

                saldoDoDia = calculaHoras(horaFechada, oitoHoras);

                totalHorasDia.setText(horaFechada);
                totalHorasDia.setTextColor(getResources().getColor(R.color.cor_negativa));

                saldoDia.setText("-" + saldoDoDia);
                saldoDia.setTextColor(getResources().getColor(R.color.cor_negativa));

                new AlertDialog.Builder(ListaPontosActivity.this).setTitle("Simulação realizada!")
                        .setMessage("Por enquanto seu saldo está Negativo. " +
                                "Faltam :" + saldoDoDia + " para completar sua jornada de trabalho. Não esqueça de verificar o tempo de tolerância.").show();
            }

        } else {
            new AlertDialog.Builder(ListaPontosActivity.this).setIcon(R.drawable.notification_error)
                    .setTitle("Alerta").setMessage("Ponto não pôde ser simulado por falta de marcações." +
                    " Verifique novamente as marcações, o total de marcações deve ser par!").setPositiveButton("OK", null).show();
        }
    }

    private String calcularHorasDia() {
        //pegando id do ponto selecionado através do dia selecionado
        Ponto pontoCalc = new Ponto();
        pontoCalc.setIdPonto(diaSelecionado.getId());
        PontoDAO dao = new PontoDAO(ListaPontosActivity.this);

        //pegando lista de pontos do dia selecionado
        List<Ponto> listaPontoCalc = new ArrayList<>();
        listaPontoCalc = dao.getListaPonto(pontoCalc);

        List<Ponto> pontoHorasCalc = new ArrayList<>();

        //subtrair horas
        for (int i = 0; i < listaPontoCalc.size(); i = i + 2) {

            Ponto pontoCalculado = new Ponto();
            String x = listaPontoCalc.get(i).getPonto();
            String y = listaPontoCalc.get(i + 1).getPonto();

            String horasCalcDia = calculaHoras(x, y);

            pontoCalculado.setPonto(horasCalcDia);

            pontoHorasCalc.add(pontoCalculado);

        }
        //somar as horas
        String somaDasHoras = pontoHorasCalc.get(0).getPonto();
        for (int i = 1; i < pontoHorasCalc.size(); i++) {
            String y = pontoHorasCalc.get(i).getPonto();

            somaDasHoras = somaHoras(somaDasHoras, y);
        }
        return somaDasHoras;
    }

    private String somaHoras(String x, String y) {
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
        if (saldoMinutos >= 60) {
            saldoHora = saldoHora + 1;
            saldoMinutos = saldoMinutos - 60;
        }

        if (saldoSegundos >= 60) {
            saldoMinutos = saldoMinutos + 1;
            saldoSegundos = saldoSegundos - 60;

            if (saldoMinutos <= 60) {
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

    private String calculaHoras(String x, String y) {

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



        int hora1 = cal1.getTime().getHours();
        int minutos1 = cal1.getTime().getMinutes();
        int segundos1 = cal1.getTime().getSeconds();

        int hora2 = cal2.getTime().getHours();
        int minutos2 = cal2.getTime().getMinutes();
        int segundos2 = cal2.getTime().getSeconds();

        int saldoHora = hora2 - hora1;
        int saldoMinutos = minutos2 - minutos1;
        int saldoSegundos = segundos2 - segundos1;

        if (saldoMinutos < 0) {
            saldoHora = saldoHora - 1;
            saldoMinutos = 60 + saldoMinutos;
        }

        if (saldoSegundos < 0) {
            saldoMinutos = saldoMinutos - 1;
            saldoSegundos = 60 + saldoSegundos;

        if (saldoMinutos < 0) {
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

    private Ponto salvaponto() {
        Ponto pegaNovoPonto = new Ponto();
        edtNovoPonto = (EditText) findViewById(R.id.edtNovoPonto);
        edtTarefa = (EditText) findViewById(R.id.edt_tarefa_ponto);

        pegaNovoPonto.setIdPonto(diaSelecionado.getId());
        pegaNovoPonto.setPonto(edtNovoPonto.getText().toString());
        pegaNovoPonto.setTarefa(edtTarefa.getText().toString());

        return pegaNovoPonto;
    }

    private void carregaLista() {
        listViewPonto = (ListView) findViewById(R.id.lisViewPontos);

        PontoDAO pontoDAO = new PontoDAO(ListaPontosActivity.this);

        Ponto idDia = new Ponto();
        idDia.setIdPonto(diaSelecionado.getId());

        listaPontos = pontoDAO.getListaPonto(idDia);
        pontoDAO.close();

        listViewPonto.setAdapter(new AdapterListaPonto(ListaPontosActivity.this, listaPontos));
        int listPontoSize = listaPontos.size();
        if (listPontoSize > 0) {
            String ultimaTarefa = listaPontos.get(listPontoSize - 1).getTarefa();
            edtTarefa.setText(ultimaTarefa);
        }


        String totalHora = diaSelecionado.getTotalHorasDia();
        String saldoH = diaSelecionado.getSaldoHorasDia();
        if (totalHora != null) {
            int verificarHoraNegativa = verificaSeHoraNegativa(totalHora);
            if (verificarHoraNegativa == 0) {
                totalHorasDia.setText(totalHora);
                totalHorasDia.setTextColor(getResources().getColor(R.color.cor_positiva));

                saldoDia.setText("+" + saldoH);
                saldoDia.setTextColor(getResources().getColor(R.color.cor_positiva));
            } else {
                totalHorasDia.setText(totalHora);
                totalHorasDia.setTextColor(getResources().getColor(R.color.cor_negativa));

                saldoDia.setText("-" + saldoH);
                saldoDia.setTextColor(getResources().getColor(R.color.cor_negativa));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (novoDia==null) {
            if (diaSelecionado.getSaldoHorasDia() != null) {
                getMenuInflater().inflate(R.menu.menu_actionbar, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_lista_pontos, menu);
            }
            return true;
        }else{
            return false;
        }

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
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListaPontos Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.br.checkponto/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListaPontos Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.br.checkponto/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
