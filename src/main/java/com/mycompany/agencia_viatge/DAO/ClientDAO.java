/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.DAO;

import com.mycompany.agencia_viatge.Model.*;
import jakarta.persistence.*;
import java.sql.*;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author alumne
 */
public class ClientDAO {

    private SessionFactory sf; //Com valem mantenir una mateixa sessio, fem que els DAO s'han de declarar la variable Session

    public ClientDAO(SessionFactory em) {
        this.sf = em;  //Realitzem el constructor perque rebi la sessio
    }

    public void crearClient(String nom, String cognom, String email, String dni, String telefon) {
        Transaction transaccio = null; //fem una referncia de transacció per separar la sessio de la transacció
        try (Session em = sf.openSession()) {
            System.out.println("Començat introducció...");
            transaccio = em.beginTransaction(); //Emmagtzem la transaccio per poder fer el rollback
            var client = new Client(); //Creem una instancia de client per poder assignar els valors
            System.out.println("Introduint DNI...");
            client.setDni(dni);
            System.out.println("Introduint nom...");
            client.setNom(nom);
            System.out.println("Introduint cognom...");
            client.setCognom(cognom);
            System.out.println("Introduint email...");
            client.setEmail(email);
            System.out.println("Introduint telèfon...");
            client.setTelefon(telefon);
            System.out.println("Intentar introduir client...");
            em.persist(client); //Una vegada que ja hem pogut assignar els valors, fem que la sessio faci una persistencia de la classe
            transaccio.commit(); //Finalemnte, haurme de fer un commit per assegurarnos que funciona
            System.out.println("Client introduit");
        } catch (Exception e) {
            if (transaccio != null) { //Si no es null, retornara al començament
                transaccio.rollback(); //Fem un rollback a la situacio que s'ha generat
                transaccio.commit(); //fem un commit, per netegar qualsevol informació que s'ha creat que no hauria
            }
            System.out.println("!!Ha hagut un error i no s'ha afegit el client: " + e);
        }
    }

    public ArrayList<Client> mostrarTotsClient() {
        ArrayList<Client> totsClients = new ArrayList<>();
        try (Session em = sf.openSession()) {

            String queryString = String.format("SELECT e FROM %s e", Client.class.getSimpleName());//Seleccionem la taula clients
            Query query = em.createQuery(queryString); //Executem la busqueda
            var resultList = query.getResultList(); //Emmagatzem el resultat
            for (Object obj : resultList) {
                totsClients.add((Client) obj); //Afegim tots el resultats al array
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: " + e);
        }

        return totsClients;
    }

    public Client buscarClient(String dni) {
        Client client_trobat = null;
        try(Session em = sf.openSession()) {
            client_trobat = em.find(Client.class, dni); //busquem a partir de la clase qui conicideix amb el dni
        } catch (Exception e) {
            client_trobat = null;
            System.out.println("Ha hagut un error: " + e);
        }
        return client_trobat;
    }

    public void eliminarClient(Client clientActual) {
        Transaction transaccio = null;
        try(Session em = sf.openSession()) {
            System.out.println("Começant eliminació...");
            transaccio = em.beginTransaction();
            //En utilitzar sessio (hibernate) i no entity manager (jakarta), podem eliminar una entitat detached sense problema.
            em.remove(clientActual); //Eliminar el Client i refescar la sessio
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("Client eliminar");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut eliminar al client: " + e.getMessage());
        }
    }

    public void actualizarClient(Client clientActual) {
        Transaction transaccio = null;
        try(Session em = sf.openSession()) {
            System.out.println("Começant actualització...");
            transaccio = em.beginTransaction();
            em.merge(clientActual); //Combinem les dues entitats
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("S'ha actualitzat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut actualizar el client: " + e.getMessage());
        }
    }
}
