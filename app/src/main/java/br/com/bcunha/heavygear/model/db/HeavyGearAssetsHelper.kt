package br.com.bcunha.heavygear.model.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

import java.util.ArrayList

import br.com.bcunha.heavygear.model.pojo.Ativo

/**
 * Created by BRUNO on 12/09/2016.
 */
class HeavyGearAssetsHelper(context: Context)//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
    : SQLiteAssetHelper(context, DB_NOME, null, VERSAO) {

    internal var heavyGearDB: SQLiteDatabase? = null

    fun openDB() {
        heavyGearDB = writableDatabase
    }

    fun closeDB() {
        if (heavyGearDB != null && heavyGearDB!!.isOpen) {
            heavyGearDB!!.close()
        }
    }

    fun insertAtivo(id: Int, codigo: String, empresa: String, tipo: String): Long {
        val contentValues = ContentValues()
        if (id != -1) {
            contentValues.put(CAMPO_ID, id)
        }
        contentValues.put(CAMPO_CODIGO, codigo)
        contentValues.put(CAMPO_EMPRESA, empresa)
        contentValues.put(CAMPO_TIPO, tipo)

        return heavyGearDB!!.insert(TABELA_ACOES, null, contentValues)
    }

    fun updateAtivos(id: Int, codigo: String, empresa: String, tipo: String): Long {
        val contentValues = ContentValues()
        contentValues.put(CAMPO_CODIGO, codigo)
        contentValues.put(CAMPO_EMPRESA, empresa)
        contentValues.put(CAMPO_TIPO, tipo)

        val where = CAMPO_ID + " = " + id

        return heavyGearDB!!.update(TABELA_ACOES, contentValues, where, null).toLong()
    }

    fun deleteAtivo(id: Int): Long {
        val where = CAMPO_ID + " = " + id

        return heavyGearDB!!.delete(TABELA_ACOES, where, null).toLong()
    }

    fun getAtivo(codigo: String): Ativo {
        val query = "SELECT " +
                " * " +
                "FROM " +
                TABELA_ACOES +
                " WHERE " +
                CAMPO_CODIGO + " = '" + codigo + "' AND " +
                CAMPO_ENABLE + " = 'S'"
        val cursor = heavyGearDB!!.rawQuery(query, null)
        cursor.moveToFirst()
        val ativo = Ativo(cursor.getString(cursor.getColumnIndex(CAMPO_CODIGO)),
                cursor.getString(cursor.getColumnIndex(CAMPO_EMPRESA)),
                cursor.getString(cursor.getColumnIndex(CAMPO_TIPO)),
                0.0)
        return ativo
    }

    val ativos: List<Ativo>
        get() {
            val query = "SELECT * FROM $TABELA_ACOES WHERE $CAMPO_ENABLE = 'S' ORDER BY $CAMPO_TIPO DESC"

            val ativos = ArrayList<Ativo>()

            val cursor = heavyGearDB!!.rawQuery(query, null)

            while (cursor.moveToNext()) {
                ativos.add(Ativo(cursor.getString(cursor.getColumnIndex(CAMPO_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(CAMPO_EMPRESA)),
                        cursor.getString(cursor.getColumnIndex(CAMPO_TIPO)),
                        0.0))
            }

            return ativos
        }

    fun execQuery(query: String): Cursor {
        return heavyGearDB!!.rawQuery(query, null)
    }

    fun pesquisaAtivo(filtro: String): List<Ativo> {
        val ativos = ArrayList<Ativo>()
        if (filtro == "") {
            return ativos
        }

        val query = "SELECT " +
                " * " +
                " FROM " +
                TABELA_ACOES +
                " WHERE " +
                CAMPO_CODIGO + " LIKE '%" + filtro + "%' AND " +
                CAMPO_ENABLE + " = 'S'" +
                " ORDER BY _id ASC"

        val cursor = heavyGearDB!!.rawQuery(query, null)

        while (cursor.moveToNext()) {
            ativos.add(Ativo(cursor.getString(cursor.getColumnIndex(CAMPO_CODIGO)),
                    cursor.getString(cursor.getColumnIndex(CAMPO_EMPRESA)),
                    cursor.getString(cursor.getColumnIndex(CAMPO_TIPO)),
                    0.0))
        }
        return ativos
    }

    companion object {

        val DB_NOME = "heavygear.db"
        val VERSAO = 1

        val TABELA_ACOES = "acoes"

        // Campos comuns
        val CAMPO_ID = "_id"
        val CAMPO_CODIGO = "codigo"

        // Campos tabela ACOES
        val CAMPO_EMPRESA = "empresa"
        val CAMPO_TIPO = "tipo"
        val CAMPO_COTACAO = "cotacao"
        val CAMPO_ENABLE = "enable"
    }
}
