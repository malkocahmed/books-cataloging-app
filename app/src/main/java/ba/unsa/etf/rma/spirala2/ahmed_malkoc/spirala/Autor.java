package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by malko on 14-May-18.
 */

public class Autor implements Parcelable {
    /*Klasa Autor treba da sadrži sljedeće atribute: imeiPrezime tipa string i knjige tipa
ArrayList<string> (klasa treba da ima konstruktor koji prima imeiPrezime autora te id
knjige koji će se dodati u niz knjige, potrebno je implementirati gettere i settere kao i
metodu dodajKnjigu(string id) koja dodaje dati id u listu knjige (ako već ne postoji)*/

    private String imeiPrezime;
    private ArrayList<String> knjige;
    private boolean pomocni;

    public Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        knjige = new ArrayList<String>();
        knjige.add(id);
        pomocni=false;
    }

    public void setPomocni() {
        pomocni = true;
    }
    public boolean getPomocni() {
        return pomocni;
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String id) {
        if(!knjige.contains(id)) {
            knjige.add(id);
        }
        else return;
    }


    protected Autor(Parcel in) {
        imeiPrezime = in.readString();
        knjige = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imeiPrezime);
        dest.writeStringList(knjige);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Autor> CREATOR = new Creator<Autor>() {
        @Override
        public Autor createFromParcel(Parcel in) {
            return new Autor(in);
        }

        @Override
        public Autor[] newArray(int size) {
            return new Autor[size];
        }
    };

}
