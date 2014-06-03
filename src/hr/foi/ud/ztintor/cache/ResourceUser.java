/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.cache;

import hr.foi.ud.ztintor.mvc.Model;
import java.net.URL;

/**
 *
 * @author Zoran Tintor
 */
public class ResourceUser {

    private Resource webStranica;
    private ResourceProvider provider;

    public void dohvatiResurs(URL url, ResourceProvider rp, Model model) {
        webStranica = rp.dohvatiResurs(url, model);
        setWebStranica(webStranica);
        model.setWebStranica(webStranica);
    }

    //Getteri i setteri
    public Resource getWebStranica() {
        return webStranica;
    }

    public void setWebStranica(Resource webStranica) {
        this.webStranica = webStranica;
    }

    public ResourceProvider getProvider() {
        return provider;
    }

    public void setProvider(ResourceProvider provider) {
        this.provider = provider;
    }
}
