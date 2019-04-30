package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malko on 13-Apr-18.
 */

public class KategorijaAdapter extends BaseAdapter implements Filterable {
    Context kontekst;
    List<Kategorija> kategorije; //original data
    List<Kategorija> filterisano; //filtered data

    KategorijaFilter filter; // filter

    private int broj; // broj filterisanih

    public KategorijaAdapter(Context kontekst, List<Kategorija> kategorije) {
        this.kontekst = kontekst;
        this.kategorije = kategorije;
        this.filterisano = kategorije;
        broj = filterisano.size();
    }

    @Override
    public int getCount() {
        return filterisano.size();
    }

    @Override
    public Object getItem(int position) {
        return filterisano.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) kontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View neki = inflater.inflate(R.layout.kategorija_element, parent, false);
        TextView kategorijaTekst = (TextView) neki.findViewById(R.id.textViewIme);

        kategorijaTekst.setText(filterisano.get(position).getNaziv());

        return neki;
    }

    public boolean provjera() {
        return broj == 0;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new KategorijaFilter();

        return filter;
    }



    public class KategorijaFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence tekst) {
            FilterResults rezultat = new FilterResults();
            if (tekst == null || tekst.length() == 0) {
                rezultat.values = kategorije;
            }
            else {
                ArrayList<Kategorija> rezultatFilterisaneKategorije = new ArrayList<>();
                for (Kategorija k : kategorije) {
                    if (k.getNaziv().toUpperCase().contains(tekst.toString().toUpperCase())) {
                        rezultatFilterisaneKategorije.add(k);
                    }
                }
                rezultat.values = rezultatFilterisaneKategorije;

            }

            return rezultat;


        }


        @Override
        protected void publishResults(CharSequence tekst, FilterResults rezultat)
        {
            filterisano = (List<Kategorija>) rezultat.values;
            broj = filterisano.size();
            notifyDataSetChanged();
        }

    }


}
