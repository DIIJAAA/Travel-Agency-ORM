/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.agencia_viatge;

import com.mycompany.agencia_viatge.Model.*;
import com.mycompany.agencia_viatge.DAO.*;
import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author alumne
 */
public class ModelControllador {
    
    //creem referncies de la sessio i el scanner, d'aquesta manera tots els daos utilitzen el mateix
    private SessionFactory sf;
    private Scanner reader;
    private ClientDAO clientDAO;
    private ActivitatDAO activitatDAO;
    private ReservaDAO reservaDAO;
    private AllotjamentDAO allotjamentDAO;
    private EstadaDAO estadaDAO;
    private Reserva_activitatDAO resactDAO;
    
    public ModelControllador(SessionFactory em, Scanner reader) {
        this.sf = em;
        this.reader = reader;
        clientDAO = new ClientDAO(em);
        activitatDAO = new ActivitatDAO(em);
        reservaDAO = new ReservaDAO(em);
        allotjamentDAO = new AllotjamentDAO(em);
        estadaDAO = new EstadaDAO(em);
        resactDAO = new Reserva_activitatDAO(em);
    }
    
    public void crearClient() {
        System.out.println("===Crear client===");
        System.out.print("Nom: ");
        String nom = reader.next();
        
        System.out.print("Cognom: ");
        String cognom = reader.next();
        
        System.out.print("Email: ");
        String email = reader.next();
        
        System.out.print("DNI: ");
        String dni = reader.next();
        
        System.out.print("Telefon: ");
        String telefon = reader.next();
        
        if (clientDAO.buscarClient(dni) == null) { //crideem al dao de client, aixi ens assegurem que aquest dni no exiteixi
            clientDAO.crearClient(nom, cognom, email, dni, telefon);
        } else {
            System.out.println("Aquest DNI ja es troba registrat");
        }
    }
    
    public Client buscarClient() {
        System.out.println("===Buscar client===");
        System.out.print("Busca al client per DNI: ");
        String dni = reader.next();
        return clientDAO.buscarClient(dni);//crideem al dao de client i ens retorna el client
    }
    
    public void mostrarClient() {
        System.out.println("===Mostrar tots els clients===");
        if (clientDAO.mostrarTotsClient().isEmpty()) { //busquem tots els client, si esta buit en mostrara un missatge, si hi ha, mostrara tots 
            System.out.println("=No hi ha clients=");
        } else {
            for (Client clients : clientDAO.mostrarTotsClient()) {
                System.out.println(clients);
            }
        }
    }
    
    public void eliminarClient() {
        System.out.println("===Eliminar client===");
        System.out.print("Busca al client per DNI: ");
        String dni = reader.next();
        Client clientActual = clientDAO.buscarClient(dni); //per poder eliminar, primer ens hem d'assegurar que el client exteixi abans de fer una crida al dao d'eliminar
        if (clientActual == null) {
            System.out.println("=No s'ha trobat el client=");
        } else {
            clientDAO.eliminarClient(clientActual);
        }
    }
    
    public void updateClient() { //el metode update es molt similar al de crear, només que aqui, en comptes de veure que exiteixi, mirem que no exitxi per no deixar editar
        System.out.println("===Actualitzar client===");
        System.out.print("Busca al client per DNI: ");
        String dni = reader.next();
        Client clientActual = clientDAO.buscarClient(dni);
        if (clientActual == null) {
            System.out.println("=No s'ha trobat el client=");
        } else {
            System.out.println("===Actualitzar client===");
            System.out.print("Nom: (" + clientActual.getNom() + ")");
            clientActual.setNom(reader.next());
            
            System.out.print("Cognom: (" + clientActual.getCognom() + ")");
            clientActual.setCognom(reader.next());
            
            System.out.print("Email: (" + clientActual.getEmail() + ")");
            clientActual.setEmail(reader.next());
            
            System.out.print("Telefon: (" + clientActual.getTelefon() + ")");
            clientActual.setTelefon(reader.next());
            
            clientDAO.actualizarClient(clientActual);
        }
    }
    
    public void crearAllotjament() {
        System.out.println("===Crear Allotjament===");
        
        System.out.print("Nom: ");
        String nom = reader.next();
        
        System.out.print("Lloc: ");
        String lloc = reader.next();
        
        System.out.print("Tipus: ");
        String tipus = reader.next();
        
        double preu = -1;
        while (preu == -1) {
            try {
                System.out.print("Preu: ");
                preu = reader.nextDouble();
                if (preu < 0) { //comporbem que el preu no sigui menor de 0
                    throw new Exception("Preu negatiu");
                }
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("El valor del preu es invàlid: " + e.getMessage());
                preu = -1;
                
            }
        }
        
        allotjamentDAO.crearAllotjament(nom, lloc, tipus, preu);
    }
    
    public Allotjament buscarAllotjament() {
        System.out.println("===Buscar Allotjament===");
        
        int id = -1;
        while (id == -1) {
            try {
                System.out.print("Busca al allotjament per ID: ");
                id = reader.nextInt();
                if (id < 1) { //ens assegurem que el id no sigui menor que 0 ja que hibernate no crea ids 0
                    throw new Exception("ID negatiu");
                }
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("El valor del ID es invàlid (només números positius sense negatius, sense incloure 0): " + e.getMessage());
                id = -1;
            }
        }
        return allotjamentDAO.buscarAllotjament(id);
    }
    
    public void mostrarTotsAllotjaments() {
        System.out.println("===Mostrar tots Allotjament===");
        if (allotjamentDAO.mostrarTotsAllotjament().isEmpty()) {
            System.out.println("--No hi ha allotjaments--");
        } else {
            for (Allotjament allotjament : allotjamentDAO.mostrarTotsAllotjament()) {
                System.out.println(allotjament);
            }
        }
    }
    
    public void eliminarAllotjament() {
        System.out.println("===ELiminar allotjament===");
        
        int id = -1;
        while (id == -1) {
            try {
                System.out.print("Busca al allotjament per ID: ");
                id = reader.nextInt();
                if (id < 1) { //ens assegurem que el id no sigui menor que 0 ja que hibernate no crea ids 0
                    throw new Exception("ID negatiu");
                }
                if (allotjamentDAO.buscarAllotjament(id) == null) { //surt un missatge si no hi ha allotjament per eliminar
                    System.out.println("--Allotjament no s'ha trobat--");
                } else {
                    allotjamentDAO.eliminarAllotjament(allotjamentDAO.buscarAllotjament(id));
                }
                
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("El valor del ID es invàlid (només números positius sense negatius, sense incloure 0): " + e.getMessage());
                id = -1;
            }
        }
    }
    
    public void modificarAllotjament() {//semblant al de crear
        System.out.println("===Modificar allotjament===");
        Allotjament allo = buscarAllotjament();
        if (allo == null) {
            System.out.println("Allotjament no trobat");
        } else {
            System.out.print("Nom (" + allo.getNom() + "): ");
            allo.setNom(reader.next());
            
            System.out.print("Lloc (" + allo.getLloc() + "): ");
            allo.setLloc(reader.next());
            
            System.out.print("Tipus (" + allo.getTipus() + "): ");
            allo.setTipus(reader.next());
            
            double preu = -1;
            while (preu == -1) {
                try {
                    System.out.print("Preu (" + allo.getPreu_per_nit() + "): ");
                    preu = reader.nextDouble();
                    if (preu < 0) {
                        throw new Exception("Preu negatiu");
                    }
                } catch (Exception e) {
                    reader.nextLine();
                    System.out.println("El valor del preu es invàlid: " + e.getMessage());
                    preu = -1;
                    
                }
            }
            allo.setPreu_per_nit(preu);
            
            allotjamentDAO.actualizarAllotjament(allo);
        }
    }
    
    public void crearActivitat() {
        System.out.println("===Crear Activitat===");
        
        System.out.print("Nom: ");
        String nom = reader.next();
        
        System.out.print("Lloc: ");
        String lloc = reader.next();
        
        System.out.print("Descripcio: ");
        String descripcio = reader.next();
        
        double preu = -1;
        while (preu == -1) {
            try {
                System.out.print("Preu: ");
                preu = reader.nextDouble();
                if (preu < 0) { //ens assegurem que el preu no sigui menor que 0
                    throw new Exception("Preu negatiu");
                }
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("El valor del preu es invàlid: " + e.getMessage());
                preu = -1;
                
            }
        }
        
        activitatDAO.crearActivitat(nom, lloc, descripcio, preu);
    }
    
    public Activitat buscarActiviat() {
        System.out.println("===Buscar Activitat===");
        
        int id = -1;
        while (id == -1) {
            try {
                System.out.print("Busca al Activitat per ID: ");
                id = reader.nextInt();
                if (id < 1) {//ens assegurem que el id no sigui menor que 0 ja que hibernate no crea ids 0
                    throw new Exception("ID negatiu");
                }
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("El valor del ID es invàlid (només números positius sense negatius, sense incloure 0): " + e.getMessage());
                id = -1;
            }
        }
        return activitatDAO.buscarActivitat(id);
    }
    
    public void mostrarTotesActivitat() {
        System.out.println("===Mostrar totes les activitats===");
        if (activitatDAO.mostrarTotesActivitats().isEmpty()) {
            System.out.println("--No hi ha activitats--");
        } else {
            for (Activitat activitat : activitatDAO.mostrarTotesActivitats()) {
                System.out.println(activitat);
            }
        }
    }
    
    public void eliminarActivitat() {
        System.out.println("===ELiminar activitat===");
        int id = -1;
        while (id == -1) {
            try {
                System.out.print("Busca al activitat per ID: ");
                id = reader.nextInt();
                if (id < 1) {//ens assegurem que el id no sigui menor que 0 ja que hibernate no crea ids 0
                    throw new Exception("ID negatiu");
                }
                if (activitatDAO.buscarActivitat(id) == null) {//evitem que no pugui eliminar una activitat que no exieteix
                    System.out.println("--Activitat no s'ha trobat--");
                } else {
                    activitatDAO.eliminarActivitat(activitatDAO.buscarActivitat(id));
                }
                
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("El valor del ID es invàlid (només números positius sense negatius, sense incloure 0): " + e.getMessage());
                id = -1;
            }
        }
    }
    
    public void modificarActivitats() {//semblant al de crear
        System.out.println("===Modificar Activitat===");
        Activitat act = buscarActiviat();
        
        if (act == null) {
            System.out.println("Aquesta activitat no exiteix");
        } else {
            System.out.print("Nom (" + act.getNom() + "): ");
            act.setNom(reader.next());
            
            System.out.print("Lloc(" + act.getLloc() + "): ");
            act.setLloc(reader.next());
            
            System.out.print("Descripcio (" + act.getDescripcio() + "): ");
            act.setDescripcio(reader.next());
            
            double preu = -1;
            while (preu == -1) {
                try {
                    System.out.print("Preu (" + act.getPreu_per_persona() + "): ");
                    preu = reader.nextDouble();
                    if (preu < 0) {
                        throw new Exception("Preu negatiu");
                    }
                } catch (Exception e) {
                    reader.nextLine();
                    System.out.println("El valor del preu es invàlid: " + e.getMessage());
                    preu = -1;
                    
                }
            }
            act.setPreu_per_persona(preu);
            activitatDAO.actualitzarActivitat(act);
        }
        
    }
    
    public void crearReserva() {
        Client client = buscarClient();
        if (client == null) {
            System.out.println("!!Aquest client no exiteix");
        } else {
            System.out.println("Client trobat");
            System.out.println("===Crear reserva===");
            int numPersones = -1;
            LocalDate reservaInici = LocalDate.now();
            LocalDate reservaFi = LocalDate.now();
            
            while (numPersones == -1) {
                try {
                    System.out.println("Introdueix la data inci de la reserva: ");
                    reservaInici = comprobarDates();
                    System.out.println("Introdueix la data fi de la reserva: ");
                    reservaFi = comprobarDates();
                    while (reservaFi.isBefore(reservaInici)) { //ens assegurem que la data fi no sigui abans que la data d'inici
                        System.out.println("No pots introduir una data fi abans de la data inici");
                        System.out.println("Introdueix la data fi de la reserva: ");
                        reservaFi = comprobarDates();
                    }
                    
                    System.out.print("Quantes persones sereu?: ");
                    numPersones = reader.nextInt();
                    if (numPersones < 1) { //de manera obligatoria haura de haver-hi una persona
                        throw new Exception("No podeu ser menys d'1");
                    }
                } catch (Exception e) {
                    reader.nextLine();
                    System.out.println("Ha hagut un error: " + e.getMessage());
                    numPersones = -1;
                }
            }
            
            ArrayList<Reserva_activitat> activitats = new ArrayList<>(); //creem una llista de reserva_activitat per poder guardar-ho i assignar-lo a la reserva
            int numActivitats = -1;//<-- el manetenim a -1 per poder fer el control d'errors
            
            ArrayList<Estada> allotjaments = new ArrayList<>();  //creem una llista de estada per poder guardar-ho i assignar-lo a la reserva
            int numAllotjament = -1; //<-- el manetenim a -1 per poder fer el control d'errors
            
            if (activitatDAO.mostrarTotesActivitats().isEmpty()) { //si no hi ha activiats, no podem assignar activiats a la reserva
                System.out.println("--No hi ha activitats--");
            } else {
                while (numActivitats == -1) { //bucle que només pugui acceptar com reposta +0 el num d'activitats
                    try {
                        System.out.print("Quantes activitas vols fer? (no pots repetir la mateixa): ");
                        numActivitats = reader.nextInt();
                        if (numActivitats < 0) {
                            throw new Exception("Valor negatiu");
                        }
                        if (numActivitats > activitatDAO.mostrarTotesActivitats().size()) { //ens assegurem que el número d'activitats no sigui més gran que el número total
                            throw new Exception("Ho sentim, no hi ha suficient activitats per escollir de mes");
                        }
                    } catch (Exception e) {
                        reader.nextLine();
                        System.out.println("Valor incorecte: " + e.getMessage());
                        numActivitats = -1;
                    }
                }
            }
            
            if (allotjamentDAO.mostrarTotsAllotjament().isEmpty()) {
                System.out.println("--No hi ha allotjaments--");
            } else {
                while (numAllotjament == -1) {  //bucle que només pugui acceptar com reposta +0 el num d'allotjament
                    try {
                        System.out.print("Quants allotjaments vols? (no pots repetir la mateixa): ");
                        numAllotjament = reader.nextInt();
                        if (numAllotjament < 0) {
                            throw new Exception("Valor negatiu");
                        }
                        if (numAllotjament > allotjamentDAO.mostrarTotsAllotjament().size()) { //ens assegurem que el número d'allotjaments no sigui més gran que el número total
                            throw new Exception("Ho sentim, no hi ha suficient allotjaments per escollir de mes");
                        }
                    } catch (Exception e) {
                        reader.nextLine();
                        System.out.println("Valor incorecte: " + e.getMessage());
                        numAllotjament = -1;
                    }
                }
            }
            
            if (numActivitats == -1 && numAllotjament == -1) { //si esquipea el bucle, significa que no hi ha ni activitats ni allotjaments
                System.out.println("--Ho sentim, no hi ha ni activitats ni allotjaments--");
            } else {
                if (numActivitats > 0) {
                    for (int i = 0; i < numActivitats; i++) {
                        int pos = i + 1;
                        System.out.println("Activitat #" + pos + ": ");
                        Activitat activitat = buscarActiviat();
                        if (activitat == null) { //si l'activitat no exteix, retornarem a interntar-ho
                            System.out.println("Aquesta activitat no exiteix");
                            i--;
                        } else {
                            System.out.println("Introduiex una data inici: ");
                            LocalDate dtInAct = comprobarDates();
                            while (dtInAct.isBefore(reservaInici) || dtInAct.isAfter(reservaFi)) { //comporbem que les dates no sigui abans o despres de la dates de les reserves
                                System.out.println("Has introduit una data invalida, no pots introduir una data abans de la data inici de la reserva o depres de la data fi de la reserva");
                                System.out.println("Introduiex una data inici: ");
                                dtInAct = comprobarDates();
                            }
                            System.out.println("Introdueix una data fi: ");
                            LocalDate dtFiAct = comprobarDates();
                            
                            while (dtFiAct.isBefore(reservaInici) || dtFiAct.isAfter(reservaFi) || dtFiAct.isBefore(dtInAct)) { //comporbem que les dates no sigui abans o despres de la dates de les reserves ni abans de la data d'inici
                                System.out.println("Has introduit una data invalida, no pots introduir una data abans de la data inici de la reserva o depres de la data fi de la reserva ni abans de de la data d'inici de l'activitat");
                                System.out.println("Introdueix una data fi: ");
                                dtFiAct = comprobarDates();
                            }
                            Reserva_activitat resAct = new Reserva_activitat();
                            resAct.setActivitat(activitat);
                            resAct.setData_inici(dtInAct);
                            resAct.setData_fi(dtFiAct);
                            resAct.setPreuTotal(activitat.getPreu_per_persona() * numPersones); //fem el calcul del preu per num de persones que hem indicat a la reserva
                            activitats.add(resAct);
                            System.out.println("--Activitat afegida");
                        }
                    }
                }
                if (numAllotjament > 0) {
                    for (int i = 0; i < numAllotjament; i++) {
                        int pos = i + 1;
                        System.out.println("Allotjament #" + pos + ": ");
                        Allotjament allotjament = buscarAllotjament();
                        if (allotjament == null) { //primer comprobem que exiteixi el allotjament, si no, haura de reintentar-ho
                            System.out.println("Aquest allotjament no exiteix");
                            i--;
                        } else {
                            System.out.println("Introdueix la data d'inici");
                            LocalDate diaInci = comprobarDates();
                            while (diaInci.isAfter(reservaFi) || diaInci.isBefore(reservaInici)) {  //comporbem que les dates no sigui abans o despres de la dates de les reserves
                                System.out.println("No pots introduir una data abans de la data d'inici de la reserva o despres de la data fi de la reserva");
                                System.out.println("Introdueix la data d'inici");
                                diaInci = comprobarDates();
                            }
                            
                            System.out.println("Introdueix la data d'fi");
                            LocalDate diaFi = comprobarDates();
                            while (diaFi.isAfter(reservaFi) || diaFi.isBefore(reservaInici) || diaFi.isBefore(diaInci)) { //comporbem que les dates no sigui abans o despres de la dates de les reserves ni abans de la data d'inici
                                System.out.println("No pots introduir una data abans de la data d'inici de la reserva o despres de la data fi de la reserva ni abans de la data inici de la reserva");
                                System.out.println("Introdueix la data d'fi");
                                diaFi = comprobarDates();
                            }
                            
                            if (estadaDAO.mostrarTotsEstadaDates(diaInci, diaFi, allotjament).isEmpty()) { //comporbem que les dates no estiguin agafdes, si ho estan, haura de reintnatr introduir el allotjament
                                Estada estadaNova = new Estada();
                                estadaNova.setAllotjament(allotjament);
                                estadaNova.setData_inici(diaInci);
                                estadaNova.setData_fi(diaFi);
                                Duration duration = Duration.between(diaInci.atStartOfDay(), diaFi.atStartOfDay());
                                long dies = duration.toDays() + 1; //com que exclou la data fi, afegim un 1
                                estadaNova.setPreuTotal(dies * allotjament.getPreu_per_nit()); //fem el calcul del preu per num de dies que hem indicat anteriorment
                                allotjaments.add(estadaNova);
                                System.out.println("--Allotjament afegit");
                            } else {
                                System.out.println("Aquestes dates ja estan agafades, intenta un altre allotjament!");
                                i--;
                            }
                        }
                    }
                }
            }
            reservaDAO.crearReserva(client, reservaInici, reservaFi, numPersones, allotjaments, activitats);
        }
    }
    
    public Reserva buscarReservaPerID() {
        System.out.println("===Buscant reserva per ID reserva===");
        Reserva reserva = null;
        try {
            System.out.print("Introdueix el id de reserva: ");
            int id = reader.nextInt();
            if (id < 1) { //ens assegurem que el id no sigui menor que 0 ja que hibernate no crea ids 0
                throw new Exception("Rang ID massa petit");
            }
            
            reserva = reservaDAO.mostrarReserva(id);
        } catch (Exception e) {
            reader.nextLine();
            System.out.println("Ha hagut un error: " + e);
            reserva = null;
        }
        
        return reserva;
    }
    
    public void eliminarReserva() {
        System.out.println("===Eliminar reserva===");
        Reserva res = buscarReservaPerID();
        if (res != null) {
            reservaDAO.eliminar(res);
        } else {
            System.out.println("!Aquesta reserva no exiteix!");
        }
    }
    
    public void mostrarTotesReserves() {//aquesta, en comptes de mostrar totes les reseves, busca les reserves per client
        System.out.println("===Buscar totes les reserves per client===");
        Client cli = buscarClient();
        if (cli == null) { //primer ens assegurem que el client exiteix
            System.out.println("El client no exiteix");
        } else {
            System.out.println("Client trobat");
            ArrayList<Reserva> res = reservaDAO.mostrarTotesReserva(cli); //una vegada assegurada, cridem al dao de reserva
            if (res.isEmpty()) { //en cas de que no exiteixi, ens sortira un error
                System.out.println("Aquest client no te reserves");
            } else {
                for (Reserva re : res) {
                    System.out.println(re);
                }
                System.out.println("\nTotal - " + res.size() + " reserva/es"); //al final ens sortira quantes reserves ha realitzat un client
            }
        }
    }
    
    public void actualitzarReserva() {
        System.out.println("===Actualitzar reserva===");
        Reserva res = buscarReservaPerID();
        if (res == null) {
            System.out.println("Reserva no trobada");
        } else {
            LocalDate reservaInici = res.getData_inici();
            LocalDate reservaFi = res.getData_fi();
            List<Estada> estades = res.getEstades();
            List<Reserva_activitat> activitats = res.getReserva_activitat();
            
            if (estades.isEmpty()) { //COM QUE ESTEM EDITANT UNA RESEVA, ES POSSIBLE QUE JA TINGUI, LLAVORS NOMES FEM QUE INTENIR DE LA LLISTA DONADA I CANVIEM LES DADES, GRACIES AL CASCADE, NO HEM DE GESTIONAR RES MES
                System.out.println("--No tens allotjaments contractats, no podras actualitzar-los");
            } else {
                for (Estada estadaActual : estades) {
                    boolean valid = false;
                    System.out.println("Actualizant allotjament: " + estadaActual.getAllotjament().getNom());
                    while (!valid) {
                        System.out.println("Introdueix la data d'inici (" + estadaActual.getData_inici() + "): ");
                        LocalDate diaInci = comprobarDates();
                        while (diaInci.isAfter(reservaFi) || diaInci.isBefore(reservaInici)) {
                            System.out.println("No pots introduir una data abans de la data d'inici de la reserva o despres de la data fi de la reserva");
                            System.out.println("Introdueix la data d'inici");
                            diaInci = comprobarDates();
                        }
                        
                        System.out.println("Introdueix la data d'fi (" + estadaActual.getData_fi() + "): ");
                        LocalDate diaFi = comprobarDates();
                        while (diaFi.isAfter(reservaFi) || diaFi.isBefore(reservaInici) || diaFi.isBefore(diaInci)) {
                            System.out.println("No pots introduir una data abans de la data d'inici de la reserva o despres de la data fi de la reserva ni abans de la data inici de la reserva");
                            System.out.println("Introdueix la data d'fi");
                            diaFi = comprobarDates();
                        }
                        if (estadaDAO.mostrarTotsEstadaDatesSenseReserva(diaInci, diaFi, estadaActual.getAllotjament(), res).isEmpty()) {
                            estadaActual.setData_inici(diaInci);
                            estadaActual.setData_fi(diaFi);
                            res.setPreu_total(res.getPreu_total() - estadaActual.getPreuTotal());
                            Duration duration = Duration.between(diaInci.atStartOfDay(), diaFi.atStartOfDay());
                            long dies = duration.toDays() + 1;
                            double preuFinal = dies * estadaActual.getAllotjament().getPreu_per_nit();
                            estadaActual.setPreuTotal(preuFinal);
                            res.setPreu_total(res.getPreu_total() + preuFinal);
                            valid = true;
                        } else {
                            System.out.println("--Ho sentim, aquest dates ja estan agafades");
                            valid = false;
                        }
                    }
                }
            }
            
            if (activitats.isEmpty()) { //COM QUE ESTEM EDITANT UNA RESEVA, ES POSSIBLE QUE JA TINGUI, LLAVORS NOMES FEM QUE INTENIR DE LA LLISTA DONADA I CANVIEM LES DADES, GRACIES AL CASCADE, NO HEM DE GESTIONAR RES MES
                System.out.println("---No tens activitats contractades, no podras actualitzar-los");
            } else {
                for (Reserva_activitat activitat_reserva : activitats) {
                    System.out.println("Actualizant activitat: " + activitat_reserva.getActivitat().getNom());
                    System.out.println("Introduiex una data inici (" + activitat_reserva.getData_inici() + "): ");
                    LocalDate dtInAct = comprobarDates();
                    while (dtInAct.isBefore(reservaInici) || dtInAct.isAfter(reservaFi)) {
                        System.out.println("Has introduit una data invalida, no pots introduir una data abans de la data inici de la reserva o depres de la data fi de la reserva");
                        System.out.println("Introduiex una data inici: ");
                        dtInAct = comprobarDates();
                    }
                    System.out.println("Introdueix una data fi (" + activitat_reserva.getData_fi() + "): ");
                    LocalDate dtFiAct = comprobarDates();
                    
                    while (dtFiAct.isBefore(reservaInici) || dtFiAct.isAfter(reservaFi) || dtFiAct.isBefore(dtInAct)) {
                        System.out.println("Has introduit una data invalida, no pots introduir una data abans de la data inici de la reserva o depres de la data fi de la reserva ni abans de de la data d'inici de l'activitat");
                        System.out.println("Introdueix una data fi: ");
                        dtFiAct = comprobarDates();
                    }
                    activitat_reserva.setData_inici(dtInAct);
                    activitat_reserva.setData_fi(dtFiAct);
                    System.out.println("--Activitat actuaitzada");
                }
            }
            
            ArrayList<Allotjament> totsAllo = allotjamentDAO.mostrarTotsAllotjament();
            if (totsAllo.size() <= estades.size()) { //com que es possible que tingui resevat activitats, hem d'assegurarnos que hi hagues mes disponibles ja que no podem contactar el mateix
                System.out.println("---Ho sentim, però no hi ha més allotjaments disponibles");
            } else {
                int numAllotjament = -1;
                while (numAllotjament == -1) {
                    try {
                        System.out.print("Quants allotjaments vols? (no pots repetir la mateixa): ");
                        numAllotjament = reader.nextInt();
                        if (numAllotjament < 0) {
                            throw new Exception("Valor negatiu");
                        }
                        if (numAllotjament > totsAllo.size() - estades.size()) {
                            throw new Exception("Ho sentim, no hi ha suficient allotjaments per escollir de mes");
                        }
                    } catch (Exception e) {
                        reader.nextLine();
                        System.out.println("Valor incorecte: " + e.getMessage());
                        numAllotjament = -1;
                    }
                }
                
                for (int i = 0; i < numAllotjament; i++) {
                    int num = i + 1;
                    System.out.println("Allotjament nou #" + num);
                    
                    Allotjament allotjament = buscarAllotjament();
                    if (allotjament == null) {
                        System.out.println("Aquest allotjament no exiteix");
                        i--;
                    } else {
                        boolean invalid = false;
                        for (Estada estade : estades) {
                            if (estade.getAllotjament().equals(allotjament)) {
                                invalid = true;
                            }
                        }
                        
                        if (invalid) {
                            System.out.println("Aquest allotjament ja el tens contractat");
                            i--;
                        } else {
                            System.out.println("Introdueix la data d'inici");
                            LocalDate diaInci = comprobarDates();
                            while (diaInci.isAfter(reservaFi) || diaInci.isBefore(reservaInici)) {
                                System.out.println("No pots introduir una data abans de la data d'inici de la reserva o despres de la data fi de la reserva");
                                System.out.println("Introdueix la data d'inici");
                                diaInci = comprobarDates();
                            }
                            
                            System.out.println("Introdueix la data d'fi");
                            LocalDate diaFi = comprobarDates();
                            while (diaFi.isAfter(reservaFi) || diaFi.isBefore(reservaInici) || diaFi.isBefore(diaInci)) {
                                System.out.println("No pots introduir una data abans de la data d'inici de la reserva o despres de la data fi de la reserva ni abans de la data inici de la reserva");
                                System.out.println("Introdueix la data d'fi");
                                diaFi = comprobarDates();
                            }
                            
                            if (estadaDAO.mostrarTotsEstadaDates(diaInci, diaFi, allotjament).isEmpty()) {
                                Estada estadaNova = new Estada();
                                estadaNova.setReserva(res);
                                estadaNova.setAllotjament(allotjament);
                                estadaNova.setData_inici(diaInci);
                                estadaNova.setData_fi(diaFi);
                                Duration duration = Duration.between(diaInci.atStartOfDay(), diaFi.atStartOfDay());
                                long dies = duration.toDays() + 1;
                                double preuTotal = dies * allotjament.getPreu_per_nit();
                                estadaNova.setPreuTotal(preuTotal);
                                res.setPreu_total(res.getPreu_total() + preuTotal);
                                estades.add(estadaNova);
                                System.out.println("--Allotjament afegit");
                            } else {
                                System.out.println("Aquestes dates ja estan agafades, intenta un altre allotjament!");
                                i--;
                            }
                        }
                    }
                }
            }
            //<---
            ArrayList<Activitat> activitatsTotals = activitatDAO.mostrarTotesActivitats();
            if (activitatsTotals.size() <= activitats.size()) { //com que es possible que tingui resevat allotjaments, hem d'assegurarnos que hi hagues mes disponibles ja que no podem contactar el mateix
                System.out.println("---Ho sentim, no hi ha activitats suficents per afegir");
            } else {
                int numActivitats = -1;
                while (numActivitats == -1) {
                    try {
                        System.out.print("Quantes activitas vols fer? (no pots repetir la mateixa): ");
                        numActivitats = reader.nextInt();
                        if (numActivitats < 0) {
                            throw new Exception("Valor negatiu");
                        }
                        if (numActivitats > activitatDAO.mostrarTotesActivitats().size()) {
                            throw new Exception("Ho sentim, no hi ha suficient activitats per escollir de mes");
                        }
                    } catch (Exception e) {
                        reader.nextLine();
                        System.out.println("Valor incorecte: " + e.getMessage());
                        numActivitats = -1;
                    }
                }
                
                for (int i = 0; i < numActivitats; i++) {
                    int pos = i + 1;
                    System.out.println("Activitat #" + pos + ": ");
                    Activitat activitat = buscarActiviat();
                    if (activitat == null) {
                        System.out.println("Aquesta activitat no exiteix");
                        i--;
                    } else {
                        System.out.println("Introduiex una data inici: ");
                        LocalDate dtInAct = comprobarDates();
                        while (dtInAct.isBefore(reservaInici) || dtInAct.isAfter(reservaFi)) {
                            System.out.println("Has introduit una data invalida, no pots introduir una data abans de la data inici de la reserva o depres de la data fi de la reserva");
                            System.out.println("Introduiex una data inici: ");
                            dtInAct = comprobarDates();
                        }
                        System.out.println("Introdueix una data fi: ");
                        LocalDate dtFiAct = comprobarDates();
                        
                        while (dtFiAct.isBefore(reservaInici) || dtFiAct.isAfter(reservaFi) || dtFiAct.isBefore(dtInAct)) {
                            System.out.println("Has introduit una data invalida, no pots introduir una data abans de la data inici de la reserva o depres de la data fi de la reserva ni abans de de la data d'inici de l'activitat");
                            System.out.println("Introdueix una data fi: ");
                            dtFiAct = comprobarDates();
                        }
                        Reserva_activitat resAct = new Reserva_activitat();
                        resAct.setActivitat(activitat);
                        resAct.setData_inici(dtInAct);
                        resAct.setData_fi(dtFiAct);
                        resAct.setReserva(res);
                        double preuFinal = activitat.getPreu_per_persona() * res.getPersones_total();
                        resAct.setPreuTotal(preuFinal);
                        res.setPreu_total(res.getPreu_total() + preuFinal);
                        activitats.add(resAct);
                        System.out.println("--Activitat afegida");
                    }
                }
            }
            
            res.setEstades(estades);
            res.setReserva_activitat(activitats);
            reservaDAO.actualitzar(res);
        }
    }
    
    public LocalDate comprobarDates() {
        boolean valid = false;
        LocalDate data = null;
        while (!valid) {
            try {
                System.out.print("Introdueix la data (dd-MM-yyyy): ");
                String dataTxt = reader.next();
                DateTimeFormatter tipusData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                data = LocalDate.parse(dataTxt, tipusData);
                valid = true;
            } catch (Exception e) {
                valid = false;
                reader.nextLine();
                System.out.println("Has introduit un tipus incorrecte (si el dia/mes es 1 hauràs d'escriure 01): " + e);
            }
        }
        return data;
    }
}
