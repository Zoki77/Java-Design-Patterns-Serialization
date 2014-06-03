/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor;

import hr.foi.ud.ztintor.mvc.Model;
import hr.foi.ud.ztintor.mvc.View;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zoran Tintor
 */
public class Ztintor_zadaca_4 {

    public static URL pocetniLink;
    public static boolean postoji = false;
    public static boolean ser = false;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException {

        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        view.setCac(model.getC());
        model.provjeriSintaksu(args, model, view);

        long pocetak;
        long kraj;
        pocetak = System.currentTimeMillis();

        while (true) {

            kraj = System.currentTimeMillis();
            try {
                Thread.currentThread().sleep(model.getInterval() * 1000 - (kraj - pocetak));
            } catch (InterruptedException ex) {
                Logger.getLogger(Ztintor_zadaca_4.class.getName()).log(Level.SEVERE, null, ex);
            }
            pocetak = System.currentTimeMillis();
            model.osvjeziStranicu();

        }//while

    }
}
