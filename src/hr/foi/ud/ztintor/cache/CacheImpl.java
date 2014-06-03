/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.cache;

import hr.foi.ud.ztintor.Ztintor_zadaca_4;
import hr.foi.ud.ztintor.evictor.Izbaci;
import hr.foi.ud.ztintor.evictor.Evictor;
import hr.foi.ud.ztintor.mvc.Model;
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
public class CacheImpl implements Cache {

    private ArrayList lista = new ArrayList();
    private long trenutnaVelicina = 0;
    private boolean toBig = false;
    private Model m;

    //Spremanje datoteke u željeni direktorij te brisanje ako nema mjesta.
    @Override
    public void spremi(Resource r) {
        Evictor e = new Izbaci();
        File file = new File(m.getNazivSpremista() + "\\" + r.getNaziv());
        try {

            if (m.isKb()) {
                trenutnaVelicina = m.izracunajVelicinu();
                if ((trenutnaVelicina + r.getSadrzaj().toString().getBytes().length / 1000) > m.getOgranicenje()) {
                    if (m.isStrategija()) {
                        toBig = e.izbaci(lista, m.getNazivSpremista());
                    } else {
                        toBig = e.izbaciKB(lista, m.getNazivSpremista());
                    }

                }
            } else {
                if (Ztintor_zadaca_4.postoji) {
                    //Ztintor_zadaca_4.ser=true;
                    m.setOgranicenje(m.getOgranicenje() + 1);
                    Ztintor_zadaca_4.postoji = false;
                }
                System.out.println("-----------------------------------------------------"+m.getOgranicenje());
                if (new File(m.getNazivSpremista()).listFiles().length == m.getOgranicenje() + 1) {
                    if (m.isStrategija()) {
                        toBig = e.izbaci(lista, m.getNazivSpremista());
                    } else {
                        toBig = e.izbaciKB(lista, m.getNazivSpremista());
                    }
                }
            }

            if (toBig) {
                System.exit(1);
            }

            if (!m.provjeriListu(r.getNaziv())) {
                r.setVrijemeSpremanja(new Date());
                r.setId(getLista().size());
                r.setSpremljen(true);
                r.setBrojKoristenja(0);
                lista.add(r);
                setLista(lista);
                m.upisiUDnevnik(r);
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(r.getSadrzaj().toString());
            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(CacheImpl.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    // Dohvaćanje resursa po nazivu.
    @Override
    public Resource dohvatiResurs(String ime) {
        Resource resurs = null;
        Resource r;
        for (int i = 0; i < getLista().size(); i++) {
            r = (Resource) getLista().get(i);
            if (r.getNaziv().equals(ime) && r.isSpremljen()) {
                resurs = r;
                resurs.setBrojKoristenja(r.getBrojKoristenja() + 1);
                break;
            }
        }
        return resurs;

    }

    @Override
    public ArrayList getLista() {
        return lista;
    }

    @Override
    public void setLista(ArrayList list) {
        this.lista = list;
    }

    @Override
    public Model getM() {
        return m;
    }

    @Override
    public void setM(Model m) {
        this.m = m;
    }
}
