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
@Entity //Indiquem que sigui una entitat
@Table(name = "client") //I la linkquem al nom de la taula
public class Client {

    @Id //com que es un id normal, només es suficient amb la annotació id
    //Com que aquesta ID es de tipus string i no volem que sigui automatic, no fa falta que especifiquem una altra annotacio
    private String dni;
    
    
    private String nom;
    private String cognom;
    private String email;
    private String telefon;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE) //Indiquem que te relacio amb una altra taula i quina taula ha de tindre la FK, en aquest cas, es l'altra
    //aqui especifiquem que eliminen la reserva quan el eliminem, pero que no modifiqui la reserva per si.
    private List<Reserva> reserves = new ArrayList<>();

    public Client() {
    }

    public Client(String dni, String nom, String cognom, String email, String telefon) {
        this.dni = dni;
        this.nom = nom;
        this.cognom = cognom;
        this.email = email;
        this.telefon = telefon;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public List<Reserva> getReserves() {
        return reserves;
    }

    public void setReserves(List<Reserva> reserves) {
        this.reserves = reserves;
    }

    @Override
    public String toString() {
        return "Client: #" + dni + "\n" + cognom + ", " + nom + "\n Contacte - Email: " + email + " - Telefon:" + telefon;
    }

}
