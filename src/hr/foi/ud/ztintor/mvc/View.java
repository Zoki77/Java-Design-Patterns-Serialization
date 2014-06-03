/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.mvc;

import hr.foi.ud.ztintor.Ztintor_zadaca_4;
import hr.foi.ud.ztintor.cache.Cache;
import hr.foi.ud.ztintor.cache.Resource;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Zoran Tintor
 */
public class View implements Observer {

    private Model m;
    private Cache cac;

    public View(Model model) {
        this.m = model;
    }

    // ispis komandi
    public void ispisiUpute() {
        System.out.println("----------KOMANDE-------------");
        System.out.println("-I - ispis adresa poveznica s rednim brojem");
        System.out.println("-J n - prijelaz na poveznicu s rednim brojem n te ucitavanje web stranice");
        System.out.println("-S - prikaži sadrzaj spremista");
        System.out.println("-E - prazni spremiste");
        System.out.println("-Q - prekid rada programa");
        System.out.println("------------------------------");
    }

    // ispis poveznica
    public void ispisPoveznica() {
        m.dohvatiListupoveznica();
        System.out.println("---------POVEZNICE-------------");
        ispisiLinije(m.getMax());
        for (int i = 0; i < m.getWebStranica().getListaURLa().size(); i++) {
            System.out.print("| " + (i + 1) + ".");
            ispisipraznineBroja((Integer.toString(m.getWebStranica().getListaURLa().size()).length() - Integer.toString(i + 1).length()) + 1);
            System.out.print("|  " + m.getWebStranica().getListaURLa().get(i));
            ispisipraznineURLa(m.getMax() - m.getWebStranica().getListaURLa().get(i).toString().length());
            ispisiLinije(m.getMax());
        }
    }

    //ispisi za tablicu
    public void ispisiLinije(int broj) {
        for (int a = 0; a < broj + 12; a++) {
            System.out.print("-");
        }
        System.out.println("");

    }

    public void ispisipraznineURLa(int broj) {
        for (int a = 0; a < broj; a++) {
            System.out.print(" ");
        }
        System.out.println("  |");

    }

    public void ispisipraznineBroja(int broj) {
        for (int a = 0; a < broj; a++) {
            System.out.print(" ");
        }
    }

    // ispis pri prelasku na novu stranicu
    public void novaStranica(String redniBroj) {
        m.promjeniStranicu(redniBroj);
        System.out.println("---------------Nova stranica------------");
        System.out.println(Ztintor_zadaca_4.pocetniLink.toString());
    }

    // ispis statistike
    public void ispisiStatistiku() {
        Resource r;
        System.out.println("--------------STATISTIKA--------------");
        System.out.println(m.getNazivSpremista());
        for (int i = 0; i < cac.getLista().size(); i++) {
            r = (Resource) cac.getLista().get(i);
            if (r.isSpremljen()) {
                if (m.getWebStranica().getNaziv().equals(r.getNaziv())) {
                    System.out.println((i + 1) + ". " + r.getNaziv() + "   " + r.getBrojKoristenja() + "   " + "Koristi se");
                } else {
                    System.out.println((i + 1) + ". " + r.getNaziv() + "   " + r.getBrojKoristenja() + "   " + r.getZadnjeKoistenje());
                }
            }
        }
        System.out.println("--------------------------------------");
    }

    // ispis pri izlasku
    public void izadi() {
        System.out.printf("Serijalizirani podatci spremljeni u: " + m.getNazivSpremista() + "\\serijalizacija.ser");
        m.serijaliziraj();
    }

    // ispis kod brisanja
    public void brisiNaZahtjev() {
        m.setOcisti(true);
        m.brisiSpremiste();
        System.out.println("Obrisano spremiste " + m.getNazivSpremista());
    }

    public Cache getCac() {
        return cac;
    }

    public void setCac(Cache cac) {
        this.cac = cac;
    }

    //Observer
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }
}
