package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.security.keystore.KeyNotYetValidException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.OnItemClick, MyResultReceiver.Receiver {

    private ArrayList<Kategorija> kategorije;
    private ArrayList<Knjiga> knjige;
    private Kontejner container;
    private Boolean siriL = false;

    private int KONTROLA;

    public void setKONTROLA(int i) { KONTROLA = i; }
    public int getKONTROLA() { return KONTROLA; }

    FragmentManager fm = getFragmentManager();
    public static BazaOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_kategorijeakt);

        db = new BazaOpenHelper(this, BazaOpenHelper.DATABASE_NAME, null, BazaOpenHelper.DATABASE_VERSION);
        //this.deleteDatabase(BazaOpenHelper.DATABASE_NAME);

        knjige = new ArrayList<Knjiga>();
        kategorije = new ArrayList<Kategorija>();
        KONTROLA = 0;

        container = new Kontejner();

        /*container.dodajKategoriju(new Kategorija("Romani"));
        container.dodajKategoriju(new Kategorija("Zbirke pjesama"));
        container.dodajKategoriju(new Kategorija("Enciklopedije"));
        container.dodajKategoriju(new Kategorija("Rječnici"));
        container.dodajKategoriju(new Kategorija("Geografske karte"));*/



        //final FragmentManager fm = getFragmentManager();


        FrameLayout ldetalji = (FrameLayout) findViewById (R.id.mjesto2);

        if(ldetalji != null) {
            siriL = true;

            //dodavanje fragmenta u lijevi frame layout (landscape)
            ListeFragment lf;

            lf = new ListeFragment();
            fm.beginTransaction().replace(R.id.mjesto1, lf).commit();


            //dodavanje fragmenta u desni frame layout
            KnjigeFragment kf;
            kf = (KnjigeFragment)fm.findFragmentById(R.id.mjesto2);
            kf = new KnjigeFragment();

            fm.beginTransaction().replace(R.id.mjesto2, kf).commit();

        }


        ListeFragment lf = (ListeFragment) fm.findFragmentByTag("listef");
        if(lf == null) {
            lf = new ListeFragment();
            //Bundle puni = new Bundle();
            //puni.putParcelableArrayList("kategorije", kategorije);
            //lf.setArguments(puni);

            fm.beginTransaction().replace(R.id.liste, lf, "listef").commit();
        }
        else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) super.onBackPressed();
        else getFragmentManager().popBackStack();
    }

    @Override
    public void onItemClicked(int pos, View view) {

        KnjigeFragment kf = new KnjigeFragment();

        if(getKONTROLA() == 1) {
            Bundle kliknuta = new Bundle();
            kliknuta.putParcelable("autor", getMyData().getAutori().get(pos));
            kf.setArguments(kliknuta);
        }
        else if(getKONTROLA() == 2) {
            Bundle kliknuta = new Bundle();
            kliknuta.putParcelable("kategorija", getMyData().getKategorije().get(pos));
            kf.setArguments(kliknuta);
        }

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
            getFragmentManager().beginTransaction().replace(R.id.liste, kf).addToBackStack(null).commit();
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            getFragmentManager().beginTransaction().replace(R.id.mjesto2, kf).addToBackStack(null).commit();

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case KnjigePoznanika.STATUS_START:
                break;
            case KnjigePoznanika.STATUS_FINISHED:

                ArrayList<Knjiga> pom = resultData.getParcelableArrayList("listaKnjiga");
                this.setKnjigeOnline(pom);

                FragmentOnline fo = (FragmentOnline) fm.findFragmentById(R.id.liste);

                if(fo != null)
                    fo.ucitavanje(pom);
                else
                    Log.d("onReceiveResult", "Fragment list is null");

                break;
            case KnjigePoznanika.STATUS_ERROR:
                Log.d("ERROR", "Došlo je do greške!");

                break;
        }
    }

    public Kontejner getMyData() {
        return container;
    }

    public void addBook(Knjiga knjiga) {
        container.dodajKnjigu(knjiga);
    }

    public void addCategory(Kategorija kategorija) {
        container.dodajKategoriju(kategorija);
    }

    public void addAuthor(Autor autor) { container.dodajAutora(autor); }

    public void addAuthors(ArrayList<Autor> autori) { container.dodajAutore(autori); }

    public void setKnjigeOnline(ArrayList<Knjiga> knjige) { container.setKnjigeZaSpiner(knjige); }

    public void obojiKnjiguAkt(Knjiga knjiga) { container.obojiKnjiguKontejner(knjiga); }
}
