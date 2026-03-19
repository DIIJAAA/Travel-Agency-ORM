/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.DAO;

import com.mycompany.agencia_viatge.Model.*;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author alumne
 */
public class ActivitatDAO {

    private SessionFactory sf; //Com valem mantenir una mateixa sessio, fem que els DAO s'han de declarar la variable Session

    public ActivitatDAO(SessionFactory em) { //Realitzem el constructor perque rebi la sessio
        this.sf = em;
    }

    public void crearActivitat(String nom, String lloc, String descripcio, Double preu) {
        Transaction transaccio = null; //fem una referncia de transacció per separar la sessio de la transacció
        try (Session em = sf.openSession()) {
            System.out.println("Començant introducció...");
            transaccio = em.beginTransaction(); //emmagatzem la transacció i la començem al mateix temps
            Activitat allotjament = new Activitat(); //Creem una instancia de activitat per poder assignar els valors
            System.out.println("Introduint nom...");
            allotjament.setNom(nom);
            System.out.println("Introduint lloc...");
            allotjament.setLloc(lloc);
            System.out.println("Introduint descripcio...");
            allotjament.setDescripcio(descripcio);
            System.out.println("Introduint preu...");
            allotjament.setPreu_per_persona(preu);
            em.persist(allotjament); //Una vegada que ja hem pogut assignar els valors, fem que la sessio faci una persistencia de la classe
            transaccio.commit(); //Finalemnte, haurme de fer un commit per assegurarnos que funciona
            System.out.println("Activitat introduit");
        } catch (Exception e) {
            if (transaccio != null) { //primer ens assegurem que s'hagi creat correctament una transacció
                transaccio.rollback(); //Fem un rollback a la situacio que s'ha generat
                transaccio.commit(); //fem un commit, per netegar qualsevol informació que s'ha creat que no hauria
            }
            System.out.println("!!Ha hagut un error: " + e.getMessage());
        }
    }

    public ArrayList<Activitat> mostrarTotesActivitats() {
        ArrayList<Activitat> totsAllotjament = new ArrayList<>();
        try (Session em = sf.openSession()) {
            String queryString = String.format("SELECT e FROM %s e", Activitat.class.getSimpleName()); //aixó es una sentencia hql
            Query query = em.createQuery(queryString); //per executar la sentencia, hem de guarda-ho en una variable tipus query
            var resultList = query.getResultList(); //Com que aquesta retorna una llista, la hem de quardar en un result list 
            for (Object obj : resultList) {
                totsAllotjament.add((Activitat) obj); //Afegim tots el resultats al array
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
        }

        return totsAllotjament;
    }

    public Activitat buscarActivitat(int id) {
        Activitat allotjament_trobat = null;
        try (Session em = sf.openSession();) {
            allotjament_trobat = em.find(Activitat.class, id); //busquem a partir de la clase qui conicideix amb el id
        } catch (Exception e) {
            allotjament_trobat = null;
            System.out.println("Ha hagut un error: " + e);
        }
        return allotjament_trobat;
    }

    public void eliminarActivitat(Activitat activitatActual) {
        Transaction transaccio = null;  //fem una referncia de transacció per separar la sessio de la transacció
        try (Session em = sf.openSession();) {
            System.out.println("Começant eliminació...");
            transaccio = em.beginTransaction(); //emmagatzem la transacció i la començem al mateix temps
            //En utilitzar sessio (hibernate) i no entity manager (jakarta), podem eliminar una entitat detached sense problema.
            em.remove(activitatActual); //Per eliminar-ho, hem de fer referncia a la sessio i a la funció remove, i li donem la instancia
            //A continuació, sempre hem de fer un flush i un clear abans de fer el commit per assegurarnos que no quedi informacio antiga
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("Activitat eliminat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut eliminar l'activitat: " + e.getMessage());
        }
    }

    public void actualitzarActivitat(Activitat activitatActual) {
        Transaction transaccio = null;
        try (Session em = sf.openSession()) {
            System.out.println("Começant actualització...");
            transaccio = em.beginTransaction();  //emmagatzem la transacció i la començem al mateix temps
            em.merge(activitatActual); //Per actualitzar-ho, hem de fer referncia a la sessio i a la funció merge, i li donem la instancia i combinem les dues entitats
            //A continuació, sempre hem de fer un flush i un clear abans de fer el commit per assegurarnos que no quedi informacio antiga
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("S'ha actualitzat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut actualizar l'activitat: " + e.getMessage());
        }
    }

}
