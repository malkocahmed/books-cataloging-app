package ba.unsa.etf.rma.spirala2.ahmed_malkoc.spirala;

/**
 * Created by malko on 27-May-18.
 */

public class Kontakt {

    private String imePrezime;
    private String mail;

    public Kontakt(String imePrezime, String mail) {
        this.imePrezime = imePrezime;
        this.mail = mail;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
