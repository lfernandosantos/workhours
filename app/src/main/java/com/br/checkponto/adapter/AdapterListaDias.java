package com.br.checkponto.adapter;



import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.checkponto.ListaPontosActivity;
import com.br.checkponto.R;
import com.br.checkponto.dao.Dia;
import com.br.checkponto.dao.Ponto;
import com.br.checkponto.dao.PontoDAO;

import java.util.List;

/**
 * Created by ferna on 06/09/2015.
 */
public class AdapterListaDias extends BaseAdapter {
    private Activity activity;
    private final List<Dia> dias;

    public AdapterListaDias(Activity activity, List<Dia> dias) {
        this.activity = activity;
        this.dias = dias;
    }

    @Override
    public int getCount() {
        return dias.size() ;
    }

    @Override
    public Object getItem(int position) {
        return dias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dias.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.itens_lista_dias_adapter, parent, false);

        Dia dia = dias.get(position);


        TextView txtdia = (TextView) view.findViewById(R.id.dia_item_lista_adapter);
        txtdia.setText(dia.getDia());

        TextView txtMes = (TextView) view.findViewById(R.id.mes_item_lista_adapter);
        txtMes.setText(" / "+dia.getMes());

        TextView txtAno = (TextView) view.findViewById(R.id.ano_item_lista_adapter);
        txtAno.setText(" / "+dia.getAno());


        //teste na lista
        PontoDAO dao = new PontoDAO(activity);
        Ponto pontoTeste = new Ponto();
        pontoTeste.setIdPonto(dia.getId());
        List<Ponto> teste = dao.getListaPonto(pontoTeste);
        if (teste.size()== 1){
            TextView txtP1 = (TextView) view.findViewById(R.id.textViewP1);
            txtP1.setText("--");
        }

        if (teste.size()== 2){
            TextView txtP1 = (TextView) view.findViewById(R.id.textViewP1);
            txtP1.setText("--");

            TextView txtP2 = (TextView) view.findViewById(R.id.textViewP2);
            txtP2.setText("--");


        }

        if (teste.size()== 3){
            TextView txtP1 = (TextView) view.findViewById(R.id.textViewP1);
            txtP1.setText("--");

            TextView txtP2 = (TextView) view.findViewById(R.id.textViewP2);
            txtP2.setText("--");

            TextView txtP3 = (TextView) view.findViewById(R.id.textViewP3);
            txtP3.setText("--");
        }

        if (teste.size()>=4){
            TextView txtP1 = (TextView) view.findViewById(R.id.textViewP1);
            txtP1.setText("--");

            TextView txtP2 = (TextView) view.findViewById(R.id.textViewP2);
            txtP2.setText("--");

            TextView txtP3 = (TextView) view.findViewById(R.id.textViewP3);
            txtP3.setText("--");

            TextView txtP4 = (TextView) view.findViewById(R.id.textViewP4);
            txtP4.setText("--");
        }

        /*
       if (position % 2 == 0){
           ImageView iconPontoEnter =
            view.setBackgroundColor(activity.getResources().getColor(R.color.linha_par));
        }else {
           ImageView iconPontoLeave = view.findViewById(R.id)
            view.setBackgroundColor(activity.getResources().getColor(R.color.linha_impar));
        }*/



        return view;
    }
}
