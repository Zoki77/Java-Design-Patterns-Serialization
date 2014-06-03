/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.mvc;

import hr.foi.ud.ztintor.Dretva;
import hr.foi.ud.ztintor.Ztintor_zadaca_4;
import static hr.foi.ud.ztintor.Ztintor_zadaca_4.pocetniLink;
import static hr.foi.ud.ztintor.Ztintor_zadaca_4.postoji;
import hr.foi.ud.ztintor.cache.Cache;
import hr.foi.ud.ztintor.cache.CacheImpl;
import hr.foi.ud.ztintor.cache.Resource;
import hr.foi.ud.ztintor.cache.ResourceProvider;
import hr.foi.ud.ztintor.cache.ResourceUser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Zoran Tintor
 */
public class Model extends Observable {

    private boolean status;
    private String nazivSpremista;
    private int interval = 0;
    private int ogranicenje = 0;
    private boolean kb;
    private boolean strategija;
    private boolean ocisti;
    Pattern pattern;
    Matcher m;
    StringBuilder stb = new StringBuilder();
    ArrayList list = new ArrayList();
    Cache c = new CacheImpl();
    ResourceProvider rp = new ResourceProvider();
    ResourceUser ru = new ResourceUser();
    StringBuilder sb;
    String imeDatoteke;
    Resource res;
    Resource webStranica = new Resource();
    private int max = 0;
    private int velicina = 0;

    //Funkcija za provjeru sintakse.
    public void provjeriSintaksu(String[] args, Model model, View view) {
        for (int i = 0; i < args.length; i++) {
            stb.append(args[i]).append(" ");
        }
        String p = stb.toString().trim();
        // url putanja interval ograničenje (-KB) -NS|-NK (-clean)
        String sintaksa = "^[^\\s]+ ([A-Z]\\:\\\\[^\\s]+) ([\\d]+) ([\\d]+)( +-KB)? (-NS|-NK+)( +-clean)?$";
        pattern = Pattern.compile(sintaksa);
        m = pattern.matcher(p);
        status = m.matches();
        if (status) {
            nazivSpremista = m.group(1);
            interval = Integer.parseInt(m.group(2));
            ogranicenje = Integer.parseInt(m.group(3));
            if (m.group(4) == null) {
                kb = false;
            } else {
                kb = true;
            }
            if (m.group(5).equals("-NS")) {
                strategija = true;
            } else {
                strategija = false;
            }
            if (m.group(6) == null) {
                ocisti = false;
            } else {
                ocisti = true;
            }

            dohvatiSerializaciju();
            brisiSpremiste();
            postaviCacheUzorak();
            pokreniDretvu(args, model, view);
        } else {
            setChanged();
            notifyObservers("Sintaksa ne odgovara");
            System.exit(1);
        }
    }

    // Dohvaćanje serijaliziranih podataka.
    public void dohvatiSerializaciju() {
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(nazivSpremista + "\\serijalizacija.ser");
            postoji = true;
            Ztintor_zadaca_4.ser = true;
            ObjectInputStream in;
            in = new ObjectInputStream(fileIn);
            list = (ArrayList) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException ex) {
            setChanged();
            notifyObservers("Ne postoji datoteka serializacije");
            postoji = false;
            Ztintor_zadaca_4.ser = false;
        } catch (IOException ex) {
            Logger.getLogger(Ztintor_zadaca_4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Ztintor_zadaca_4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //brisanje spremista
    public void brisiSpremiste() {
        if (ocisti) {
            File f = new File(nazivSpremista);
            for (File file : f.listFiles()) {
                file.delete();
            }
            list.clear();
            setChanged();
            notifyObservers("Spremiste " + nazivSpremista + " je ocisceno");
        }
    }

    // postavljanje varijabli i povezivanje klasa.
    public void postaviCacheUzorak() {

        c.setLista(list);
        c.setM(this);
        rp.setCache(c);
    }

    // Pokrtanje dretve.
    public void pokreniDretvu(String[] args, Model model, View view) {
        try {
            pocetniLink = new URL(args[0]);
        } catch (MalformedURLException ex) {
            setChanged();
            notifyObservers("Krivi link");
            System.exit(1);
        }
        Dretva dretva = new Dretva(rp, ru, model, view);
        dretva.start();
    }

    // osvježavanje stranice.
    public void osvjeziStranicu() {
        StringBuilder sss;
        setChanged();
        notifyObservers("Novi interval!!!!!!");
        sb = rp.dohvatiSadrzaj(pocetniLink);
        imeDatoteke = rp.dohvatiImeDatoteke(pocetniLink);
        sss = dohvatiSadrzajDatoteke(imeDatoteke);
        if (sss != null) {
            webStranica.setSadrzaj(sss);
        } else {
            if (Ztintor_zadaca_4.ser) {
                setOgranicenje(getOgranicenje() - 1);
            }
            ru.dohvatiResurs(pocetniLink, rp, this);
            dohvatiListupoveznica();
        }
        res = update(sb, imeDatoteke);
        setWebStranica(res);
        dohvatiListupoveznica();
    }

    // Dohvaćanje liste poveznica.
    public void dohvatiListupoveznica() {
        ArrayList listaURLa = new ArrayList();
        String text;
        String linkText;
        int prvi = 0;
        int zadnji;
        int zadnji1;
        int zadnji2;
        while (true) {
            try {
                prvi = webStranica.getSadrzaj().toString().indexOf("href=", prvi) + 6;
                if (prvi == 5) {
                    break;
                }
                text = webStranica.getSadrzaj().toString().substring(prvi);
                zadnji1 = text.indexOf("\"");
                zadnji2 = text.indexOf("'");
                if ((zadnji1 < zadnji2)) {
                    zadnji = zadnji1;
                } else {
                    zadnji = zadnji2;
                }
                if (zadnji1 == -1) {
                    zadnji = zadnji2;
                }
                if (zadnji2 == -1) {
                    zadnji = zadnji1;
                }
                if (text.startsWith("http:")) {
                    linkText = text.substring(0, zadnji);
                } else if (text.startsWith("www.")) {
                    linkText = "http://" + text.substring(0, zadnji);
                } else if (text.startsWith("/")) {
                    linkText = webStranica.getUrl().toString() + text.substring(0, zadnji);
                } else {
                    linkText = webStranica.getUrl().toString() + "/" + text.substring(0, zadnji);
                }
                URL link = new URL(linkText);
                listaURLa.add(link);
                velicina = linkText.length();
                if (velicina > max) {
                    max = velicina;
                }

            } //while
            catch (MalformedURLException ex) {
                Logger.getLogger(ResourceUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//while
        webStranica.setListaURLa(listaURLa);
    }

    // Promjena stranice.
    public void promjeniStranicu(String redniBroj) {
        webStranica.setZadnjeKoistenje(new Date());
        Ztintor_zadaca_4.pocetniLink = (URL) webStranica.getListaURLa().get(Integer.parseInt(redniBroj) - 1);
    }

    // Serijalizacija
    public void serijaliziraj() {
        webStranica.setZadnjeKoistenje(new Date());
        try {
            for (int i = 0; i < c.getLista().size(); i++) {
                ((Resource) c.getLista().get(i)).setSadrzaj(null);
            }
            FileOutputStream fileOut =
                    new FileOutputStream(nazivSpremista + "\\serijalizacija.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(c.getLista());
            out.close();
            fileOut.close();
            //System.out.printf("Serialized data is saved in " + nazivSpremista + "\\serijalizacija.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
        System.exit(1);
    }

    // provjeravanje dali je objekt koristen.
    public boolean provjeriListu(String ime) {
        Resource r;
        boolean ima = false;
        for (int i = 0; i < c.getLista().size(); i++) {
            r = (Resource) c.getLista().get(i);
            if (r.getNaziv().equals(ime)) {
                r.setBrojKoristenja(r.getBrojKoristenja() + 1);
                r.setVrijemeSpremanja(new Date());
                r.setSpremljen(true);
                upisiUDnevnik(r);
                ima = true;
                break;
            }
        }
        return ima;
    }

    // upisivanje u dnevnik.
    public void upisiUDnevnik(Resource r) {
        File dnevnik = new File(nazivSpremista + "\\dnevnik.txt");

        try {
            if (!dnevnik.exists()) {
                dnevnik.createNewFile();
            }
            FileWriter fwd = new FileWriter(dnevnik.getAbsoluteFile(), true);
            BufferedWriter bwd = new BufferedWriter(fwd);
            bwd.write("Dodan dokument: " + r.getNaziv() + "   " + r.getVrijemeSpremanja() + "\r\n");
            bwd.close();
        } catch (IOException ex) {
            Logger.getLogger(CacheImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // racunanje velicine spremita.
    public long izracunajVelicinu() {
        long velicinaSpremnika = 0;
        File dir = new File(nazivSpremista);
        File[] listf = dir.listFiles();
        for (int i = 0; i < listf.length; i++) {
            if (listf[i].getName().endsWith(".html")) {
                velicinaSpremnika = velicinaSpremnika + (int) (listf[i].length() / 1000);
            }
        }
        return velicinaSpremnika;
    }

    // osvjezavanje stranice.
    public Resource update(StringBuilder sb, String ime) {
        Resource resu;
        resu = c.dohvatiResurs(ime);
        resu.setSadrzaj(dohvatiSadrzajDatoteke(ime));
        System.out.println(ime);
        File file = new File(nazivSpremista + "\\" + ime);
        if (resu.getSadrzaj().toString().equals(sb.toString())) {
            setChanged();
            notifyObservers("Stranica nije updaejtana");
        } else {
            setChanged();
            notifyObservers("Stranica Updejtana");
            try {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(sb.toString());
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(CacheImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resu;
    }

    // dohvaćanje sadržaja datoteke.
    public StringBuilder dohvatiSadrzajDatoteke(String naziv) {
        StringBuilder sadrzaj = new StringBuilder();
        try {

            String outputLine;
            File file = new File(nazivSpremista + "\\" + naziv);
            if (!file.exists()) {
                setChanged();
                notifyObservers("datoteka izbrisna, citam sa stranice!");
                return null;

            }

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);


            while ((outputLine = br.readLine()) != null) {
                sadrzaj.append(outputLine);
                sadrzaj.append("\n");
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(CacheImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sadrzaj;
    }

    public Resource getWebStranica() {
        return webStranica;
    }

    public void setWebStranica(Resource webStranica) {
        this.webStranica = webStranica;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getNazivSpremista() {
        return nazivSpremista;
    }

    public void setNazivSpremista(String nazivSpremista) {
        this.nazivSpremista = nazivSpremista;
    }

    public Cache getC() {
        return c;
    }

    public void setC(Cache c) {
        this.c = c;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isOcisti() {
        return ocisti;
    }

    public void setOcisti(boolean ocisti) {
        this.ocisti = ocisti;
    }

    public int getOgranicenje() {
        return ogranicenje;
    }

    public void setOgranicenje(int ogranicenje) {
        this.ogranicenje = ogranicenje;
    }

    public boolean isKb() {
        return kb;
    }

    public void setKb(boolean kb) {
        this.kb = kb;
    }

    public boolean isStrategija() {
        return strategija;
    }

    public void setStrategija(boolean strategija) {
        this.strategija = strategija;
    }
}
