package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by malko on 14-Apr-18.
 */

public class AutorAdapter extends BaseAdapter {

    Context kontekst;
    ArrayList<Autor> autori;
    //ArrayList<String> autori;
    //ArrayList<String> brojNapisanih;

    public AutorAdapter(Context kontekst, ArrayList<Autor> autori) {
        this.kontekst = kontekst;
        this.autori = autori;
        //this.brojNapisanih = brojNapisanih;
    }

    @Override
    public int getCount() {
        return autori.size();
    }

    @Override
    public Object getItem(int position) {
        return autori.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) kontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View neki = inflater.inflate(R.layout.autor_element, parent, false);
        TextView imePrezime = (TextView) neki.findViewById(R.id.imePrezime);
        TextView brojKnjiga = (TextView) neki.findViewById(R.id.brojKnjiga);

        imePrezime.setText(autori.get(position).getImeiPrezime());
        brojKnjiga.setText("Broj napisanih knjiga: " + String.valueOf(autori.get(position).getKnjige().size()));



        return neki;
    }
}
