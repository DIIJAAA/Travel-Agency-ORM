/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.agencia_viatge.Model.IDS.*;

/**
 *
 * @author alumne
 */
@Entity
@Table(name = "allotjament")
public class Allotjament {

    @Id  //com que es un id normal, només es suficient amb la annotació id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Com que aquesta ID es de tipus string i no volem que sigui automatic, no fa falta que especifiquem una altra annotacio
    private int id_allotjament;

    private String nom;
    private String lloc;
    private String tipus;
    private Double preu_per_nit;

    @OneToMany(mappedBy = "allotjament", cascade = CascadeType.ALL)//Indiquem que te relacio amb una altra taula i quina taula ha de tindre la FK, en aquest cas, es l'altra
    //Hi ha diferents tipus de casacade, aquesta realitza totes, en el sentit de que inserta, eliminar, actualita i mante coherenci
    //Aqui no especiquem que sigui fetch perque no realiquem cap tipus de consulta enrte estades i allotjamnet que sigui necessaria
    private List<Estada> estades;//Hibernate retorna persitence.bag, llavors no pots posar-ho en un arralist que necessita una classe especifica, i una llista es lo suficientment abstracta per acceptar aquest tipus de classe

    public Allotjament() {
    }

    public Allotjament(String nom, String lloc, String tipus, Double preu_per_nit) {
        this.nom = nom;
        this.lloc = lloc;
        this.tipus = tipus;
        this.preu_per_nit = preu_per_nit;
    }

    public int getId_allotjament() {
        return id_allotjament;
    }

    public void setId_allotjament(int id_allotjament) {
        this.id_allotjament = id_allotjament;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLloc() {
        return lloc;
    }

    public void setLloc(String lloc) {
        this.lloc = lloc;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public Double getPreu_per_nit() {
        return preu_per_nit;
    }

    public void setPreu_per_nit(Double preu_per_nit) {
        this.preu_per_nit = preu_per_nit;
    }

    public List<Estada> getEstades() {
        return estades;
    }

    public void setEstades(List<Estada> estades) {
        this.estades = estades;
    }

    @Override
    public String toString() {
        return "#" + id_allotjament + " - " + nom + "-" + tipus + "\nLloc: " + lloc + "\nPreu: " + preu_per_nit + "€/Nit";
    }

}
