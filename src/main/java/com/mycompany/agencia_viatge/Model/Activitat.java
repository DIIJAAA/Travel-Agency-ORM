/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alumne
 */
@Entity
@Table(name = "activitat")
public class Activitat {

    @Id //com que es un id normal, només es suficient amb la annotació id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_activitat;

    private String nom;
    private String descripcio;
    private String lloc;
    private Double preu_per_persona;

    @OneToMany(mappedBy = "activitat", cascade = CascadeType.ALL)//Indiquem que te relacio amb una altra taula i quina taula ha de tindre la FK, en aquest cas, es l'altra
    //Hi ha diferents tipus de casacade, aquesta realitza totes, en el sentit de que inserta, eliminar, actualita i mante coherenci
    //Aqui no especiquem que sigui fetch perque no realiquem cap tipus de consulta enrte estades i allotjamnet que sigui necessaria
    private List<Reserva_activitat> reserva_activitat;//Hibernate retorna persitence.bag, llavors no pots posar-ho en un arralist que necessita una classe especifica, i una llista es lo suficientment abstracta per acceptar aquest tipus de classe

    public Activitat() {
    }

    public Activitat(String nom, String descripcio, String lloc, Double preu_per_persona) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.lloc = lloc;
        this.preu_per_persona = preu_per_persona;
    }

    public int getId_activitat() {
        return id_activitat;
    }

    public void setId_activitat(int id_activitat) {
        this.id_activitat = id_activitat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getLloc() {
        return lloc;
    }

    public void setLloc(String lloc) {
        this.lloc = lloc;
    }

    public Double getPreu_per_persona() {
        return preu_per_persona;
    }

    public void setPreu_per_persona(Double preu_per_persona) {
        this.preu_per_persona = preu_per_persona;
    }

    public List<Reserva_activitat> getReserva_activitat() {
        return reserva_activitat;
    }

    public void setReserva_activitat(List<Reserva_activitat> reserva_activitat) {
        this.reserva_activitat = reserva_activitat;
    }

    @Override
    public String toString() {
        return "#" + id_activitat + ": " + nom + " - Lloc: " + lloc + "\n" + descripcio + "\nPreu: " + preu_per_persona + "€/persona";
    }

}
