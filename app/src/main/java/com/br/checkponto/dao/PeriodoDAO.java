package com.br.checkponto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.br.checkponto.adapter.AdapterListaDias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferna on 02/11/2015.
 */
public class PeriodoDAO extends SQLiteOpenHelper {
    private static final String NAME = "PeriodoBD";
    private static final String TABELA = "TBPeriodo";
    private static final int VERSAO = 1;

    public PeriodoDAO(Context context) {
        super(context, NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE "+TABELA+"(id INTEGER PRIMARY KEY, pInicial TEXT, pInicialData TEXT, pFinal TEXT, pFinalDate TEXT, pDia TEXT);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS "+ TABELA;
        db.execSQL(ddl);
        onCreate(db);
    }

    public void alterarPeriodo(Periodo periodo, int tipo){
        ContentValues values = new ContentValues();

        if (tipo == 1){
            values.put("pInicial", periodo.getPeriodoInicial());
            values.put("pInicialData",periodo.getpInicialDate());
        }else
        if (tipo == 2){
            values.put("pFinal", periodo.getPeriodoFinal());
            values.put("pFinalDate",periodo.getpFinalDate());
        }

        String[] args = {periodo.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", args);

    }


    public void inserirPeriodo(Periodo periodo, int tipo){

        ContentValues values = new ContentValues();

        if (tipo ==1){
            values.put("pInicial", periodo.getPeriodoInicial());
            values.put("pInicialData",periodo.getpInicialDate());
        }else
        if (tipo ==2){
            values.put("pFinal", periodo.getPeriodoFinal());
            values.put("pFinalDate",periodo.getpFinalDate());
        }
        getWritableDatabase().insert(TABELA, null, values);
    }

    public void inserirPeriodoDia(Periodo periodo){

        ContentValues values = new ContentValues();

        values.put("id", periodo.getId());
        //values.put("dia", periodo.getDia());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public List<Periodo> getPeriodo(){

        List<Periodo> periodos = new ArrayList<Periodo>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (c.moveToNext()){
            Periodo periodo =  new Periodo();

            periodo.setId(c.getInt(c.getColumnIndex("id")));
            periodo.setPeriodoInicial(c.getString(c.getColumnIndex("pInicial")));
            periodo.setpInicialDate(c.getString(c.getColumnIndex("pInicialData")));
            periodo.setPeriodoFinal(c.getString(c.getColumnIndex("pFinal")));
            periodo.setpFinalDate(c.getString(c.getColumnIndex("pFinalDate")));

            periodos.add(periodo);
        }

        return periodos;
    }

}
