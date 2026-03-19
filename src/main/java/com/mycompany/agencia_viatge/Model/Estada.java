/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.mycompany.agencia_viatge.Model.IDS.*;

/**
 *
 * @author alumne
 */
@Entity//<- Com que es tracta d'una taula, hem d'especifcar l'annotació entity
@Table(name = "estada")//<-- Aqui especifiquem el nom de la taula, no fa falta, però es per si volem anomenar la taula diferent
@IdClass(Estada_id.class) //Com que es tracta d'una taula amb ID compostos, hem de crear una classe separada per gestionar-los.
//La diferencia entre id class i embedded id es la forma de cridar-la en la query
/*
id class -> select estada from estada where estada.ID = ?
embedded -> select estada from estada where estada.estada_id.id = ?

 */
public class Estada {

    @Id
    /*Com que aquesta classe reb el id de l'annotació idClass, hem de reespecificar els ids, però si hagues utilitzant un embedded, només hauria de cridar
    hauriem de crear una instancia de la classe id i indicar que es el ID.
    @EmbeddedId
    private Reserva_activitat_id id;
     */
    @ManyToOne() //Indiquem que aquesta esta relacionada amb una altra taula
    @JoinColumn(name = "id_allotjament")
    private Allotjament allotjament;

    @Id
    @ManyToOne() //Indiquem que aquesta esta relacionada amb una altra taula
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    private LocalDate data_inici;
    private LocalDate data_fi;

    private double preuTotal;

    public Estada() {
    }

    public Estada(Allotjament allotjament, Reserva reserva, LocalDate data_inici, LocalDate data_fi, double preuTotal) {
        this.allotjament = allotjament;
        this.reserva = reserva;
        this.data_inici = data_inici;
        this.data_fi = data_fi;
        this.preuTotal = preuTotal;
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

    public double getPreuTotal() {
        return preuTotal;
    }

    public void setPreuTotal(double preuTotal) {
        this.preuTotal = preuTotal;
    }
}
