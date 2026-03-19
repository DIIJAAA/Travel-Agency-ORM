/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode; //no podem importar la biblioteca amb annotations.*, perque hi ha annotacions de jakarta que confliqueten amb les annotacions de hibernate

/**
 *
 * @author alumne
 */
@Entity//<- Com que es tracta d'una taula, hem d'especifcar l'annotació entity
@Table(name = "reserva")//<-- Aqui especifiquem el nom de la taula, no fa falta, però es per si volem anomenar la taula diferent
public class Reserva {

    @Id
    //com que es un id normal, només es suficient amb la annotació id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //<- Aqui especifiquem que sigui autonumeric, hi ha difernts verions, pero aquesta es la més semblant al auto_increment de sql ja que genera un valor automaticmanet per cada instancia
    private int id_reserva;

    @ManyToOne() //Indiquem que aquesta esta relacionada amb una altra taula amb una notacio m:1, una reserva esta relacionada amb un client pero un client pot tindre mutiples reservas
    @JoinColumn(name = "dni") //Especifiquem quina columna volem relacionar-ho
    private Client client;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL) //Indiquem que te relacio amb una altra taula i quina taula ha de tindre la FK, en aquest cas, es l'altra
    //Hi ha diferents tipus de casacade, aquesta realitza totes, en el sentit de que inserta, eliminar, actualita i mante coherencia
    @Fetch(FetchMode.SELECT) //<- el fetch mode especifica que quan busquem aquesta instancia a la base de dades, de quina manera busca les taules relacionades, en aquesta mode fa un select en comptes d'un join
    private List<Estada> estades; //Hibernate retorna persitence.bag, llavors no pots posar-ho en un arralist que necessita una classe especifica, i una llista es lo suficientment abstracta per acceptar aquest tipus de classe

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)//El mateix que a dalt
    @Fetch(FetchMode.SELECT)
    private List<Reserva_activitat> reserva_activitat;

    private LocalDate data_inici;
    private LocalDate data_fi;
    private int persones_total;
    private double preu_total;

    public Reserva() {//Totes les classes necesiten una clase buida per gestionar-ho de manera més senzilla
    }

    public Reserva(int id_reserva, Client client, List<Estada> estades, List<Reserva_activitat> reserva_activitat, LocalDate data_inici, LocalDate data_fi, int persones_total, double preu_total) {
        this.id_reserva = id_reserva;
        this.client = client;
        this.estades = estades;
        this.reserva_activitat = reserva_activitat;
        this.data_inici = data_inici;
        this.data_fi = data_fi;
        this.persones_total = persones_total;
        this.preu_total = preu_total;
    }//Una altra per indicar quins camps pot ser omplet per hibernate

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getData_inici() {
        return data_inici;
    }

    public void setData_inici(LocalDate data_inici) {
        this.data_inici = data_inici;
    }

    public LocalDate getData_fi() {
        return data_fi;
    }

    public void setData_fi(LocalDate data_fi) {
        this.data_fi = data_fi;
    }

    public int getPersones_total() {
        return persones_total;
    }

    public void setPersones_total(int persones_total) {
        this.persones_total = persones_total;
    }

    public double getPreu_total() {
        return preu_total;
    }

    public void setPreu_total(double preu_total) {
        this.preu_total = preu_total;
    }

    public List<Estada> getEstades() {
        return estades;
    }

    public void setEstades(List<Estada> estades) {
        this.estades = estades;
    }

    public List<Reserva_activitat> getReserva_activitat() {
        return reserva_activitat;
    }

    public void setReserva_activitat(List<Reserva_activitat> reserva_activitat) {
        this.reserva_activitat = reserva_activitat;
    }

    private String estades() {
        String resultat = "";
        if (estades.isEmpty()) {
            resultat = "Sense allotjaments contractades";
        } else {
            resultat = "Allotjaments: ";
            for (Estada estade : estades) {
                resultat += estade.getAllotjament().getNom() + ", " + estade.getPreuTotal() + "€";
            }
            resultat += "\nTotal - " + estades.size();
        }
        return resultat;
    }

    private String activitats() {
        String resultat = "";
        if (reserva_activitat.isEmpty()) {
            resultat = "Sense activitats contractades";
        } else {
            resultat = "Activitats: ";
            for (Reserva_activitat act : reserva_activitat) {
                resultat += "\n" + act.getActivitat().getNom() + ", " + act.getPreuTotal() + "€";
            }
            resultat += "\nTotal - " + reserva_activitat.size();
        }
        return resultat;
    }

    @Override
    public String toString() {
        return "Reserva #" + id_reserva + " - #" + client.getDni() + "\nDe " + data_inici + " a " + data_fi + "\nPersones total: " + persones_total + "\nPreu total: " + preu_total + "€\n" + estades() + "\n" + activitats();
    }

}
