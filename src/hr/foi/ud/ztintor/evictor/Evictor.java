/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.evictor;

import hr.foi.ud.ztintor.cache.Resource;
import java.util.ArrayList;

/**
 *
 * @author Zoran Tintor
 */
public interface Evictor{

    public boolean izbaci(ArrayList list, String nazivSpremista);

    public boolean izbaciKB(ArrayList list, String nazivSpremista);

    public void obrisi(Resource r, String nazivSpremista);
}
