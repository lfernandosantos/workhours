package com.br.checkponto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferna on 20/09/2015.
 */
public class PontoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String NAME = "BDPonto";
    private static final String TABELA = "Hora";

    public PontoDAO(Context context) {
        super(context, NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl ="CREATE TABLE "+TABELA+"(id INTEGER, ponto TEXT, tarefa TEXT);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS "+ TABELA;
        db.execSQL(ddl);
        onCreate(db);
    }
    public void insere (Ponto ponto) {
        ContentValues values = new ContentValues();
        values.put("id", ponto.getIdPonto());
        values.put("ponto", ponto.getPonto());
        values.put("tarefa", ponto.getTarefa());
        getWritableDatabase().insert(TABELA, null, values);
    }

    public List<Ponto> getListaPonto(Ponto p) {
        List<Ponto> pontos = new ArrayList<>();

        String[] args = {p.getIdPonto().toString()};
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" WHERE id =?;",args);
        while (c.moveToNext()){
            Ponto ponto = new Ponto();
            ponto.setIdPonto(c.getLong(c.getColumnIndex("id")));
            ponto.setPonto(c.getString(c.getColumnIndex("ponto")));
            ponto.setTarefa(c.getString(c.getColumnIndex("tarefa")));
            pontos.add(ponto);
        }
        return pontos;
    }

    public void deletar(Ponto pontoSelecionado) {
        String [] args = {pontoSelecionado.getPonto()};
        getWritableDatabase().delete(TABELA,"ponto=?",args);
    }

    public void deletarPorId(Ponto pontoSelecionado) {
        String [] args = {pontoSelecionado.getIdPonto().toString()};
        getWritableDatabase().delete(TABELA,"id=?",args);
    }
}
