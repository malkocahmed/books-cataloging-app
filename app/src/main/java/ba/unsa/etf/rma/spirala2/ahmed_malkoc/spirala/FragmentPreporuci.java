package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FragmentPreporuci extends Fragment {

    private ArrayList<Kontakt> kontakti;


    public FragmentPreporuci() {

    }

    private ArrayList<Kontakt> ucitajKontakte() {

        ArrayList<Kontakt> kontakts = new ArrayList<Kontakt>();

        try {
            ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            if (cur.getCount() > 0) {
                Log.i("Content provider", "Reading contact  emails");

                while (cur.moveToNext()) {

                    String ime = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                    Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = " + contactId, null, null);


                    while (emails.moveToNext()) {
                        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        kontakts.add(new Kontakt(ime, emailAddress));
                    }
                    emails.close();
                }

            }
            cur.close();

        } catch (Exception e) {

        }


        //return mejlovi;
        return kontakts;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preporuci, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        kontakti = ucitajKontakte();
        ArrayList<String> mailovi = new ArrayList<String>();

        for (Kontakt k: kontakti) {
            mailovi.add(k.getMail());
        }

        final Bundle podaci = getArguments();

        final Spinner kontaktiS = (Spinner) view.findViewById(R.id.sKontakti);
        Button dugmePosalji = (Button) view.findViewById(R.id.dPosalji);
        final TextView naslov = (TextView) view.findViewById(R.id.knjigaNaslov);
        final TextView autori = (TextView) view.findViewById(R.id.knjigaAutori);

        naslov.setText("Naziv: " + podaci.getString("naziv"));
        autori.setText("Autori: " + podaci.getString("autori"));
        ArrayAdapter<String> kontaktAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, mailovi);
        kontaktiS.setAdapter(kontaktAdapter);

        dugmePosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ime = "";
                for (Kontakt k: kontakti) {
                    if(k.getMail().equals(kontaktiS.getSelectedItem().toString())) {
                        ime = k.getImePrezime();
                        break;
                    }
                }

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] TO = {""};
                TO[0] = kontaktiS.getSelectedItem().toString();
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka");
                String poruka = "Zdravo " + ime + "," + System.lineSeparator() + "Pročitaj knjigu " + podaci.getString("naziv") +
                        " od " + podaci.getString("autori") + "!";

                emailIntent.putExtra(Intent.EXTRA_TEXT, poruka);

                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity().getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
