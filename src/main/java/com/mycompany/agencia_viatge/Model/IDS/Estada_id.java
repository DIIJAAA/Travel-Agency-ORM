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
public class Estada_id implements Serializable {

    private Allotjament allotjament;
    private Reserva reserva;

    public Estada_id() {
    }

    public Estada_id(Allotjament allotjament, Reserva reserva) {
        this.allotjament = allotjament;
        this.reserva = reserva;
    }

    public Allotjament getAllotjament() {
        return allotjament;
    }

    public void setAllotjament(Allotjament allotjament) {
        this.allotjament = allotjament;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estada_id other = (Estada_id) obj;
        if (!Objects.equals(this.allotjament, other.allotjament)) {
            return false;
        }
        return Objects.equals(this.reserva, other.reserva);
    }

}
