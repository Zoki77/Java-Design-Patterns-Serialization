/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.mvc;

import java.io.Console;

/**
 *
 * @author Zoran Tintor
 */
public class Controller {

    private View v;
    private boolean petlja;
    private String input;

    public Controller(View view) {
        this.v = view;
    }

    public void provjeriUnos() {
        Console c = System.console();
        if (c == null) {
            System.exit(1);
        }

        v.ispisiUpute();

        petlja = true;
        while (petlja) {
            input = c.readLine();
            String split[] = input.split(" ");
            switch (split[0]) {
                case "-I":
                    v.ispisPoveznica();
                    break;
                case "-J":
                    v.novaStranica(split[1]);
                    petlja = false;
                    break;

                case "-S":
                    v.ispisiStatistiku();
                    break;
                case "-E":
                    v.brisiNaZahtjev();
                    break;
                case "-Q":
                    v.izadi();
                    break;
            }
        }
    }
}
