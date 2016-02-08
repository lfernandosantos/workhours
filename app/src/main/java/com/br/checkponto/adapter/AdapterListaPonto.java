package com.br.checkponto.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.checkponto.R;
import com.br.checkponto.dao.Dia;
import com.br.checkponto.dao.Ponto;
import com.br.checkponto.dao.PontoDAO;

import java.util.List;

/**
 * Created by ferna on 28/09/2015.
 */
public class AdapterListaPonto extends BaseAdapter {
    private Activity activity;
    private final List<Ponto> pontos;

    public AdapterListaPonto(Activity activity, List<Ponto> pontos) {
        this.activity = activity;
        this.pontos = pontos;
    }

    @Override
    public int getCount() {
        return pontos.size() ;
    }

    @Override
    public Object getItem(int position) {
        return pontos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pontos.get(position).getIdPonto();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.itens_lista_pontos_adapter, parent, false);

        Ponto ponto = pontos.get(position);


        TextView txtPonto = (TextView) view.findViewById(R.id.ponto_item_lista_adapter);
        txtPonto.setText(ponto.getPonto());

        TextView txtTarefa = (TextView) view.findViewById(R.id.tarefa_item_lista_adapter);
        txtTarefa.setText(ponto.getTarefa());


        //teste na lista
        PontoDAO dao = new PontoDAO(activity);

        List<Ponto> teste = dao.getListaPonto(ponto);



       if (position % 2 == 0){
           ImageView iconPontoEnter = (ImageView) view.findViewById(R.id.imageViewPonto);
            iconPontoEnter.setBackground(activity.getResources().getDrawable(R.drawable.ic_ponto_enter2));
        }else {
           ImageView iconPontoLeave = (ImageView) view.findViewById(R.id.imageViewPonto);
            iconPontoLeave.setBackground(activity.getResources().getDrawable(R.drawable.ic_ponto_leave));
        }



        return view;
    }
}
