package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by malko on 13-Apr-18.
 */

public class Kategorija implements Parcelable {

    private String naziv;


    public Kategorija(String naziv) {
        this.naziv = naziv;
    }

    protected Kategorija(Parcel in) {
        naziv = in.readString();
    }

    public static final Creator<Kategorija> CREATOR = new Creator<Kategorija>() {
        @Override
        public Kategorija createFromParcel(Parcel in) {
            return new Kategorija(in);
        }

        @Override
        public Kategorija[] newArray(int size) {
            return new Kategorija[size];
        }
    };

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(naziv);
    }
}
