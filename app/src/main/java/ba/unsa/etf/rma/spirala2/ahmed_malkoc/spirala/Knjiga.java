package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by malko on 13-Apr-18.
 */

public class Knjiga implements Parcelable {
    private String autor;
    private String naslov;
    private String kategorija;
    private Bitmap slikaa;
    private Boolean obojen;

    private Boolean novi; //atribut koji ce oznacavati koji konstruktor je pozvan, tj. da li konstruktor koji instancira knjige
    //za potrebe spirale2 ili spirale3

    //novi atributi za potrebe spirale 3
    private String id;
    private String naziv;
    private ArrayList<Autor> autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;

    public Knjiga(String autor, String naslov, String kategorija, Bitmap slikaa) { //konstruktor za funkcionalnosti spirale2
        this.autor = autor;
        this.naslov = naslov;
        this.kategorija = kategorija;
        this.slikaa = slikaa;
        obojen = false;
        autori = new ArrayList<Autor>();
        autori.add(new Autor(autor,naslov));

        novi = false; //pozvan je stari konstruktor pa je novi=false
        //novi atributi
        id = "";
        naziv = "";
        opis = "";
        datumObjavljivanja = "";
        slika = null;
        brojStranica = 0;

    }

    //konstruktor za funckionalnosti spirale3
    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;

        novi = true; //pozvan novi konstruktor pa je novi=true

        //stare parametre moramo deklarisati, ali ih necemo koristiti
        this.autor = "";
        this.naslov = "";
        this.kategorija = "";
        this.slikaa = null;
        this.obojen = false;
    }

    public String getAutor() {
        return autor;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void  setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public void setSlikaa(Bitmap slikaa) {
        this.slikaa= slikaa;
    }

    public Bitmap getSlikaa() {
        return slikaa;
    }

    public Boolean daLijeObojen() {
        return obojen;
    }

    public Boolean getObojen() {
        return obojen;
    }

    public void setObojen(Boolean obojen) {
        this.obojen = obojen;
    }

    public Boolean getNovi() {
        return novi;
    }

    public void setNovi(Boolean novi) {
        this.novi = novi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public Boolean daLiSadrziAutora(String pautor) {
        if(pautor == autor) return true;
        if(autori.size() == 0) return false;

        for (Autor a: autori) {
            if(a.getImeiPrezime().equals(pautor))
                return true;
        }

        return false;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public static Creator<Knjiga> getCREATOR() {
        return CREATOR;
    }

    public void obojiKnjigu() {
        obojen = true;
    }

    protected Knjiga(Parcel in) {
        autor = in.readString();
        naslov = in.readString();
        kategorija = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(autor);
        dest.writeString(naslov);
        dest.writeString(kategorija);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Knjiga> CREATOR = new Parcelable.Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };
}
