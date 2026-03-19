/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.agencia_viatge;

import com.mycompany.agencia_viatge.Model.*;
import com.mycompany.agencia_viatge.DAO.*;
import java.sql.Connection;
import java.util.Scanner;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author alumne
 */
public class Agencia_viatge {

    public static void main(String[] args) {

        try { //Com que la sessio pot donar errors, el posem amb un trycatch per evitar que la consola mostri el error i només es tanqui
            //Primer creem els gestionador de sessions perque nomes se excuti una vegada i tots comparten la presistencia
            System.out.println("---Generant base de dades---");
            var configuration = new Configuration().addAnnotatedClass(Client.class).addAnnotatedClass(Reserva.class).addAnnotatedClass(Activitat.class).addAnnotatedClass(Allotjament.class).addAnnotatedClass(Estada.class).addAnnotatedClass(Reserva_activitat.class);
            //amb la configuracio, si el tenim activat en les propeiats, regenenara les taules
            var sessionFactory = configuration.buildSessionFactory(); //creeem el generador de sessions
            System.out.println("---Iniciant sessio---");

            Scanner reader = new Scanner(System.in); //<-- creem un scanner que el tindra com parametre el model,
            //d'aquesta manera la podem manternir oberta durant la sessio i tancarla quan acabi, evitant errors
            ModelControllador model = new ModelControllador(sessionFactory, reader); //<-- El model controllador sera el nostre model que controla els daos per mantenir una relacio semblant entre ells
            Agencia_viatge projecte = new Agencia_viatge();//Nomes per poder cridar els menus

            int opcioMenu = -1;
            int opcioGestio = -1;
            System.out.println("======================\n\tBenvinguts!\n======================");
            if (sessionFactory != null) { //Primer comporbem que la conexio no sigui nul·la
                while (opcioMenu != 5) { //Despres pasem al menu
                    opcioMenu = projecte.menu(reader); //a continuacio, ens sortira un menu on ens donara la opcio de escollir diferents opcions
                    switch (opcioMenu) {
                        case 1:
                            System.out.println("====Gestionar Clients====");
                            opcioGestio = projecte.menuGestio(reader, "client"); //a continuacio, ens sortira un menu "secundari" on ens donara la opcio de escollir diferents opcions respecte a la opcio escollida
                            switch (opcioGestio) {
                                case 1:
                                    model.crearClient(); //en aquest casos, cridara al model i realitzarà la opcio que hem escollir
                                    break;
                                case 2:
                                    Client cli = model.buscarClient(); //aqui cridara al metode del model que ens retornara el client
                                    //a continuació, depenent de si s'ha tobat o no, sortirà el toString o un missatge de no trobat
                                    if (cli == null) {
                                        System.out.println("Client no trobat");
                                    } else {
                                        System.out.println(cli);
                                    }
                                    break;
                                case 3:
                                    model.mostrarClient();
                                    break;
                                case 4:
                                    model.eliminarClient();
                                    break;
                                case 5:
                                    model.updateClient();
                                    break;
                                case 6:
                                    System.out.println("==Intenat sortir del menu secundari==");
                            }
                            opcioGestio = -1;
                            System.out.println("==Sortint==");
                            break;
                        case 2:
                            System.out.println("====Gestionar Reserves====");
                            opcioGestio = projecte.menuGestio(reader, "reserves");
                            switch (opcioGestio) {
                                case 1:
                                    model.crearReserva();
                                    break;
                                case 2:
                                    Reserva res = model.buscarReservaPerID();//aqui cridara al metode del model que ens retornara el reserva
                                    //a continuació, depenent de si s'ha tobat o no, sortirà el toString o un missatge de no trobat
                                    if (res == null) {
                                        System.out.println("Resrva no trobada");
                                    } else {
                                        System.out.println(res);
                                    }
                                    break;
                                case 3:
                                    model.mostrarTotesReserves();
                                    break;
                                case 4:
                                    model.eliminarReserva();
                                    break;
                                case 5:
                                    model.actualitzarReserva();
                                    break;
                                case 6:
                                    System.out.println("==Intenat sortir del menu secundari==");
                            }
                            opcioGestio = -1;
                            break;
                        case 3:
                            System.out.println("====Gestionar Activitats====");
                            opcioGestio = projecte.menuGestio(reader, "activitats");
                            switch (opcioGestio) {
                                case 1:
                                    model.crearActivitat();
                                    break;
                                case 2:
                                    Activitat act = model.buscarActiviat();//aqui cridara al metode del model que ens retornara el activitat
                                    //a continuació, depenent de si s'ha tobat o no, sortirà el toString o un missatge de no trobat
                                    if (act == null) {
                                        System.out.println("Activitat no trobada");
                                    } else {
                                        System.out.println(act);
                                    }

                                    break;
                                case 3:
                                    model.mostrarTotesActivitat();
                                    break;
                                case 4:
                                    model.eliminarActivitat();
                                    break;
                                case 5:
                                    model.modificarActivitats();
                                    break;
                                case 6:
                                    System.out.println("==Intenat sortir del menu secundari==");
                            }
                            opcioGestio = -1;
                            break;
                        case 4:
                            System.out.println("====Gestionar Allotjament====");
                            opcioGestio = projecte.menuGestio(reader, "allotjament");
                            switch (opcioGestio) {
                                case 1:
                                    model.crearAllotjament();
                                    break;
                                case 2:
                                    Allotjament allo = model.buscarAllotjament();//aqui cridara al metode del model que ens retornara el allotjament
                                    //a continuació, depenent de si s'ha tobat o no, sortirà el toString o un missatge de no trobat
                                    if (allo == null) {
                                        System.out.println("Allotjament no trobar");
                                    } else {
                                        System.out.println(allo);
                                    }
                                    break;
                                case 3:
                                    model.mostrarTotsAllotjaments();
                                    break;
                                case 4:
                                    model.eliminarAllotjament();
                                    break;
                                case 5:
                                    model.modificarAllotjament();
                                    break;
                                case 6:
                                    System.out.println("==Intenat sortir del menu secundari==");
                            }
                            opcioGestio = -1;
                            break;
                        default:
                            System.out.println("\n==S'he esta intentat sortir=="); //Primer intentem tancar les clases i conexions
                            System.out.println("Tancat lector..."); //Tancem el reader
                            reader.close();
                            System.out.println("S'ha tancat el lector");

                            System.out.println("Tancant sessio..."); //Tancem la sessio i el generador de sessions
                            sessionFactory.close();
                            System.out.println("S'ha tancat la sessio");
                    }
                }

            } else {
                System.out.println("\n===Ha hagut un error de conexio===");
            }
        } catch (HibernateException e) {
            System.out.println("Ha hagut un error : " + e.getMessage());
        }

        System.out.println("\n===S'ha sortit===");
    }

    public int menu(Scanner reader) {
        int opcioMenu = -1;
        while (opcioMenu == -1) {
            try {
                System.out.println("\n======Menu======");
                System.out.println("1- Gestionar Clients");
                System.out.println("2- Gestionar Reserves");
                System.out.println("3- Gestionar Activitats");
                System.out.println("4- Gestionar Allotjament");
                System.out.println("5- Sortir");
                System.out.print("Introdueix el teu valor: ");
                opcioMenu = reader.nextInt();
                if (opcioMenu < 1 || opcioMenu > 5) { //comporben que no sorti del rang, si surt, donarem una excepcio personalitzada i haura de reintentar-ho
                    throw new Exception("Fora de rang");
                }
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("Has introduit un valor incorrecte: " + e);
                opcioMenu = -1;
            }
        }
        return opcioMenu;
    }

    public int menuGestio(Scanner reader, String nom) {
        int opcioMenu = -1;
        while (opcioMenu == -1) {
            try {
                System.out.printf("\n======Gestio de %s======%n", nom);
                System.out.println("1- Crear " + nom);
                System.out.println("2- Buscar " + nom);
                System.out.println("3- Buscar tots " + nom);
                System.out.println("4- Eliminar " + nom);
                System.out.println("5- Actualitzar " + nom);
                System.out.println("6- Sortir");
                System.out.print("Introdueix el teu valor: ");
                opcioMenu = reader.nextInt();
                if (opcioMenu < 1 || opcioMenu > 6) { //comporben que no sorti del rang, si surt, donarem una excepcio personalitzada i haura de reintentar-ho
                    throw new Exception("Fora de rang");
                }
            } catch (Exception e) {
                reader.nextLine();
                System.out.println("Has introduit un valor incorrecte: " + e);
                opcioMenu = -1;
            }
        }
        return opcioMenu;
    }
}
