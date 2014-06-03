/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.cache;

import hr.foi.ud.ztintor.Dretva;
import hr.foi.ud.ztintor.mvc.Model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zoran Tintor
 */
public class ResourceProvider {

    private StringBuilder sadrzaj;
    private Resource webStranica;
    private Cache cache;
    private String nazivDatoteke;

    public StringBuilder dohvatiSadrzaj(URL url) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            sadrzaj = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                sadrzaj.append(inputLine);
                sadrzaj.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sadrzaj;
    }

    public Resource dohvatiResurs(URL url, Model m) {


        nazivDatoteke = dohvatiImeDatoteke(url);
        webStranica = cache.dohvatiResurs(nazivDatoteke);
        if (webStranica == null) {
            webStranica = new Resource();
            webStranica.setUrl(url);
            webStranica.setNaziv(nazivDatoteke);
            webStranica.setSadrzaj(dohvatiSadrzaj(url));
            cache.spremi(webStranica);
        } else {
            webStranica.setSadrzaj(m.dohvatiSadrzajDatoteke(nazivDatoteke));
        }
        return webStranica;
    }//funkcija

    public String dohvatiImeDatoteke(URL url) {
        String ime = "";
        String[] naziv;
        naziv = url.toString().substring(7).split("/");
        for (int i = 0; i < naziv.length; i++) {
            ime = ime.concat(naziv[i].toString() + ".");
        }
        ime = ime + "html";
        return ime;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}//klasa
