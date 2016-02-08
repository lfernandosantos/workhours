package com.br.checkponto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.br.checkponto.ListaDiasActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ferna on 06/09/2015.
 */
public class DiaDAO extends SQLiteOpenHelper {
    private static final String NAME = "DB";
    private static final int VERSAO = 1;
    private static final String TABELA = "TBCheckPonto";

    public DiaDAO(Context context) {
        super(context, NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE "+TABELA+"(id INTEGER PRIMARY KEY, dia TEXT, mes TEXT, ano TEXT, totalHorasDia TEXT, saldoHorasDia TEXT, diaEmDate TEXT);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS "+ TABELA;
        db.execSQL(ddl);
        onCreate(db);
    }
    public void insere (Dia dia) {
        ContentValues values = new ContentValues();

        values.put("dia", dia.getDia());
        values.put("mes", dia.getMes());
        values.put("ano", dia.getAno());
        values.put("diaEmDate", dia.getDiaNoFormatoDate());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public List<Dia> getListaDias(){
        List<Dia> dias = new ArrayList<Dia>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (c.moveToNext()){
            Dia dia = new Dia();

            dia.setId(c.getLong(c.getColumnIndex("id")));
            dia.setDia(c.getString(c.getColumnIndex("dia")));
            dia.setMes(c.getString(c.getColumnIndex("mes")));
            dia.setAno(c.getString(c.getColumnIndex("ano")));
            dia.setTotalHorasDia(c.getString(c.getColumnIndex("totalHorasDia")));
            dia.setSaldoHorasDia(c.getString(c.getColumnIndex("saldoHorasDia")));
            dia.setDiaNoFormatoDate(c.getString(c.getColumnIndex("diaEmDate")));

            dias.add(dia);
        }
        return dias;
    }

    public void deletar(Dia diaSelecionado) {

        String [] args = {diaSelecionado.getId().toString()};
        getWritableDatabase().delete(TABELA,"id=?",args);
    }

    public void salvaHoraDia(Dia diaSalvaHoradia) {
        String [] args = {diaSalvaHoradia.getId().toString()};
        ContentValues values = new ContentValues();
        values.put("totalHorasDia",diaSalvaHoradia.getTotalHorasDia());
        values.put("saldoHorasDia",diaSalvaHoradia.getSaldoHorasDia());

        getWritableDatabase().update(TABELA,values,"id=?",args);
    }
}
