/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.DAO;

import com.mycompany.agencia_viatge.Model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author alumne
 */
public class Reserva_activitatDAO {

    private SessionFactory sf;  //Com valem mantenir una mateixa sessio, fem que els DAO s'han de declarar la variable Session

    public Reserva_activitatDAO(SessionFactory em) {  //Realitzem el constructor perque rebi la sessio
        this.sf = em;
    }

    public void crearReserva_activitat(Activitat activ, Reserva reserva, LocalDate ini, LocalDate fi, Double preu) {
        Transaction transaccio = null;
        try (Session em = sf.openSession()) {
            System.out.println("Començant introducció...");
            Reserva_activitat reserva_activitat = new Reserva_activitat();
            transaccio = em.beginTransaction();
            System.out.println("Relacionat activitats...");
            reserva_activitat.setActivitat(activ);
            System.out.println("Relacionant reserva...");
            reserva_activitat.setReserva(reserva);
            System.out.println("Introduint dates...");
            reserva_activitat.setData_inici(ini);
            reserva_activitat.setData_fi(fi);
            System.out.println("Introduint preu...");
            reserva_activitat.setPreuTotal(preu);
            em.persist(reserva_activitat);
            System.out.println("Reserves i activitats relacionades");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error: " + e.getMessage());
        }
    }

    public ArrayList<Reserva_activitat> mostrarTotsReserva_activitat() {
        ArrayList<Reserva_activitat> totsReserva_activitat = new ArrayList<>();
        try (Session em = sf.openSession()) {
            //aixo es una sentencia HQL
            String queryString = String.format("SELECT e FROM %s e", Reserva_activitat.class.getSimpleName());
            Query query = em.createQuery(queryString);
            var resultList = query.getResultList();
            for (Object obj : resultList) {
                totsReserva_activitat.add((Reserva_activitat) obj); //Afegim tots el resultats al array
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
        }

        return totsReserva_activitat;
    }

    public Reserva_activitat buscarReserva_activitat(int id) {
        Reserva_activitat reserva_activitat_trobat = null;
        try (Session em = sf.openSession()) {
            reserva_activitat_trobat = em.find(Reserva_activitat.class, id); //busquem a partir de la clase qui conicideix amb el id
        } catch (Exception e) {
            reserva_activitat_trobat = null;
            System.out.println("==No s'ha trobat==");
        }
        return reserva_activitat_trobat;
    }

    public void eliminarReserva_activitat(Reserva_activitat reserva_activitatActual) {
        Transaction transaccio = null;
        try (Session em = sf.openSession()) {
            System.out.println("Começant eliminació...");
            transaccio = em.beginTransaction();
            //En utilitzar sessio (hibernate) i no entity manager (jakarta), podem eliminar una entitat detached sense problema.
            em.remove(reserva_activitatActual);
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("Reserva_activitat eliminat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut eliminar l'reserva_activitat: " + e.getMessage());
        }
    }

    public void actualizarReserva_activitat(Reserva_activitat reserva_activitatActual) {
        Transaction transaccio = null;
        try (Session em = sf.openSession()) {
            System.out.println("Começant actualització...");
            transaccio = em.beginTransaction();
            em.merge(reserva_activitatActual); //Combinem les dues entitats
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("S'ha actualitzat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut actualizar l'reserva_activitat: " + e.getMessage());
        }
    }
}
