/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.ud.ztintor.cache;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Zoran Tintor
 */
public class Resource implements Serializable {

    private int brojKoristenja;
    private Date vrijemeSpremanja;
    private Date zadnjeKoistenje;
    private URL url;
    private String naziv;
    private ArrayList listaURLa;
    private StringBuilder sadrzaj;
    private boolean spremljen;
    private int id;
    private Date vrijemeBrisanja;

    //Getteri i Setteri
    public int getBrojKoristenja() {
        return brojKoristenja;
    }

    public void setBrojKoristenja(int brojKoristenja) {
        this.brojKoristenja = brojKoristenja;
    }

    public Date getVrijemeSpremanja() {
        return vrijemeSpremanja;
    }

    public void setVrijemeSpremanja(Date vrijemeSpremanja) {
        this.vrijemeSpremanja = vrijemeSpremanja;
    }

    public Date getZadnjeKoistenje() {
        return zadnjeKoistenje;
    }

    public void setZadnjeKoistenje(Date zadnjeKoistenje) {
        this.zadnjeKoistenje = zadnjeKoistenje;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList getListaURLa() {
        return listaURLa;
    }

    public void setListaURLa(ArrayList listaURLa) {
        this.listaURLa = listaURLa;
    }

    public StringBuilder getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(StringBuilder sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public boolean isSpremljen() {
        return spremljen;
    }

    public void setSpremljen(boolean spremljen) {
        this.spremljen = spremljen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getVrijemeBrisanja() {
        return vrijemeBrisanja;
    }

    public void setVrijemeBrisanja(Date vrijemeBrisanja) {
        this.vrijemeBrisanja = vrijemeBrisanja;
    }
}
