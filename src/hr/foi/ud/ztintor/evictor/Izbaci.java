/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.evictor;

import hr.foi.ud.ztintor.cache.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zoran Tintor
 */
public class Izbaci implements Evictor {

    private Resource r = new Resource();
    private Resource najr = new Resource();
    private Date d;
    private Date najstariji;
    private int najkoristeniji = -1;
    private int broj;

    @Override
    public boolean izbaci(ArrayList list, String nazivSpremista) {
        if (list.isEmpty()) {
            System.out.println("Datoteka prevelika za spremiste");
            return true;
        } else {
            najstariji = new Date();
            for (int i = 0; i < list.size(); i++) {
                r = (Resource) list.get(i);
                d = r.getVrijemeSpremanja();
                if (d.compareTo(najstariji) < 0 && r.isSpremljen()) {
                    najstariji = d;
                    najr = r;
                }
            }
            obrisi(najr, nazivSpremista);
            return false;
        }

    }

    @Override
    public boolean izbaciKB(ArrayList list, String nazivSpremista) {
        if (list.isEmpty()) {
            System.out.println("Datoteka prevelika za spremiste");
            return true;
        } else {
            ArrayList jednaki = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                r = (Resource) list.get(i);
                broj = r.getBrojKoristenja();
                if (broj == najkoristeniji) {
                    jednaki.add(r);
                } else if (broj > najkoristeniji) {
                    najkoristeniji = broj;
                    najr = r;
                    jednaki = new ArrayList();
                    jednaki.add(r);
                }
            }//for
            if (jednaki.size() > 1) {
                izbaci(jednaki, nazivSpremista);
            } else {
                obrisi(najr, nazivSpremista);
            }
            return false;
        }

    }//funkcija

    @Override
    public void obrisi(Resource r, String nazivSpremista) {
        r.setVrijemeBrisanja(new Date());
        r.setSpremljen(false);
        File file = new File(nazivSpremista + "\\" + r.getNaziv());
        System.out.println("Iz spremista se izbacuje datoteka: " + file.getName());
        file.delete();

        File dnevnik = new File(nazivSpremista + "\\dnevnik.txt");

        try {
            if (!dnevnik.exists()) {
                dnevnik.createNewFile();
            }

            FileWriter fwd = new FileWriter(dnevnik.getAbsoluteFile(), true);
            BufferedWriter bwd = new BufferedWriter(fwd);
            bwd.write("Izbačen dokument: " + r.getNaziv() + "   Vrijeme zadržavanja " + ((r.getVrijemeBrisanja().getTime() - r.getVrijemeSpremanja().getTime()) / 1000) + " sekundi    Broj koristenja: " + r.getBrojKoristenja() + "\r\n");
            bwd.close();
        } catch (IOException ex) {
            Logger.getLogger(Izbaci.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
