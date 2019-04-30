package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;


public class DodavanjeKnjigeFragment extends Fragment {

    private ArrayList<String> imena = new ArrayList<String>();
    private ImageView slika;

    public DodavanjeKnjigeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dodavanje_knjige, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final KategorijeAkt aktivnost = (KategorijeAkt)getActivity();

        ArrayList<Kategorija> kategorije = aktivnost.getMyData().getKategorije();

        for (Kategorija k : kategorije) {
            imena.add(k.getNaziv());
        }

        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, imena);
        final Spinner spiner = (Spinner) view.findViewById(R.id.sKategorijaKnjige);
        spiner.setAdapter(adapterS);

        Button dugmeNadjiSliku = (Button)view.findViewById(R.id.dNadjiSliku);
        Button dugmeUpisiKnjigu = (Button)view.findViewById(R.id.dUpisiKnjigu);
        Button dugmePonisti = (Button)view.findViewById(R.id.dPonisti);
        final EditText tekstAutor = (EditText)view.findViewById(R.id.imeAutora);
        final EditText tekstNaziv = (EditText)view.findViewById(R.id.nazivKnjige);

        slika = (ImageView)view.findViewById(R.id.naslovnaStr);

        dugmeNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);*/

                Intent intent = new Intent();
                intent.setAction(intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, ""), 2);
            }
        });

        dugmeUpisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DodavanjeKnjigeFragment.this.slika.buildDrawingCache();
                Knjiga k = new Knjiga(tekstAutor.getText().toString(), tekstNaziv.getText().toString(),
                        spiner.getSelectedItem().toString(), Bitmap.createBitmap(DodavanjeKnjigeFragment.this.slika.getDrawingCache()));

                Autor a = new Autor(tekstAutor.getText().toString(), null);
                a.setPomocni();

                aktivnost.addBook(k);
                aktivnost.addAuthor(a);

                FragmentManager fm = getFragmentManager();

                ListeFragment lf = new ListeFragment();
                fm.beginTransaction().replace(R.id.liste, lf).commit();
            }
        });

        dugmePonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();

                ListeFragment lf = new ListeFragment();
                fm.beginTransaction().replace(R.id.liste, lf).commit();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == -1) {
            this.slika.setImageURI(data.getData());

        }
    }

}
