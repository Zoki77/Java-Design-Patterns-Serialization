/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.cache;

import hr.foi.ud.ztintor.mvc.Model;
import java.util.ArrayList;

/**
 *
 * @author Zoran Tintor
 */
public interface Cache {

    public void spremi(Resource r);

    public ArrayList getLista();

    public void setLista(ArrayList list);

    public Resource dohvatiResurs(String ime);

    public Model getM();

    public void setM(Model m);
}
