package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by malko on 30-May-18.
 */

public class BazaOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MojaBaza.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_KATEGORIJA = "Kategorija";
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";


    public static final String TABLE_KNJIGA = "Knjiga";
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUMOBJAVLJIVANJA = "datumObjavljivanja";
    public static final String KNJIGA_BROJSTRANICA = "brojStranica";
    public static final String KNJIGA_IDWEBSERVIS = "idWebServis";
    public static final String KNJIGA_IDKATEGORIJE = "idkategorije";
    public static final String KNJIGA_SLIKA = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";


    public static final String TABLE_AUTOR = "Autor";
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";


    public static final String TABLE_AUTORSTVO = "Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_IDAUTORA = "idautora";
    public static final String AUTORSTVO_IDKNJIGE = "idknjige";


    private static final String CREATE_TABLE_KATEGORIJA = "create table "
            + TABLE_KATEGORIJA + " ("
            + KATEGORIJA_ID + " integer primary key autoincrement,"
            + KATEGORIJA_NAZIV + " text not null unique);";


    private static final String CREATE_TABLE_KNJIGA = "create table "
            + TABLE_KNJIGA + " ("
            + KNJIGA_ID + " integer primary key autoincrement,"
            + KNJIGA_NAZIV + " text not null,"
            + KNJIGA_OPIS + " text,"
            + KNJIGA_DATUMOBJAVLJIVANJA + " text,"
            + KNJIGA_BROJSTRANICA + " integer,"
            + KNJIGA_IDWEBSERVIS + " text,"
            + KNJIGA_IDKATEGORIJE + " integer not null,"
            + KNJIGA_SLIKA + " text,"
            + KNJIGA_PREGLEDANA + " integer not null);";


    private static final String CREATE_TABLE_AUTOR = "create table "
            + TABLE_AUTOR + " ("
            + AUTOR_ID + " integer primary key autoincrement,"
            + AUTOR_IME + " text not null);";


    private static final String CREATE_TABLE_AUTORSTVO = "create table "
            + TABLE_AUTORSTVO + " ("
            + AUTORSTVO_ID + " integer primary key autoincrement,"
            + AUTORSTVO_IDAUTORA + " integer not null,"
            + AUTORSTVO_IDKNJIGE + " integer not null);";


    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KATEGORIJA);
        db.execSQL(CREATE_TABLE_KNJIGA);
        db.execSQL(CREATE_TABLE_AUTOR);
        db.execSQL(CREATE_TABLE_AUTORSTVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIJA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KNJIGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTORSTVO);
        onCreate(db);
    }

    private void pobrisi() {
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("DELETE FROM " + TABLE_KNJIGA);
        db.execSQL("DELETE FROM " + TABLE_AUTOR);
        db.execSQL("DELETE FROM " + TABLE_KATEGORIJA);
        db.execSQL("DELETE FROM " + TABLE_AUTORSTVO);
    }

    public long dodajKategoriju(String naziv) throws SQLException {
        long kategorija_id = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues category = new ContentValues();
            category.put(KATEGORIJA_NAZIV, naziv);

            kategorija_id = db.insertOrThrow(TABLE_KATEGORIJA, null, category);
        }
        catch (SQLiteConstraintException e){
            e.getStackTrace();
            kategorija_id = -1;
        }
        return kategorija_id;
    }

    public long dodajKnjigu(Knjiga knjiga) throws SQLException {
        long knjiga_id = 0;

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues book = new ContentValues();
            int obojena = 0;
            if(knjiga.getObojen())
                obojena = 1;

            if(knjiga.getNovi()) {
                book.put(KNJIGA_NAZIV, knjiga.getNaziv());
                book.put(KNJIGA_OPIS, knjiga.getOpis());
                book.put(KNJIGA_DATUMOBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
                book.put(KNJIGA_BROJSTRANICA, knjiga.getBrojStranica());
                book.put(KNJIGA_IDWEBSERVIS, knjiga.getId());
                book.put(KNJIGA_IDKATEGORIJE, dajIDKategorije(knjiga.getKategorija()));
                book.put(KNJIGA_SLIKA, knjiga.getSlika().toString());
                book.put(KNJIGA_PREGLEDANA, obojena);
            }
            else {
                book.put(KNJIGA_NAZIV, knjiga.getNaslov());
                book.put(KNJIGA_OPIS, "");
                book.put(KNJIGA_DATUMOBJAVLJIVANJA, "");
                book.put(KNJIGA_BROJSTRANICA, -1);
                book.put(KNJIGA_IDWEBSERVIS, "");
                book.put(KNJIGA_IDKATEGORIJE, dajIDKategorije(knjiga.getKategorija()));
                book.put(KNJIGA_SLIKA, "");
                book.put(KNJIGA_PREGLEDANA, obojena);
            }

                knjiga_id = db.insertOrThrow(TABLE_KNJIGA, null, book);

        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            knjiga_id = -1;
        }
        return knjiga_id;
    }

    public long dodajAutora(String ime) throws SQLException {
        long autor_id = 0;

        try {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues autor = new ContentValues();

            autor.put(AUTOR_IME, ime);
            autor_id = db.insertOrThrow(TABLE_AUTOR, null, autor);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            autor_id = -1;
        }
        return autor_id;
    }

    public long dodajAutorstvo(Integer idAutora, Integer idKnjige) throws SQLException {
        long autorstvo_id = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues autorstvo = new ContentValues();

            autorstvo.put(AUTORSTVO_IDAUTORA, idAutora);
            autorstvo.put(AUTORSTVO_IDKNJIGE, idKnjige);

            autorstvo_id = db.insertOrThrow(TABLE_AUTORSTVO, null, autorstvo);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            autorstvo_id = -1;
        }

        return autorstvo_id;
    }

    public boolean imaLiKategorije(String kategorija) {
        SQLiteDatabase db = getReadableDatabase();

        String sql =
                "SELECT " + KATEGORIJA_NAZIV +
                " FROM "+ TABLE_KATEGORIJA +
                " WHERE " + KATEGORIJA_NAZIV + " = '" + kategorija + "'";
        Cursor rezultat = db.rawQuery(sql, null);
        rezultat.moveToFirst();
        if(rezultat.isBeforeFirst())
            return false;

        return true;
    }

    public boolean imaLiAutora(String autor) {
        SQLiteDatabase db = getReadableDatabase();

        String query =
                "SELECT " + AUTOR_IME +
                        " FROM "+ TABLE_AUTOR +
                        " WHERE " + AUTOR_IME + " = '" + autor + "'";

        Cursor rezultat = db.rawQuery(query, null);
        rezultat.moveToFirst();
        if(rezultat.isBeforeFirst())
            return false;

        return true;
    }

    public ArrayList<Kategorija> uzmiKategorijeIzBaze() {
        ArrayList<Kategorija> kategorije = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        //pobrisi();

        String query =
                "SELECT " + KATEGORIJA_NAZIV +
                        " FROM " + TABLE_KATEGORIJA + ";";

        Cursor rezultat = db.rawQuery(query, null);
        rezultat.moveToFirst();

        if(rezultat.getCount() == 0) return kategorije;
        int indeks = rezultat.getColumnIndexOrThrow(KATEGORIJA_NAZIV);

        if (rezultat != null) {
            while (!rezultat.isAfterLast()) {
                kategorije.add(new Kategorija(rezultat.getString(indeks)));

                rezultat.moveToNext();
            }
        }

        rezultat.close();
        return kategorije;
    }

    public ArrayList<Knjiga> uzmiKnjigeIzBaze() {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query =
                "SELECT * FROM " + TABLE_KNJIGA + ";";

        Cursor rezultat = db.rawQuery(query, null);
        rezultat.moveToFirst();
        if(rezultat.getCount() == 0)
            return knjige;

        while (!rezultat.isAfterLast()) {
            String naziv = rezultat.getString(rezultat.getColumnIndex(KNJIGA_NAZIV));
            String kategorija = dajNazivKategorije(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_IDKATEGORIJE)));
            Integer pregledana = rezultat.getInt(rezultat.getColumnIndex(KNJIGA_PREGLEDANA));
            boolean pr = true;
            if (pregledana == 0) pr = false;

            String idWeb = rezultat.getString(rezultat.getColumnIndex(KNJIGA_IDWEBSERVIS));
            if (idWeb.length() > 0) {
                String opis = rezultat.getString(rezultat.getColumnIndex(KNJIGA_OPIS));
                String datum = rezultat.getString(rezultat.getColumnIndex(KNJIGA_DATUMOBJAVLJIVANJA));
                String url = rezultat.getString(rezultat.getColumnIndex(KNJIGA_SLIKA));
                Integer brojStr = rezultat.getInt(rezultat.getColumnIndex(KNJIGA_BROJSTRANICA));
                try {
                    URL slika = new URL(rezultat.getString(rezultat.getColumnIndex(KNJIGA_SLIKA)));
                    ArrayList<Autor> autori = vratiAutoreNaOsnovuIDKnjige(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_ID)));
                    Knjiga knjiga = new Knjiga(idWeb, naziv, autori, opis, datum, slika, brojStr);
                    knjiga.setKategorija(kategorija);
                    knjiga.setObojen(pr);
                    knjige.add(knjiga);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else {
                ArrayList<Autor> autori = vratiAutoreNaOsnovuIDKnjige(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_ID)));
                String autor = "";
                if(autori.size() > 0)
                    autor = autori.get(0).getImeiPrezime();

                Knjiga knjiga = new Knjiga(autor, naziv, kategorija, null);
                knjiga.setObojen(pr);
                knjige.add(knjiga);
            }

            rezultat.moveToNext();
        }


        rezultat.close();

        return knjige;
    }

    public ArrayList<Autor> uzmiAutoreIzBaze() {
        ArrayList<Autor> autori = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query1 =
                "SELECT * FROM " + TABLE_AUTOR + ";";

        Cursor rezultatAutor = db.rawQuery(query1, null);
        rezultatAutor.moveToFirst();
        if(rezultatAutor.getCount() == 0)
            return autori;

        while (!rezultatAutor.isAfterLast()) {
            String ime = rezultatAutor.getString(rezultatAutor.getColumnIndex(AUTOR_IME));
            Integer id = rezultatAutor.getInt(rezultatAutor.getColumnIndex(AUTOR_ID));

            String query2 =
                    "SELECT " + AUTORSTVO_IDKNJIGE +
                            " FROM " + TABLE_AUTORSTVO +
                            " WHERE " + AUTORSTVO_IDAUTORA + " = '" + id.toString() + "';";
            Cursor rezultatAutorstvo = db.rawQuery(query2, null);
            rezultatAutorstvo.moveToFirst();
            ArrayList<String> ideviWS = new ArrayList<>();
            while(!rezultatAutorstvo.isAfterLast()) {
                ideviWS.add(dajIDwebServisa(rezultatAutorstvo.getInt(rezultatAutorstvo.getColumnIndex(AUTORSTVO_IDKNJIGE))));
                rezultatAutorstvo.moveToNext();
            }

            rezultatAutorstvo.close();

            Autor a = new Autor(ime, ideviWS.get(0));
            if(ideviWS.size() > 1)
                for(int i=1; i<ideviWS.size(); i++)
                    a.dodajKnjigu(ideviWS.get(i));

            autori.add(a);

            rezultatAutor.moveToNext();
        }
        rezultatAutor.close();


        return autori;


    }

    public long dajIDKategorije(String kategorija) {
        Integer id = 0;

        SQLiteDatabase db = getReadableDatabase();

        String query =
                "SELECT " + KATEGORIJA_ID +
                        " FROM " + TABLE_KATEGORIJA +
                        " WHERE " + KATEGORIJA_NAZIV + " = '" + kategorija + "';";

        Cursor rezultat = db.rawQuery(query, null);
        int indeks = rezultat.getColumnIndexOrThrow(KATEGORIJA_ID);
        rezultat.moveToFirst();
        if(rezultat != null)
            id = rezultat.getInt(indeks);

        rezultat.close();
        return id;
    }

    public String dajNazivKategorije(Integer id) {
        //String naziv = "";
        SQLiteDatabase db = getReadableDatabase();

        String query =
                "SELECT " + KATEGORIJA_NAZIV +
                        " FROM " + TABLE_KATEGORIJA +
                        " WHERE " + KATEGORIJA_ID + " = '" + id.toString() + "';";

        Cursor rezultat = db.rawQuery(query, null);
        int indeks = rezultat.getColumnIndexOrThrow(KATEGORIJA_NAZIV);
        rezultat.moveToFirst();

        String naziv = rezultat.getString(indeks);

        rezultat.close();
        return naziv;
    }

    public ArrayList<Knjiga> knjigeKategorije(long idKategorije) {
        ArrayList<Knjiga> knjige = new ArrayList<>();
        Long pom = new Long(idKategorije);
        Integer id = Integer.valueOf(pom.intValue());
        SQLiteDatabase db = getReadableDatabase();
        String query =
                "SELECT * FROM " + TABLE_KNJIGA +
                        " WHERE " + KNJIGA_IDKATEGORIJE + " = '" + id.toString() + "';";
        Cursor rezultat = db.rawQuery(query, null);
        rezultat.moveToFirst();
        if(rezultat != null) {
            while(!rezultat.isAfterLast()) {
                String naziv = rezultat.getString(rezultat.getColumnIndex(KNJIGA_NAZIV));
                String kategorija = dajNazivKategorije(id);
                int pregledana = rezultat.getInt(rezultat.getColumnIndex(KNJIGA_PREGLEDANA));
                boolean pr = true;
                if(pregledana == 0) pr = false;

                String idWeb = rezultat.getString(rezultat.getColumnIndex(KNJIGA_IDWEBSERVIS));
                if(idWeb != null) {
                    String opis = rezultat.getString(rezultat.getColumnIndex(KNJIGA_OPIS));
                    String datum = rezultat.getString(rezultat.getColumnIndex(KNJIGA_DATUMOBJAVLJIVANJA));
                    String url = rezultat.getString(rezultat.getColumnIndex(KNJIGA_SLIKA));
                    int brojStr = rezultat.getInt(rezultat.getColumnIndex(KNJIGA_BROJSTRANICA));
                    try {
                        URL slika = new URL(url);
                        ArrayList<Autor> autori = vratiAutoreNaOsnovuIDKnjige(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_ID)));
                        Knjiga knjiga = new Knjiga(idWeb, naziv, autori, opis, datum, slika , brojStr);
                        knjiga.setKategorija(kategorija);
                        knjiga.setObojen(pr);
                        knjige.add(knjiga);
                    } catch (MalformedURLException e) {}
                }
                else {
                    Knjiga knjiga = new Knjiga("", naziv, kategorija, null);
                    knjiga.setObojen(pr);
                    knjige.add(knjiga);
                }

                rezultat.moveToNext();
            }
        }

        return knjige;
    }

    public String dajImeAutora(Integer autor_id) {
        SQLiteDatabase db = getReadableDatabase();
        String ime = "";
        String query =
                "SELECT " + AUTOR_IME +
                        " FROM " + TABLE_AUTOR +
                        " WHERE " + AUTOR_ID + " = '" + autor_id.toString() + "';";

        Cursor rezultat = db.rawQuery(query,null);
        rezultat.moveToFirst();
        if(rezultat != null)
            ime = rezultat.getString(rezultat.getColumnIndex(AUTOR_IME));

        rezultat.close();
        return ime;
    }

    public Integer dajIdDKnjige(String idWebServis) {
        Integer id = 0;

        SQLiteDatabase db = getReadableDatabase();

        String query1 =
                "SELECT " + KNJIGA_ID +
                        " FROM " + TABLE_KNJIGA +
                        " WHERE " + KNJIGA_IDWEBSERVIS + " = '" + idWebServis + "';";
        Cursor rezultatKnjigaId = db.rawQuery(query1, null);
        rezultatKnjigaId.moveToFirst();
        if(rezultatKnjigaId != null)
            id = rezultatKnjigaId.getInt(rezultatKnjigaId.getColumnIndex(KNJIGA_ID));

        rezultatKnjigaId.close();
        return id;
    }

    public Integer dajIdDKnjige2(String naziv) {
        Integer id = 0;

        SQLiteDatabase db = getReadableDatabase();

        String query1 =
                "SELECT " + KNJIGA_ID +
                        " FROM " + TABLE_KNJIGA +
                        " WHERE " + KNJIGA_NAZIV + " = '" + naziv + "';";
        Cursor rezultatKnjigaId = db.rawQuery(query1, null);
        rezultatKnjigaId.moveToFirst();
        if(rezultatKnjigaId != null)
            id = rezultatKnjigaId.getInt(rezultatKnjigaId.getColumnIndex(KNJIGA_ID));

        rezultatKnjigaId.close();
        return id;
    }

    public Integer dajIDAutora(String ime) {
        Integer id = 0;

        SQLiteDatabase db = getReadableDatabase();

        String query1 =
                "SELECT " + AUTOR_ID +
                        " FROM " + TABLE_AUTOR +
                        " WHERE " + AUTOR_IME + " = '" + ime + "';";
        Cursor rezultatAutorId = db.rawQuery(query1, null);
        rezultatAutorId.moveToFirst();
        if(rezultatAutorId != null)
            id = rezultatAutorId.getInt(rezultatAutorId.getColumnIndex(AUTOR_ID));

        rezultatAutorId.close();
        return id;
    }

    public String dajIDwebServisa(Integer knjiga_id) {
        SQLiteDatabase db = getReadableDatabase();

        //uzima se string id sa web servisa za knjigu sa primljenim id-em
        String query1 =
                "SELECT " + KNJIGA_IDWEBSERVIS +
                        " FROM " + TABLE_KNJIGA +
                        " WHERE " + KNJIGA_ID + " = '"+ knjiga_id.toString() + "';";
        Cursor rezultatKnjigaId = db.rawQuery(query1, null);
        rezultatKnjigaId.moveToFirst();
        int indeks = rezultatKnjigaId.getColumnIndexOrThrow(KNJIGA_IDWEBSERVIS);
        String idWebServis = rezultatKnjigaId.getString(indeks);

        rezultatKnjigaId.close();
        return idWebServis;
    }

    private class PomocnaAutorstvo {
        Integer _id;
        Integer idAutora;
        Integer idKnjige;

        public PomocnaAutorstvo(Integer _id, Integer idAutora, Integer idKnjige) {
            this._id = _id;
            this.idAutora = idAutora;
            this.idKnjige = idKnjige;
        }

        public Integer get_id() {
            return _id;
        }

        public void set_id(Integer _id) {
            this._id = _id;
        }

        public Integer getIdAutora() {
            return idAutora;
        }

        public void setIdAutora(Integer idAutora) {
            this.idAutora = idAutora;
        }

        public Integer getIdKnjige() {
            return idKnjige;
        }

        public void setIdKnjige(Integer idKnjige) {
            this.idKnjige = idKnjige;
        }
    }

    public ArrayList<Autor> vratiAutoreNaOsnovuIDKnjige(Integer knjiga_id) {
        ArrayList<Autor> autori = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        //uzimaju se slogovi iz tabele autorstvo koji imaju knjiga_id
        String query1 =
                "SELECT * FROM " + TABLE_AUTORSTVO +
                        " WHERE " + AUTORSTVO_IDKNJIGE + " = '" + knjiga_id.toString() + "';";
        Cursor rezultatAutorstvo = db.rawQuery(query1, null);
        ArrayList<PomocnaAutorstvo> sviAutorstvo = new ArrayList<>();

        //ako je rezultat upita prazan, vratit ce se arraylist autori koji nema nijednog autora
        if (rezultatAutorstvo.getCount() == 0)
            return autori;


        rezultatAutorstvo.moveToFirst();
        while(!rezultatAutorstvo.isAfterLast()) {
            sviAutorstvo.add(new PomocnaAutorstvo((rezultatAutorstvo.getInt(rezultatAutorstvo.getColumnIndex(AUTORSTVO_ID))),
                    rezultatAutorstvo.getInt(rezultatAutorstvo.getColumnIndex(AUTORSTVO_IDAUTORA)),
                    rezultatAutorstvo.getInt(rezultatAutorstvo.getColumnIndex(AUTORSTVO_IDKNJIGE))));
            rezultatAutorstvo.moveToNext();
        }

        for (PomocnaAutorstvo p: sviAutorstvo) {
            Autor a = new Autor(dajImeAutora(p.getIdAutora()), dajIDwebServisa(knjiga_id));

            String query2 =
                    "SELECT " + AUTORSTVO_IDKNJIGE +
                            " FROM " + TABLE_AUTORSTVO +
                            " WHERE " + AUTORSTVO_IDAUTORA + " = '" + p.getIdAutora().toString() + "';";
            Cursor rez = db.rawQuery(query2, null);
            rez.moveToFirst();
            ArrayList<String> ids = new ArrayList<>();

            while(!rez.isAfterLast()) {
                ids.add(dajIDwebServisa(rez.getInt(rez.getColumnIndex(AUTORSTVO_IDKNJIGE))));
                rez.moveToNext();
            }

            for (String s: ids) {
                a.dodajKnjigu(s);
            }

            autori.add(a);
        }
        return autori;
    }

    public ArrayList<Knjiga> knjigeAutora(long idAutora) {
        ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
        SQLiteDatabase db = getReadableDatabase();

        Long l = new Long(idAutora);
        Integer i = Integer.valueOf(l.intValue());
        String query1 =
                "SELECT " + AUTORSTVO_IDKNJIGE +
                        " FROM " + TABLE_AUTORSTVO +
                        " WHERE " + AUTORSTVO_IDAUTORA + " = '" + i.toString() + "';";

        Cursor rez = db.rawQuery(query1, null);
        rez.moveToFirst();

        if(rez.getCount() == 0) return knjige;

        while (!rez.isAfterLast()) {
            Integer id = rez.getInt(rez.getColumnIndex(AUTORSTVO_IDKNJIGE));
            String query2 =
                    "SELECT * FROM " + TABLE_KNJIGA +
                            " WHERE " + KNJIGA_ID + " = '" + id.toString() + "';";

            Cursor rezultat = db.rawQuery(query2, null);
            rezultat.moveToFirst();

            String naziv = rezultat.getString(rezultat.getColumnIndex(KNJIGA_NAZIV));
            String kategorija = dajNazivKategorije(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_IDKATEGORIJE)));
            Integer pregledana = rezultat.getInt(rezultat.getColumnIndex(KNJIGA_PREGLEDANA));
            boolean pr = true;
            if (pregledana == 0) pr = false;

            String idWeb = rezultat.getString(rezultat.getColumnIndex(KNJIGA_IDWEBSERVIS));
            if (idWeb.length() > 0) {
                String opis = rezultat.getString(rezultat.getColumnIndex(KNJIGA_OPIS));
                String datum = rezultat.getString(rezultat.getColumnIndex(KNJIGA_DATUMOBJAVLJIVANJA));
                String url = rezultat.getString(rezultat.getColumnIndex(KNJIGA_SLIKA));
                Integer brojStr = rezultat.getInt(rezultat.getColumnIndex(KNJIGA_BROJSTRANICA));
                try {
                    URL slika = new URL(rezultat.getString(rezultat.getColumnIndex(KNJIGA_SLIKA)));
                    ArrayList<Autor> autori = vratiAutoreNaOsnovuIDKnjige(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_ID)));
                    Knjiga knjiga = new Knjiga(idWeb, naziv, autori, opis, datum, slika, brojStr);
                    knjiga.setKategorija(kategorija);
                    knjiga.setObojen(pr);
                    knjige.add(knjiga);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else {
                ArrayList<Autor> autori = vratiAutoreNaOsnovuIDKnjige(rezultat.getInt(rezultat.getColumnIndex(KNJIGA_ID)));
                String autor = "";
                if(autori.size() > 0)
                    autor = autori.get(0).getImeiPrezime();

                Knjiga knjiga = new Knjiga(autor, naziv, kategorija, null);
                knjiga.setObojen(pr);
                knjige.add(knjiga);
            }

            rez.moveToNext();
        }

        return knjige;
    }

    public void obojiKnjiguUBazi(String idWeb) {
        SQLiteDatabase db = getWritableDatabase();

        int pregledana = 1;
        ContentValues updated = new ContentValues();
        updated.put(KNJIGA_PREGLEDANA, pregledana);
        String where = KNJIGA_IDWEBSERVIS + " = " + "WviNfB2q_oEC";
        String whereArgs[] = null;
        db.update(TABLE_KNJIGA, updated, KNJIGA_IDWEBSERVIS + " = " + idWeb , whereArgs);
    }

    public void obojiKnjiguUBazi2(String naslov) {
        SQLiteDatabase db = getWritableDatabase();

        int pregledana = 1;
        ContentValues updated = new ContentValues();
        updated.put(KNJIGA_PREGLEDANA, pregledana);
        String where = KNJIGA_NAZIV + " = " + naslov;
        String whereArgs[] = null;
        db.update(TABLE_KNJIGA, updated, where, whereArgs);
    }
}
