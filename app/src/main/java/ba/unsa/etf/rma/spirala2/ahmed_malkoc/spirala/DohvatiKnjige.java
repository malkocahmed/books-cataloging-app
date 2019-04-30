package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by malko on 14-May-18.
 */

public class DohvatiKnjige extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiKnjigeDone {
        public void onDohvatiDone(ArrayList<Knjiga> knjige);
    }

    private ArrayList<Knjiga> knjige;
    private IDohvatiKnjigeDone pozivatelj;

    //konstruktor
    public DohvatiKnjige(IDohvatiKnjigeDone pozivatelj) {
        this.pozivatelj = pozivatelj;
    }


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
    protected Void doInBackground(String... params) {
        knjige = new ArrayList<Knjiga>();

        String s = params[0];
        ArrayList<String> naslovi = new ArrayList<String>();

        String[] pom = s.split(";");
        for(int a=0; a<pom.length; a++)
            naslovi.add(pom[a]);


        if(naslovi.size() == 1) {


            String query = null;
            try {
                query = URLEncoder.encode(params[0], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=5";
            try {
                URL url = new URL(url1);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String rezultat = convertStreamToString(in);
                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject book = items.getJSONObject(i);

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
                        for (int j = 0; j < JSONautori.length(); j++) {
                            String imeAutora = JSONautori.getString(j);
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {

            for(int z=0; z<naslovi.size(); z++) {

                String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + naslovi.get(z) + "&maxResults=5";
                try {
                    URL url = new URL(url1);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String rezultat = convertStreamToString(in);
                    JSONObject jo = new JSONObject(rezultat);
                    JSONArray items = jo.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject book = items.getJSONObject(i);

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
                            for (int j = 0; j < JSONautori.length(); j++) {
                                String imeAutora = JSONautori.getString(j);
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

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(knjige);
    }

}
