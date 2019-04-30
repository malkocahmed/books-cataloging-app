package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.app.Activity;
import android.content.Context;
import android.app.FragmentManager;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by malko on 13-Apr-18.
 */

public class KnjigaAdapter extends BaseAdapter {

    Context kontekst;
    ArrayList<Knjiga> knjige;

    public KnjigaAdapter(Context kontekst, ArrayList<Knjiga> knjige) {
        this.kontekst = kontekst;
        this.knjige = knjige;
    }

    @Override
    public int getCount() {
        return knjige.size();
    }

    @Override
    public Object getItem(int position) {
        return knjige.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) kontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View neki = inflater.inflate(R.layout.knjiga_element, parent, false);
        ImageView slika = (ImageView) neki.findViewById(R.id.eNaslovna);
        TextView naziv = (TextView) neki.findViewById(R.id.eNaziv);
        TextView autor = (TextView) neki.findViewById(R.id.eAutor);
        TextView datum = (TextView) neki.findViewById(R.id.eDatumObjavljivanja);
        TextView opis = (TextView) neki.findViewById(R.id.eOpis);
        TextView brojStr = (TextView) neki.findViewById(R.id.eBrojStranica);


        if(knjige.get(position).getNovi() == false) {
            if(knjige.get(position).getSlika() == null)
                slika.setImageResource(R.drawable.slika);
            else slika.setImageBitmap(knjige.get(position).getSlikaa());
            naziv.setText(knjige.get(position).getNaslov());
            autor.setText("Autor: " + knjige.get(position).getAutor());
            datum.setText("Nepoznat datum objavljivanja" + knjige.get(position).getDatumObjavljivanja());
            opis.setText("Nema opisa" + knjige.get(position).getOpis());
            brojStr.setText("Nepoznat broj stranica");

            if(knjige.get(position).daLijeObojen()) {
                neki.setBackgroundColor(0xffaabbed);
            }
        }
        else {
            //slika.setImageResource(R.drawable.slika);
            Picasso.get().load(knjige.get(position).getSlika().toString()).into(slika);
            naziv.setText(knjige.get(position).getNaziv());
            if(knjige.get(position).getAutori().size() > 1) {
                String s = "";
                ArrayList<Autor> pom = knjige.get(position).getAutori();
                for (int i = 0; i < pom.size() - 1; i++)
                    //s.concat(pom.get(i).getImeiPrezime() + ", ");
                    s += pom.get(i).getImeiPrezime() + ", ";

                s += pom.get(pom.size() - 1).getImeiPrezime();

                autor.setText("Autori: " + s);

            }
            else if(knjige.get(position).getAutori().size() == 1)
                autor.setText("Autor: " + knjige.get(position).getAutori().get(0).getImeiPrezime());
            else autor.setText("Autori nepoznati");

            datum.setText("Datum objavljivanja: " + knjige.get(position).getDatumObjavljivanja());
            opis.setText("Opis: " + knjige.get(position).getOpis());

            if(knjige.get(position).getBrojStranica() != -1)
                brojStr.setText("Broj stranica: " + String.valueOf(knjige.get(position).getBrojStranica()));
            else brojStr.setText("Nepoznat broj stranica");

            if(knjige.get(position).daLijeObojen()) {
                neki.setBackgroundColor(0xffaabbed);
            }

        }


        Button dugmePreporuci = (Button) neki.findViewById(R.id.dPreporuci);
        final String s1 = naziv.getText().toString();
        String s2 = "";
        ArrayList<Autor> pom = knjige.get(position).getAutori();
        for (int i = 0; i < pom.size() - 1; i++)
            s2 += pom.get(i).getImeiPrezime() + ", ";

        s2 += pom.get(pom.size() - 1).getImeiPrezime();

        final String s3 = s2;

        dugmePreporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentPreporuci fp = new FragmentPreporuci();
                bundle.putString("autori", s3);
                bundle.putString("naziv", s1);
                fp.setArguments(bundle);

                FragmentManager fm = ((Activity) kontekst).getFragmentManager();

                fm.beginTransaction().replace(R.id.liste, fp).commit();

                /*Bundle bundle = new Bundle();
                BlankFragment fp = new BlankFragment();
                bundle.putString("autori", s2);
                bundle.putString("naziv", s1);
                fp.setArguments(bundle);

                FragmentManager fm = ((Activity) kontekst).getFragmentManager();

                fm.beginTransaction().replace(R.id.liste, fp).commit();/*/            }
        });

        return neki;
    }
}
