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
public class AllotjamentDAO {

    private SessionFactory sf; //Com valem mantenir una mateixa sessio, fem que els DAO s'han de declarar la variable Session

    public AllotjamentDAO(SessionFactory em) { //Realitzem el constructor perque rebi la sessio
        this.sf = em;
    }

    public void crearAllotjament(String nom, String lloc, String tipus, Double preu) {
        Transaction transaccio = null; //fem una referncia de transacció per separar la sessio de la transacció
        try (Session em = sf.openSession()) {
            System.out.println("Començant introducció...");
            transaccio = em.beginTransaction(); //emmagatzem la transacció i la començem al mateix temps
            Allotjament allotjament = new Allotjament(); //Creem una instancia de allotjament per poder assignar els valors
            System.out.println("Introduint nom...");
            allotjament.setNom(nom);
            System.out.println("Introduint lloc...");
            allotjament.setLloc(lloc);
            System.out.println("Introduint tipus...");
            allotjament.setTipus(tipus);
            System.out.println("Introduint preu...");
            allotjament.setPreu_per_nit(preu);
            em.persist(allotjament); //Una vegada que ja hem pogut assignar els valors, fem que la sessio faci una persistencia de la classe
            transaccio.commit(); //Finalemnte, haurme de fer un commit per assegurarnos que funciona
            System.out.println("Allotjament introduit");
        } catch (Exception e) {
            if (transaccio != null) { //primer ens assegurem que s'hagi creat correctament una transacció
                transaccio.rollback(); //Fem un rollback a la situacio que s'ha generat
                transaccio.commit(); //fem un commit, per netegar qualsevol informació que s'ha creat que no hauria
            }
            System.out.println("!!Ha hagut un error: " + e.getMessage());
        }
    }

    public ArrayList<Allotjament> mostrarTotsAllotjament() {
        ArrayList<Allotjament> totsAllotjament = new ArrayList<>();
        try(Session em = sf.openSession()) {
            String queryString = String.format("SELECT e FROM %s e", Allotjament.class.getSimpleName()); //aixó es una sentencia hql
            Query query = em.createQuery(queryString); //per executar la sentencia, hem de guarda-ho en una variable tipus query
            var resultList = query.getResultList(); //Com que aquesta retorna una llista, la hem de quardar en un result list 
            for (Object obj : resultList) {
                totsAllotjament.add((Allotjament) obj); //Afegim tots el resultats al array
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
        }

        return totsAllotjament;
    }

    public Allotjament buscarAllotjament(int id) {
        Allotjament allotjament_trobat = null;
        try(Session em = sf.openSession()) {
            allotjament_trobat = em.find(Allotjament.class, id); //busquem a partir de la clase qui conicideix amb el id
        } catch (Exception e) {
            allotjament_trobat = null;
            System.out.println("==No s'ha trobat==");
        }
        return allotjament_trobat;
    }

    public void eliminarAllotjament(Allotjament allotjamentActual) {
        Transaction transaccio = null;  //fem una referncia de transacció per separar la sessio de la transacció
        try(Session em = sf.openSession()) {
            System.out.println("Começant eliminació...");
            transaccio = em.beginTransaction();  //emmagatzem la transacció i la començem al mateix temps
            //En utilitzar sessio (hibernate) i no entity manager (jakarta), podem eliminar una entitat detached sense problema.
            em.remove(allotjamentActual); //Per eliminar-ho, hem de fer referncia a la sessio i a la funció remove, i li donem la instancia
            //A continuació, sempre hem de fer un flush i un clear abans de fer el commit per assegurarnos que no quedi informacio antiga
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("Allotjament eliminat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut eliminar l'allotjament: " + e.getMessage());
        }
    }

    public void actualizarAllotjament(Allotjament allotjamentActual) {
        Transaction transaccio = null;
        try(Session em = sf.openSession()) {
            System.out.println("Começant actualització...");
            transaccio = em.beginTransaction();  //emmagatzem la transacció i la començem al mateix temps
            em.merge(allotjamentActual);  //Per actualitzar-ho, hem de fer referncia a la sessio i a la funció merge, i li donem la instancia i combinem les dues entitats
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
            System.out.println("!!Ha hagut un error i no s'ha pogut actualizar l'allotjament: " + e.getMessage());
        }
    }
}
