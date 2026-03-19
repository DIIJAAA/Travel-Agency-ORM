/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge.DAO;

import com.mycompany.agencia_viatge.Model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.processing.Find;
import org.hibernate.query.Query;

/**
 *
 * @author alumne
 */
public class EstadaDAO {

    private SessionFactory sf; //Com valem mantenir una mateixa sessio, fem que els DAO s'han de declarar la variable Session
 
    public EstadaDAO(SessionFactory em) { //Realitzem el constructor perque rebi la sessio
        this.sf = em;
    }
    
    public void crearEstada(Allotjament allot, Reserva reserva, LocalDate ini, LocalDate fi, Double preu) {
        Transaction transaccio = null;
        try(Session em = sf.openSession()) {
            System.out.println("Començant introducció...");
            Estada estada = new Estada();
            transaccio = em.beginTransaction();
            System.out.println("Relacionat allotjament...");
            estada.setAllotjament(allot);
            System.out.println("Relacionant reserva...");
            estada.setReserva(reserva);
            System.out.println("Introduint dates...");
            estada.setData_inici(ini);
            estada.setData_fi(fi);
            System.out.println("Introduint preu...");
            estada.setPreuTotal(preu);
            em.persist(estada);
            transaccio.commit();
            System.out.println("Estades relacionades");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error: " + e.getMessage());
        }
    }

    public ArrayList<Estada> mostrarTotsEstada(Reserva reserva) {
        ArrayList<Estada> totsEstada = new ArrayList<>();
        try(Session em = sf.openSession()) {
            //aixo es una sentencia HQL, hem d'especifar les variables amb :, ej. :id per poder pasar despres el parametre
            var resultList = em.createQuery("select p from estada where id_reserva =: id").setParameter("id", reserva).getResultList();
            for(Object obj: resultList){
                totsEstada.add((Estada)obj);
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: "+e);
        }
        return totsEstada;
    }

    public ArrayList<Estada> mostrarTotsEstadaDates(LocalDate dataInici, LocalDate dataFi, Allotjament allotjament) {
        ArrayList<Estada> totsEstada = new ArrayList<>();
        try(Session em = sf.openSession()) {
            String busqueda = String.format("select e from %s e where allotjament =: id and (data_inici between :di and :df or data_fi between :di and :df)", Estada.class.getSimpleName());
            var query = em.createQuery(busqueda);
            //per poder buscar totes les estades que estan relacionades amb un allotjament, nomes es necessari pasarli la classe sencera per la forma que tenim posat al model, si en comptes de
            //de fer un refernica a una instancia fos text, hauriem de posar text no la classe
            query.setParameter("id", allotjament);
            query.setParameter("di", dataInici);
            query.setParameter("df", dataFi);
            var resultList = query.getResultList();
            for(Object obj: resultList){
                totsEstada.add((Estada)obj);
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: "+e);
        }
        return totsEstada;
    }
    
        public ArrayList<Estada> mostrarTotsEstadaDatesSenseReserva(LocalDate dataInici, LocalDate dataFi, Allotjament allotjament, Reserva reserva) {
        ArrayList<Estada> totsEstada = new ArrayList<>();
        try(Session em = sf.openSession()) {
            //aixo es una sentencia HQL, hem d'especifar les variables amb :, ej. :id per poder pasar despres el parametre, es poden reptir el mateix paramtre sempre i quan es repeteix el nom
            String busqueda = String.format("select e from %s e where allotjament =: id and reserva <> :res and (data_inici between :di and :df or data_fi between :di and :df)", Estada.class.getSimpleName());
            var query = em.createQuery(busqueda);
            //com es pot obseravar en la part a continuació el tema de pasar parametres
            query.setParameter("id", allotjament);
            query.setParameter("res", reserva);
            query.setParameter("di", dataInici);
            query.setParameter("df", dataFi);
            var resultList = query.getResultList();
            for(Object obj: resultList){
                totsEstada.add((Estada)obj);
            }
        } catch (Exception e) {
            System.out.println("Ha hagut un error: "+e);
        }
        return totsEstada;
    }
        
    public Estada buscarEstada(int id) {
        Estada estada_trobat = null;
        try(Session em = sf.openSession()) {
            estada_trobat = em.find(Estada.class, id); //busquem a partir de la clase qui conicideix amb el id
        } catch (Exception e) {
            estada_trobat = null;
            System.out.println("==No s'ha trobat==");
        }
        return estada_trobat;
    }

    public void eliminarEstada(Estada estadaActual) {
        Transaction transaccio = null;
        try(Session em = sf.openSession()) {
            System.out.println("Começant eliminació...");
            transaccio = em.beginTransaction();
            //En utilitzar sessio (hibernate) i no entity manager (jakarta), podem eliminar una entitat detached sense problema.
            em.remove(estadaActual);
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("Estada eliminat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut eliminar l'estada: " + e.getMessage());
        }
    }

    public void actualizarEstada(Estada estadaActual) {
        Transaction transaccio = null;
        try(Session em = sf.openSession()) {
            System.out.println("Começant actualització...");
            transaccio = em.beginTransaction();
            em.merge(estadaActual); //Combinem les dues entitats
            em.flush();
            em.clear();
            transaccio.commit();
            System.out.println("S'ha actualitzat");
        } catch (Exception e) {
            if (transaccio != null) {
                transaccio.rollback();
                transaccio.commit();
            }
            System.out.println("!!Ha hagut un error i no s'ha pogut actualizar l'estada: " + e.getMessage());
        }
    }
}
