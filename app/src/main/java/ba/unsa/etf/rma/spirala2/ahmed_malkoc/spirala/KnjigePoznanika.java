package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by malko on 21-May-18.
 */

public class KnjigePoznanika extends IntentService {

    private ArrayList<Knjiga> knjige;

    public static final int STATUS_START = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public KnjigePoznanika() { super(null); }

    public KnjigePoznanika(String name) { super(name); }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    @Override
    public void onCreate() { super.onCreate(); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();

        receiver.send(STATUS_START, Bundle.EMPTY);

        knjige = new ArrayList<Knjiga>();

        String query = null;
        try {
            intent.getExtras().getString("query");
            query = URLEncoder.encode(intent.getExtras().getString("query"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url1 = "https://www.googleapis.com/books/v1/users/" + query + "/bookshelves";

        try {
            URL url = new URL(url1);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);
            JSONArray itemsBookshelves = jo.getJSONArray("items");

            ArrayList<Integer> bookshelvesIDs = new ArrayList<Integer>();

            for(int i=0; i<itemsBookshelves.length(); i++) {
                JSONObject bookshelf = itemsBookshelves.getJSONObject(i);
                int bookshelfID = bookshelf.getInt("id");
                bookshelvesIDs.add(bookshelfID);
            }

            for (int j=0; j<bookshelvesIDs.size(); j++) {
                String url2 = "https://www.googleapis.com/books/v1/users/" + query + "/bookshelves/" + bookshelvesIDs.get(j).toString()
                        + "/volumes";

                URL urlNovi = new URL(url2);
                HttpURLConnection urlConnection2 = (HttpURLConnection) urlNovi.openConnection();
                InputStream in2 = new BufferedInputStream(urlConnection2.getInputStream());

                String rezultatNovi = convertStreamToString(in2);
                JSONObject joNovi = new JSONObject(rezultatNovi);
                JSONArray items = joNovi.getJSONArray("items");


                for (int k = 0; k < items.length(); k++) {
                    JSONObject book = items.getJSONObject(k);

                    String id;
                    if (book.has("id"))
                        id = book.getString("id");
                    else id = "";

                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    String naziv;
                    if (volumeInfo.has("title"))
                        naziv = volumeInfo.getString("title");
                    else naziv = "";

                    ArrayList<Autor> autori = new ArrayList<Autor>();
                    if (volumeInfo.has("authors")) {
                        JSONArray JSONautori = volumeInfo.getJSONArray("authors");
                        for (int l = 0; l < JSONautori.length(); l++) {
                            String imeAutora = JSONautori.getString(l);
                            autori.add(new Autor(imeAutora, id));
                        }
                    }

                    String opis;
                    if (volumeInfo.has("description"))
                        opis = volumeInfo.getString("description");
                    else opis = "";

                    String datum;
                    if (volumeInfo.has("publishedDate"))
                        datum = volumeInfo.getString("publishedDate");
                    else datum = "";

                    String urlSlika;
                    URL slika;
                    if (volumeInfo.has("imageLinks")) {
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        urlSlika = imageLinks.getString("thumbnail");
                        slika = new URL(urlSlika);
                    } else
                        slika = new URL("https://image.freepik.com/free-icon/open-book-top-view_318-58980.jpg");
                    //else slika = new URL("https://i.imgur.com/YSATyE8.jpg");

                    int brojStranica;
                    if (volumeInfo.has("pageCount"))
                        brojStranica = volumeInfo.getInt("pageCount");
                    else brojStranica = -1;

                    knjige.add(new Knjiga(id, naziv, autori, opis, datum, slika, brojStranica));
                }

            }

            bundle.putParcelableArrayList("listaKnjiga", knjige);
            receiver.send(STATUS_FINISHED, bundle);

        } catch (MalformedURLException e) {
            receiver.send(STATUS_ERROR, Bundle.EMPTY);
        } catch (IOException e) {
            receiver.send(STATUS_ERROR, Bundle.EMPTY);
        } catch (JSONException e) {
            receiver.send(STATUS_ERROR, Bundle.EMPTY);
        }






    }



}
