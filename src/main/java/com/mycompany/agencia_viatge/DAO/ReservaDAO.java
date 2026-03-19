/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.DAO;

import com.mycompany.agencia_viatge.Model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author alumne
 */
public class ReservaDAO {

    private SessionFactory sf; //Com valem mantenir una mateixa sessio, fem que els DAO s'han de declarar la variable Session

    public ReservaDAO(SessionFactory em) { //Realitzem el constructor perque rebi la sessio
        this.sf = em;
    }

    public void crearReserva(Client client, LocalDate data_inici, LocalDate data_fi, int persones_total, ArrayList<Estada> estades, ArrayList<Reserva_activitat> activitats) {
        Transaction trn = null;
        double preuTotal = 0;
        ArrayList<Reserva_activitat> activitatsAmbReserva = new ArrayList<>(); //Per evitar errors, fem dos arralist separades de l'anterior per poder introduirla a la reserva amb la informació correcta
        ArrayList<Estada> estadesAmbReserva = new ArrayList<>();

        try(Session em = sf.openSession()) {
            trn = em.beginTransaction();

            System.out.println("Comencant transacio...");
            Reserva reserva = new Reserva();

            System.out.println("Introduint client...");
            reserva.setClient(client);

            System.out.println("Introduint dates...");
            reserva.setData_inici(data_inici);
            reserva.setData_fi(data_fi);

            System.out.println("Introduint persones...");
            reserva.setPersones_total(persones_total);

            System.out.println("Introduint estades...");
            for (Estada estade : estades) {
                estade.setReserva(reserva); //Hem d'assignar indiviudalment a cada estada el id de la reserva o sino donara un error de que no pots introduir valors nulls
                preuTotal += estade.getPreuTotal(); //<-- Augmentem el preu de la reserva
                estadesAmbReserva.add(estade); //finalemnt, l'assignem a l'array list les estades amb el id reserva assignada
            }
            reserva.setEstades(estades); //assigenm la llista estades amb el array d'estades

            System.out.println("Introduint activitats...");
            for (Reserva_activitat activitat : activitats) {
                activitat.setReserva(reserva);//Hem d'assignar indiviudalment a cada estada el id de la reserva o sino donara un error de que no pots introduir valors nulls
                preuTotal += activitat.getPreuTotal();//<-- Augmentem el preu de la reserva
                activitatsAmbReserva.add(activitat); //finalemnt, l'assignem a l'array list les activitat amb el id reserva assignada
            }
            reserva.setReserva_activitat(activitats); //assigenm la llista activitat amb el array d'activitat
            System.out.println("Introduint preu...");

            reserva.setPreu_total(preuTotal);
            System.out.println("Realitzant reserva...");

            em.persist(reserva); //finament, creara la reserva
            //gracies al cascade que tenim en el model de reserva, no fa falta que manualment creem les estades o activitats ja que se realitzaran automaticmanet
            trn.commit(); //finalment realitzarem el commit
            System.out.println("S'ha realitzat la reserva correctament");
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
            try { //aqui posem doble try-catch perque durant un temps, donaba error tant la transacció com el commit i haviem de gestionar-los diferent
                if (trn != null) { //primer ens assegurem que s'hagi creat correctament una transacció
                    trn.rollback(); //Fem un rollback a la situacio que s'ha generat
                    trn.commit(); //fem un commit, per netegar qualsevol informació que s'ha creat que no hauria
                }
            } catch (Exception e1) {
                System.out.println("Ha hagut un error: " + e1);
            }

        }
    }

    public ArrayList<Reserva> mostrarTotesReserva(Client client) {
        ArrayList<Reserva> res = new ArrayList<>();
        try(Session em = sf.openSession()) {
            //aixó es una sentencia hql
            String queryString = String.format("SELECT e FROM %s e WHERE client = :id", Reserva.class.getSimpleName()); 
            Query query = em.createQuery(queryString);
            query.setParameter("id", client);
            var resultList = query.getResultList();
            for (Object obj : resultList) {
                res.add((Reserva) obj); //Afegim tots el resultats al array
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
        }
        return res;
    }

    public Reserva mostrarReserva(int id) {
        Reserva res = null;
        try(Session em = sf.openSession()) {
            res = em.find(Reserva.class, id);  //busquem a partir de la clase qui conicideix amb el id
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
        }
        return res;
    }

    public void eliminar(Reserva reserva) {
        Transaction trn = null; //fem una referncia de transacció per separar la sessio de la transacció
        try(Session em = sf.openSession()) {
            System.out.println("Començant eliminació...");
            trn = em.beginTransaction(); //emmagatzem la transacció i la començem al mateix temps
            //En utilitzar sessio (hibernate) i no entity manager (jakarta), podem eliminar una entitat detached sense problema.
            em.remove(reserva); //Per eliminar-ho, hem de fer referncia a la sessio i a la funció remove, i li donem la instancia
             //A continuació, sempre hem de fer un flush i un clear abans de fer el commit per assegurarnos que no quedi informacio antiga
            em.flush();
            em.clear();
            trn.commit();
            System.out.println("Reserva eliminada");
        } catch (Exception e) {
            if (trn != null) {
                trn.rollback();
                trn.commit();
            }
            System.out.println("Ha hagut un error: " + e);
        }
    }

    public void actualitzar(Reserva reserva) {
        Transaction trn = null;
        try(Session em = sf.openSession()) {
            System.out.println("Començant actualització...");
            trn = em.beginTransaction(); //emmagatzem la transacció i la començem al mateix temps
            em.merge(reserva);//Per actualitzar-ho, hem de fer referncia a la sessio i a la funció merge, i li donem la instancia i combinem les dues entitats
            //A continuació, sempre hem de fer un flush i un clear abans de fer el commit per assegurarnos que no quedi informacio antiga
            em.flush();
            em.clear();
            trn.commit();
            System.out.println("S'ha actualitzat");
        } catch (Exception e) {
            if (trn != null) {
                trn.rollback();
                trn.commit();
            }
            System.out.println("Ha hagut un error: " + e);
        }
    }
}
