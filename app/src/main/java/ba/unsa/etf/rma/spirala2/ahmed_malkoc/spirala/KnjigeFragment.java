package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class KnjigeFragment extends Fragment {

    public KnjigeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_knjige, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final KategorijeAkt aktivnost = (KategorijeAkt) getActivity();

        Boolean daLiJeAutor = false;

        Kategorija kliknuta = new Kategorija("neka");
        Autor kliknuti = new Autor("neki","nemaknjiga");


        long idKategorije = 0;
        long idAutora = 0;

        if(getArguments().containsKey("kategorija")) {
            Kategorija pom = getArguments().getParcelable("kategorija");
            String kat = pom.getNaziv();
            idKategorije = KategorijeAkt.db.dajIDKategorije(kat);
            /*
            Kategorija pom = getArguments().getParcelable("kategorija");
            kliknuta.setNaziv(pom.getNaziv());*/
        }
        else if (getArguments().containsKey("autor")) {
            Autor pom = getArguments().getParcelable("autor");
            idAutora = KategorijeAkt.db.dajIDAutora(pom.getImeiPrezime());
            /*kliknuti.setImeiPrezime(pom.getImeiPrezime());
            kliknuti.setKnjige(pom.getKnjige());*/
            daLiJeAutor = true;
        }

        ArrayList<Knjiga> knjige = aktivnost.getMyData().getKnjige();
        ArrayList<Knjiga> filtrirane = new ArrayList<Knjiga>();

        if(daLiJeAutor == true) {
            filtrirane = KategorijeAkt.db.knjigeAutora(idAutora);
            /*for (Knjiga k: knjige) {
                if(k.daLiSadrziAutora(kliknuti.getImeiPrezime())) {
                    filtrirane.add(k);
                }

            }*/

        }
        else {
            filtrirane = KategorijeAkt.db.knjigeKategorije(idKategorije);
            /*for (Knjiga k: knjige) {
                if(k.getKategorija().equals(kliknuta.getNaziv())) {
                    filtrirane.add(k);
                }
            }*/

        }

        Button dugmePovratak1 = (Button)view.findViewById(R.id.dPovratak1);
        ListView lista = (ListView)view.findViewById(R.id.listaKnjiga);
        KnjigaAdapter adapterK = new KnjigaAdapter(getActivity(), filtrirane);
        lista.setAdapter(adapterK);

        dugmePovratak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                ListeFragment lf = new ListeFragment();

                fm.beginTransaction().replace(R.id.liste, lf).commit();
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(0xffaabbed);

                Knjiga knjiga = (Knjiga)adapterView.getItemAtPosition(i);
                //((KategorijeAkt) getActivity()).obojiKnjiguAkt(knjiga);

                for (Knjiga k: aktivnost.getMyData().getKnjige()) {
                    if(k.getNaslov().equals(knjiga.getNaslov())) {
                        k.obojiKnjigu();
                        /*if(k.getNovi())
                            KategorijeAkt.db.obojiKnjiguUBazi(k.getId());
                        else
                            KategorijeAkt.db.obojiKnjiguUBazi2(k.getNaslov());*/
                        break;
                    }
                }


            }
        });
    }

}
