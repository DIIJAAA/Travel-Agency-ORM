/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.Model.IDS;

import com.mycompany.agencia_viatge.Model.*;
//import jakarta.persistence.*; <-- En cas de ser embeddable, hem d'importar la biblioteca de jakarta
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author alumne
 */
//Com que aixo no és una tauala, no hem de especificar res, pero hem d'implementar la interficie serializable
//Com que aquesta taula rebrar l'annotació de idClass, no hem d'especificar cap annotació, però si utilitzesem EmbeddedId, hauriem d'activar el següent
//@Embeddable
public class Reserva_activitat_id implements Serializable  {

    private Reserva reserva;
    private Activitat activitat;

    public Reserva_activitat_id() {
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Activitat getActivitat() {
        return activitat;
    }

    public void setActivitat(Activitat activitat) {
        this.activitat = activitat;
    }

    public Reserva_activitat_id(Reserva reserva_id, Activitat activitat_id) {
        this.reserva = reserva_id;
        this.activitat = activitat_id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) { //Com que es un ID, hem de generar el equals per epecifiar quins camps és un ID compost.
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reserva_activitat_id other = (Reserva_activitat_id) obj;
        if (!Objects.equals(this.reserva, other.reserva)) {
            return false;
        }
        return Objects.equals(this.activitat, other.activitat);
    }
}
