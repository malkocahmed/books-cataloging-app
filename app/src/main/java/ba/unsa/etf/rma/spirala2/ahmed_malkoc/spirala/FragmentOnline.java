package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;

import java.util.ArrayList;


public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone {

    private Spinner kSpiner;
    private ArrayList<Knjiga> knjigeS;


    public FragmentOnline() {

    }

    public void ucitavanje(ArrayList<Knjiga> knjige) {
        knjigeS = knjige;
        ArrayList<String> imena = new ArrayList<String>();
        for (Knjiga k: knjige) {
            imena.add(k.getNaziv());
        }

        ArrayAdapter<String> adapterSpinerKnjige = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, imena);
        kSpiner.setAdapter(adapterSpinerKnjige);

    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> knjige) {
        knjigeS = knjige;
        ArrayList<String> imena = new ArrayList<String>();
        for (Knjiga k: knjige) {
            imena.add(k.getNaziv());
        }

        ArrayAdapter<String> adapterSpinerKnjige = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, imena);
        kSpiner.setAdapter(adapterSpinerKnjige);

    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> knjige) {
        knjigeS = knjige;
        ArrayList<String> imena = new ArrayList<String>();
        for (Knjiga k: knjige) {
            imena.add(k.getNaziv());
        }

        ArrayAdapter<String> adapterSpinerKnjige = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, imena);
        kSpiner.setAdapter(adapterSpinerKnjige);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_online, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        final FragmentManager fm = getFragmentManager();
        KategorijeAkt aktivnost = (KategorijeAkt)getActivity();
        ArrayList<String> imenaKategorija = new ArrayList<String>();
        for (Kategorija k: aktivnost.getMyData().getKategorije()) {
            imenaKategorija.add(k.getNaziv());
        }

        final Spinner kategorijeSpiner = (Spinner) view.findViewById(R.id.sKategorije);
        final ArrayAdapter<String> kategorijeAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_dropdown_item,
                imenaKategorija);
        kategorijeSpiner.setAdapter(kategorijeAdapter);

        Button dugmeRun = (Button) view.findViewById(R.id.dRun);
        Button dugmeDodajKnjiguOnline = (Button) view.findViewById(R.id.dAdd);
        Button dugmePovratak = (Button) view.findViewById(R.id.dPovratak);
        final EditText tekstUpit = (EditText) view.findViewById(R.id.tekstUpit);
        final TextView t = (TextView) view.findViewById(R.id.tekstviewKategorije);
        kSpiner = (Spinner)view.findViewById(R.id.sRezultat);

        dugmeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = tekstUpit.getText().toString();


                if(s.contains("autor:") && s.substring(0,6).equals("autor:"))
                    new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(s.substring(6));
                else if(s.contains("korisnik:") && s.substring(0,9).equals("korisnik:")) {
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                    intent.putExtra("query", s.substring(9));
                    MyResultReceiver mReceiver = new MyResultReceiver(new Handler());
                    mReceiver.setReceiver((MyResultReceiver.Receiver) getActivity());
                    intent.putExtra("receiver", mReceiver);
                    getActivity().startService(intent);
                }
                else new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(s);
            }
        });

        dugmeDodajKnjiguOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KategorijeAkt aktivnost = (KategorijeAkt) getActivity();

                for (Knjiga k: knjigeS) {
                    if(k.getNaziv().equals(kSpiner.getSelectedItem().toString())) {

                        Knjiga pomocna = k;
                        pomocna.setKategorija(kategorijeSpiner.getSelectedItem().toString());
                        aktivnost.addBook(pomocna);
                        aktivnost.addAuthors(pomocna.getAutori());
                        break;
                    }
                }

            }
        });

        dugmePovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListeFragment lf = new ListeFragment();
                fm.beginTransaction().replace(R.id.liste, lf).commit();
            }
        });

    }
}
