package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by malko on 14-Apr-18.
 */

public class Kontejner implements Parcelable {

    private ArrayList<Knjiga> knjige;
    private ArrayList<Kategorija> kategorije;
    private ArrayList<Autor> autori;
    private ArrayList<Knjiga> knjigeZaSpiner;

    public Kontejner() {
        //knjige = new ArrayList<Knjiga>();
        //kategorije = new ArrayList<Kategorija>();
        //autori = new ArrayList<Autor>();
        //knjigeZaSpiner = new ArrayList<Knjiga>();

        kategorije = KategorijeAkt.db.uzmiKategorijeIzBaze();
        //knjige = new ArrayList<Knjiga>();
        //autori = new ArrayList<Autor>();
        knjige = KategorijeAkt.db.uzmiKnjigeIzBaze();
        autori = KategorijeAkt.db.uzmiAutoreIzBaze();
    }

    protected Kontejner(Parcel in) {
        knjige = in.createTypedArrayList(Knjiga.CREATOR);
        kategorije = in.createTypedArrayList(Kategorija.CREATOR);
        autori = in.createTypedArrayList(Autor.CREATOR);
        knjigeZaSpiner = in.createTypedArrayList(Knjiga.CREATOR);
    }

    public static final Creator<Kontejner> CREATOR = new Creator<Kontejner>() {
        @Override
        public Kontejner createFromParcel(Parcel in) {
            return new Kontejner(in);
        }

        @Override
        public Kontejner[] newArray(int size) {
            return new Kontejner[size];
        }
    };

    public ArrayList<Knjiga> getKnjige() {
        return knjige;
    }

    public ArrayList<Kategorija> getKategorije() {
        return kategorije;
    }

    public ArrayList<String> getAutoriImena() {
        ArrayList<String> imena = new ArrayList<String>();

        for (Autor k: autori)
            imena.add(k.getImeiPrezime());

        return imena;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void dodajKnjigu(Knjiga knjiga) {
        ArrayList<String> idevi = new ArrayList<String>();
        if(knjiga.getNovi()) {
            for (Knjiga k: knjige) {
                idevi.add(k.getId());
            }
            if(idevi.contains(knjiga.getId()))
                return;
            else {
                knjige.add(knjiga);
                long n = KategorijeAkt.db.dodajKnjigu(knjiga);
            }
        }
        else {
            knjige.add(knjiga);
            //KategorijeAkt.db.dodajKnjigu(knjiga);
        }
    }

    public void dodajKategoriju(Kategorija kategorija) {
        kategorije.add(kategorija);
        long nesto = KategorijeAkt.db.dodajKategoriju(kategorija.getNaziv());
        if(nesto == -1)
            Log.i("Obavještenje", "Kategorija nije dodana, došlo je do greške ili kategorija već postoji");
        else
            Log.i("Obavještenje", "Kategorija dodana");
    }

    public void dodajKnjiguZaSpiner(Knjiga knjiga) { knjigeZaSpiner.add(knjiga); }

    public ArrayList<Knjiga> getKnjigeZaSpiner() { return knjigeZaSpiner; }

    public void setKnjigeZaSpiner(ArrayList<Knjiga> knjige) { knjigeZaSpiner = knjige; }

    public void ocistiKnjigeZaSpiner() { knjigeZaSpiner.clear(); }

    public void dodajAutora(Autor autor) {
        for (Autor a: autori) {
            if(a.getImeiPrezime().equals(autor.getImeiPrezime())) {
                a.dodajKnjigu(autor.getKnjige().get(0));
                KategorijeAkt.db.dodajAutorstvo(KategorijeAkt.db.dajIDAutora(a.getImeiPrezime()),
                        KategorijeAkt.db.dajIdDKnjige(autor.getKnjige().get(0)));
                return;
            }
        }

        autori.add(autor);
        long n = KategorijeAkt.db.dodajAutora(autor.getImeiPrezime());
        //KategorijeAkt.db.dodajAutorstvo(13,19);
        Integer i = KategorijeAkt.db.dajIDAutora(autor.getImeiPrezime());
        Integer j = 0;
        if(autor.getPomocni()) return;
            //j = KategorijeAkt.db.dajIdDKnjige2(autor.getKnjige().get(0));
        j = KategorijeAkt.db.dajIdDKnjige(autor.getKnjige().get(0));


        KategorijeAkt.db.dodajAutorstvo(i,j);
    }

    public void dodajAutore(ArrayList<Autor> pautori) {
        if(pautori.size()==0) return;
        else if(pautori.size() == 1) dodajAutora(pautori.get(0));

        else {

            for (Autor a : autori) {
                for (Autor b : pautori) {
                    if (a.getImeiPrezime().equals(b.getImeiPrezime())) {
                        a.dodajKnjigu(b.getKnjige().get(0));
                        KategorijeAkt.db.dodajAutorstvo(KategorijeAkt.db.dajIDAutora(b.getImeiPrezime()),
                                KategorijeAkt.db.dajIdDKnjige(b.getKnjige().get(0)));
                    }

                }
            }

            for (Autor c : pautori) {
                if (!getAutoriImena().contains(c.getImeiPrezime())) {
                    autori.add(c);
                    KategorijeAkt.db.dodajAutora(c.getImeiPrezime());
                    KategorijeAkt.db.dodajAutorstvo(KategorijeAkt.db.dajIDAutora(c.getImeiPrezime()),
                            KategorijeAkt.db.dajIdDKnjige(c.getKnjige().get(0)));
                }

                else continue;
            }
        }
    }

    public void obojiKnjiguKontejner(Knjiga knjiga) {
        boolean nova = knjiga.getNovi();
        if(nova) {
            for (Knjiga k: knjige) {
                if(k.getId().equals(knjiga.getId())) {
                    k.obojiKnjigu();
                    //KategorijeAkt.db.obojiKnjiguUBazi(k.getId());
                }
            }
        }
        else {
            for (Knjiga k: knjige) {
                if(k.getNaslov().equals(knjiga.getNaslov())) {
                    k.obojiKnjigu();
                    //KategorijeAkt.db.obojiKnjiguUBazi2(k.getNaslov());
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(knjige);
        parcel.writeTypedList(kategorije);
        parcel.writeTypedList(autori);
        parcel.writeTypedList(knjigeZaSpiner);
    }
}
