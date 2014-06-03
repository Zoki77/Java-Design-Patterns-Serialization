/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor;

import hr.foi.ud.ztintor.cache.ResourceProvider;
import hr.foi.ud.ztintor.cache.ResourceUser;
import hr.foi.ud.ztintor.mvc.Controller;
import hr.foi.ud.ztintor.mvc.Model;
import hr.foi.ud.ztintor.mvc.View;

/**
 *
 * @author Zoran Tintor
 */
public class Dretva extends Thread {

    private Model model;
    private Controller controler;
    private View view;
    private ResourceUser ru;
    private ResourceProvider rp;

    public Dretva(ResourceProvider rp, ResourceUser ru, Model model, View view) {
        this.model = model;
        this.view = view;
        this.rp = rp;
        this.ru = ru;
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        controler = new Controller(view);
        while (true) {
            ru.dohvatiResurs(Ztintor_zadaca_4.pocetniLink, rp, model);
            model.dohvatiListupoveznica();
            controler.provjeriUnos();

        }//while
    }//run

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }
}//Klasa
