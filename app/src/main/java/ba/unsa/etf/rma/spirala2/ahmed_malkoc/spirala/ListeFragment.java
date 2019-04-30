package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListeFragment extends Fragment {

    private OnItemClick oic;

    public interface OnItemClick {
        public void onItemClicked(int pos, View view);
    }


    public ListeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liste, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        //iz aktivnosti ce biti uzeti podaci
        final KategorijeAkt aktivnost = (KategorijeAkt)getActivity();

        final ListView lv = (ListView) view.findViewById(R.id.listaKategorija);
        final KategorijaAdapter adapterK;
        final AutorAdapter adapterA;

        final ArrayList<Knjiga> knjige = aktivnost.getMyData().getKnjige();
        ArrayList<String> autori = new ArrayList<String>();
        final ArrayList<String> brojNapisanih = new ArrayList<String>();

        for (Knjiga k: knjige) {
            if(!autori.contains(k.getAutor()))
                autori.add(k.getAutor());
        }

        for (String autor: autori) {
            int n = 0;
            for (Knjiga k : knjige) {
                if (k.getAutor().equals(autor)) n++;
            }
            brojNapisanih.add("Broj napisanih knjiga: " + String.valueOf(n));
        }


        adapterA = new AutorAdapter(getActivity(), aktivnost.getMyData().getAutori());
        adapterK = new KategorijaAdapter(getActivity(), aktivnost.getMyData().getKategorije());

        //final ArrayAdapter<String> adapterTest = new ArrayAdapter<String>(getActivity().getBaseContext(),
          //      android.R.layout.simple_list_item_1, KategorijeAkt.db.uzmiKategorijeIzBaze());


        final Button dugmePretrazi = (Button) view.findViewById(R.id.dPretraga);
        final Button dugmeDodajKategoriju = (Button) view.findViewById(R.id.dDodajKategoriju);
        dugmeDodajKategoriju.setEnabled(false);
        final Button dugmeDodajKnjigu = (Button) view.findViewById(R.id.dDodajKnjigu);
        final Button dugmeDodajOnline = (Button) view.findViewById(R.id.dDodajOnline);
        final EditText tekst = (EditText)view.findViewById(R.id.tekstPretraga);

        Button dugmeAutori = (Button)view.findViewById(R.id.dAutori);
        final Button dugmeKategorije = (Button)view.findViewById(R.id.dKategorije);

        final FragmentManager fm = getFragmentManager();

        try {
            oic = (OnItemClick) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException((getActivity().toString() + "Treba implementirati OnItemClick"));
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                oic.onItemClicked(position, view);
            }
        });

        dugmeKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setAdapter(adapterK);
                aktivnost.setKONTROLA(2);

                dugmePretrazi.setVisibility(View.VISIBLE);
                dugmeDodajKategoriju.setVisibility(View.VISIBLE);
                tekst.setVisibility(View.VISIBLE);

            }
        });

        dugmeAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setAdapter(adapterA);
                aktivnost.setKONTROLA(1);

                dugmePretrazi.setVisibility(View.GONE);
                dugmeDodajKategoriju.setVisibility(View.GONE);
                tekst.setVisibility(View.GONE);

            }
        });

        dugmePretrazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KategorijeAkt aktivnost = (KategorijeAkt)getActivity();
                ArrayList<Kategorija> kategorije = aktivnost.getMyData().getKategorije();

                dugmeDodajKategoriju.setEnabled(false);

                adapterK.getFilter().filter(tekst.getText().toString());

                dugmeDodajKategoriju.setEnabled(false);

                boolean ima = false;
                for (Kategorija k: kategorije) {
                    if(k.getNaziv().toUpperCase().contains(tekst.getText().toString().toUpperCase())) {
                        ima = true;
                        break;
                    }

                }

                if(ima == false) dugmeDodajKategoriju.setEnabled(true);
            }
        });

        dugmeDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aktivnost.addCategory(new Kategorija(new String(tekst.getText().toString())));
                dugmeDodajKategoriju.setEnabled(false);

            }
        });

        dugmeDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DodavanjeKnjigeFragment dkf = new DodavanjeKnjigeFragment();
                fm.beginTransaction().replace(R.id.liste, dkf).commit();

            }
        });

        dugmeDodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentOnline fo = new FragmentOnline();
                fm.beginTransaction().replace(R.id.liste, fo).commit();
            }
        });


    }

}
