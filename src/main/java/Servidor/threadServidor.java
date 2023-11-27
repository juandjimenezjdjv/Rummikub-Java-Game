/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Rummy.Mazo;
import Rummy.Partida;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author juand
 */
public class threadServidor extends Thread {
     Socket cliente = null;//referencia a socket de comunicacion de cliente
     DataInputStream entrada=null;//Para leer comunicacion
     DataOutputStream salida=null;//Para enviar comunicacion	
     String nameUser; //Para el nombre del usuario de esta conexion
     ServidorRommy servidor;// referencia al servidor
     String partidaAlaqueestaUnido = "";//Agregar el nomber de la partida a la que se unio
     
    
     
     // identificar el numero de jugador
     int numeroDeJugador;
     
     public threadServidor(Socket cliente,ServidorRommy serv, int num)
     {
        this.cliente = cliente;
        this.servidor = serv;
        this.numeroDeJugador = num;
        nameUser="";// inicialmente se desconoce, hasta el primer read del hilo
     }
     
     //Getter an Setter...
     public String getNameUser()
     {
       return nameUser;
     }
     public void setNameUser(String name)
     {
       nameUser=name;
     }

    public String getPartidaAlaqueestaUnido() {
        return partidaAlaqueestaUnido;
    }

    public void setPartidaAlaqueestaUnido(String partidaAlaqueestaUnido) {
        this.partidaAlaqueestaUnido = partidaAlaqueestaUnido;
    }
     
    public void run()
        {
    	try
            {
            // inicializa para lectura y escritura con stream de cliente
            entrada=new DataInputStream(cliente.getInputStream());//comunic
            salida=new DataOutputStream(cliente.getOutputStream());//comunic
            // Es el primer read que hace, para el nombre del 

            String EntradaNombre = entrada.readUTF();
            if (!servidor.ValidaNombres.contains(EntradaNombre)) {
                this.setNameUser(EntradaNombre);
                servidor.ValidaNombres.add(EntradaNombre);
                System.out.println("Leyo nombre: " + nameUser);
            }else{
                servidor.ventana.mostrar("Se desconecto un usuario: "+nameUser+" Nombre ya esta en uso");
                this.cliente.close();
            }
          
    	}
    	catch (IOException e) {  e.printStackTrace();     }
    	//VARIABLES
        int opcion=0;
        String nCliente = "";
        int numUsers=0;
        String amigo="";
        String mencli="";
        int cantidadCarta = 0;
        int ponerCartas = 0;
        String pual = "";
        Partida tmp = null;
        String dts = "";
        ArrayList<Mazo.Carta> nuevasCartas;
                
    	while(true)
    	{
          try
            {   
              //Siempre espera leer un int que será la instruccion por hacer
             opcion=entrada.readInt();
             switch(opcion)
            {
                
                case 1:
                    String nombreLista = entrada.readUTF();
                    servidor.agregarnuevapartida(nombreLista);
                    System.out.println("Recibio el nombre de la persona: " + nombreLista);
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (servidor.Jugadores.get(i).getPartidaAlaqueestaUnido().equals("")){
                            servidor.Jugadores.get(i).salida.writeInt(1);
                            servidor.Jugadores.get(i).salida.writeUTF(nombreLista);
                        }
                    }
                    break;
                case 2:
                    String partidacrear = entrada.readUTF();//esta lista toma las personas y las mete en una partida
                    String nombrepartidacrear = entrada.readUTF();//esta toma el nombre de la partida nueva
                    String[] partes = partidacrear.split(",");
                    List<String> listaPartes = Arrays.asList(partes);
                    String datos = "";
                    Mazo mazo = new Mazo();
                    Partida temp;
                    temp = new Partida(mazo);
                    servidor.setParti(temp);
                    temp.setNombrePartida(nombrepartidacrear);
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (listaPartes.contains(servidor.Jugadores.get(i).getNameUser())){
                            servidor.Jugadores.get(i).setPartidaAlaqueestaUnido(nombrepartidacrear);
                            //Aqui hay que poner la funcionalidad de agregar a cada persona su mazo propio
                            temp.setCartasdeJugadores((servidor.Jugadores.get(i).recibirCartas(mazo.getBaraja())),ponerCartas);

                            temp.agregarNombres(servidor.Jugadores.get(i).getNameUser());
                            cantidadCarta = temp.getCartasdeJugadores(ponerCartas).size();
                            ponerCartas++;
                        }else{
                            servidor.Jugadores.get(i).salida.writeInt(2);
                            servidor.Jugadores.get(i).salida.writeInt(2);
                        }
                    }
                    ponerCartas=0;
                    for (int j = 0; j < temp.getNombres().size(); j++){
                        datos = datos+"J"+j+": "+temp.getNombres().get(j)+" #:";
                        datos = datos+temp.getCartasdeJugadores(ponerCartas).size()+"    ";
                        ponerCartas++;
                    }
                    ponerCartas=0;
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (listaPartes.contains(servidor.Jugadores.get(i).getNameUser())){
                            servidor.Jugadores.get(i).salida.writeInt(2);
                            servidor.Jugadores.get(i).salida.writeInt(1);
                            servidor.Jugadores.get(i).salida.writeInt(cantidadCarta);
                            for (int j = 0; j < cantidadCarta; j++){                                    
                                servidor.Jugadores.get(i).salida.writeUTF(temp.getCartasdeJugadores(ponerCartas).get(j).getColor());
                                servidor.Jugadores.get(i).salida.writeInt(temp.getCartasdeJugadores(ponerCartas).get(j).getNumero());
                                servidor.Jugadores.get(i).salida.writeInt(temp.getCartasdeJugadores(ponerCartas).get(j).getId());
                                
                            }
                            servidor.Jugadores.get(i).salida.writeUTF("Turno de: " +temp.getNombres().get(0)+"        Cartas restantes en el mazo: "+temp.getCartas().getBaraja().size());
                            servidor.Jugadores.get(i).salida.writeUTF(datos);
                            ponerCartas++;
                        }
                    }ponerCartas=0;
                    servidor.Partidas.add(temp);
                    break;
                    
                case 3:
                    nCliente = entrada.readUTF();
                    String mensaje = entrada.readUTF();
                    String pActual = "";
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (servidor.Jugadores.get(i).getNameUser().equals(nCliente))
                            pActual = servidor.Jugadores.get(i).getPartidaAlaqueestaUnido();
                    }
                    
                    //Ahora solo faltaria revisar los que tengan esa partida actual y enviarle el mensaje solo a esos
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (servidor.Jugadores.get(i).getPartidaAlaqueestaUnido().equals(pActual)){
                            servidor.Jugadores.get(i).salida.writeInt(3);
//                            servidor.Jugadores.get(i).salida.writeUTF(nCliente);
                            servidor.Jugadores.get(i).salida.writeUTF(mensaje);
                        }
                    }
                    break;
                
                case 4: //Caso de boton de salida del juego
                    String nCli = entrada.readUTF();
                    String ptual = "";
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (servidor.Jugadores.get(i).getNameUser().equals(nCli))
                            ptual = servidor.Jugadores.get(i).getPartidaAlaqueestaUnido();
                    }
                    for (int i = 0; i < servidor.Partidas.size(); i++){
                        if (servidor.Partidas.get(i).getNombrePartida().equals(ptual))
                            tmp = servidor.Partidas.get(i);
                    }
                    String nombresPartida = "";
                    
                    String guardar = "";
                    System.out.println("Cuantos nombres hay: "+ tmp.getNombres().size());
                    for (int i =0; i < tmp.getNombres().size(); i++){
                            nombresPartida += tmp.getNombres().get(i)+" ";
                    }
                    guardar= tmp.getNombrePartida()+" "+tmp.getEstado()+" "+nombresPartida;
                    servidor.guardarPartida(guardar, tmp);
                    
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (servidor.Jugadores.get(i).getNameUser().equals(nCli))
                            servidor.Jugadores.get(i).salida.writeInt(4);
                    }
                    break;
                    
                case 5:
                    String client = entrada.readUTF();
                    int siguiente = entrada.readInt();
                    
                    for (int i = 0; i < servidor.Jugadores.size(); i++){
                        if (servidor.Jugadores.get(i).getNameUser().equals(client))
                            pual = servidor.Jugadores.get(i).getPartidaAlaqueestaUnido();
                    }
                    for (int i = 0; i < servidor.Partidas.size(); i++){
                        if (servidor.Partidas.get(i).getNombrePartida().equals(pual))
                            tmp = servidor.Partidas.get(i);
                    }
                    
                    if (client.equals(tmp.getNombres().get(tmp.getTurno()))){
                        nuevasCartas = tmp.getCartasdeJugadores(tmp.getTurno());                        
                        if (siguiente == 1)
                        {//robo carta siguiente jugador
                            ponerCartas=0;
                            dts = "";
                            for (int j = 0; j < tmp.getNombres().size(); j++){
                                dts = dts+"J"+j+": "+tmp.getNombres().get(j)+" #:";
                                dts = dts+tmp.getCartasdeJugadores(ponerCartas).size()+"    ";
                                ponerCartas++;
                            }
                            ponerCartas=0;
                            System.out.println("Quiero ver los turnos de cada uno: "+tmp.getTurno());
                            if (tmp.getTurno()>=(tmp.getNombres().size()-1)){
                                tmp.setTurno(0);
                                for (int i = 0; i < servidor.Jugadores.size(); i++){
                                    if (tmp.getNombres().contains(servidor.Jugadores.get(i).getNameUser())){
                                        servidor.Jugadores.get(i).salida.writeInt(5);
                                        servidor.Jugadores.get(i).salida.writeInt(0);//Es para que se haga un condi en caso 5
                                        servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(0)+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
                                        servidor.Jugadores.get(i).salida.writeUTF(dts);
                                    }
                                }
                            }else {
                                tmp.setTurno(tmp.getTurno()+1);
                                for (int i = 0; i < servidor.Jugadores.size(); i++){
                                    if (tmp.getNombres().contains(servidor.Jugadores.get(i).getNameUser())){
                                        servidor.Jugadores.get(i).salida.writeInt(5);
                                        servidor.Jugadores.get(i).salida.writeInt(0);//Es para que se haga un condi en caso 5
                                        servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(tmp.getTurno())+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
                                        servidor.Jugadores.get(i).salida.writeUTF(dts);
                                    }
                                }
                            }
                            
                        }
                        else if (siguiente == 2){//pasa las posiciones de las cartas para ponerlas en tablero
                            int a = entrada.readInt();//cantidad de variables que vienen en la lista
                            ArrayList <Integer> posilista = new ArrayList<>();
                            int p= 0;//cantidad de variables que vienen en la lista
                            for (int i = 0; i < a; i++){
                                p = entrada.readInt();
                                posilista.add(p);
                                System.out.println("Posiciones: "+ p);
                            }
                            Collections.sort(posilista,Comparator.reverseOrder());
                            ponerCartas=0;
                            //TODO pasa de nuevo las cartas al jugador para que se actualicen
                            //TODO actualiza las cantidades de cartas y el turno en los labels
                            dts = "";
                            for (int j = 0; j < tmp.getNombres().size(); j++){
                                dts = dts+"J"+j+": "+tmp.getNombres().get(j)+" #:";
                                dts = dts+tmp.getCartasdeJugadores(ponerCartas).size()+"    ";
                                ponerCartas++;
                            }
                            ponerCartas=0;
                            
//                            for (int i = 0; i < posilista.size(); i++){
//                                
//                            }
                            ArrayList<Mazo.Carta> nueCartas = new ArrayList<>();
                            ArrayList<Mazo.Carta> borrarCartas = new ArrayList<>();
                            System.out.println("Quiero ver los turnos de cada uno: "+tmp.getTurno());
                            nuevasCartas = tmp.getCartasdeJugadores(tmp.getTurno());
                            if (client.equals(tmp.getNombres().get(tmp.getTurno()))){
                                for (int i = 0; i < nuevasCartas.size(); i++){
                                    System.out.println("Cartas: " + nuevasCartas.get(i).getNumero());
                                }
                                //Aqui poner una salida de las cartas que se van a la mierda
                                System.out.println("-------------------------------------------");
                                for (int i = 0; i < nuevasCartas.size(); i++) {
                                    if (!posilista.contains(i)){
                                        nueCartas.add(nuevasCartas.get(i));
                                    }else{
                                        borrarCartas.add(nuevasCartas.get(i));//---------------Estas son las cartas que hay que enviar al tablero
                                    }
                                    System.out.println("CaRTAs: " + nuevasCartas.get(i).getNumero());
                                }
                                
//                                nuevasCartas;
                                System.out.println("-------------------------------------------");
                                for (int i = 0; i < nueCartas.size(); i++){
                                    System.out.println("Cartas: " + nueCartas.get(i).getNumero());
                                }
                                tmp.setCartasdeJugadores(nueCartas, tmp.getTurno());
                                ponerCartas=0;
    //                            servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(tmp.getTurno()).size());
                                for (int i = 0; i < servidor.Jugadores.size(); i++){
                                    if (client.equals(servidor.Jugadores.get(i).getNameUser())){
                                        servidor.Jugadores.get(i).salida.writeInt(6);
                                        servidor.Jugadores.get(i).salida.writeInt(1);
                                        servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(tmp.getTurno()).size());
                                        System.out.println("Nombre juagdor verificarcion: "+client);
                                        for (int j = 0; j < tmp.getCartasdeJugadores(tmp.getTurno()).size(); j++){
                                            servidor.Jugadores.get(i).salida.writeUTF(tmp.getCartasdeJugadores(ponerCartas).get(j).getColor());
                                            servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(ponerCartas).get(j).getNumero());
                                            servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(ponerCartas).get(j).getId());

                                        }
                                    }else{
    //                                    servidor.Jugadores.get(i).salida.writeInt(2);
                                    }ponerCartas++;
                                }ponerCartas=0;
                            }
                            
                            if (tmp.getTurno()>=(tmp.getNombres().size()-1)){
                                tmp.setTurno(0);
                                for (int i = 0; i < servidor.Jugadores.size(); i++){
                                    if (tmp.getNombres().contains(servidor.Jugadores.get(i).getNameUser())){
                                        servidor.Jugadores.get(i).salida.writeInt(5);
                                        servidor.Jugadores.get(i).salida.writeInt(0);//Es para que se haga un condi en caso 5
                                        servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(0)+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
                                        servidor.Jugadores.get(i).salida.writeUTF(dts);
                                    }
                                }
                            }else {
                                tmp.setTurno(tmp.getTurno()+1);
                                for (int i = 0; i < servidor.Jugadores.size(); i++){
                                    if (tmp.getNombres().contains(servidor.Jugadores.get(i).getNameUser())){
                                        servidor.Jugadores.get(i).salida.writeInt(5);
                                        servidor.Jugadores.get(i).salida.writeInt(0);//Es para que se haga un condi en caso 5
                                        servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(tmp.getTurno())+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
                                        servidor.Jugadores.get(i).salida.writeUTF(dts);
                                    }
                                }
                            }
                            //--------------------------------------------------------------------------------------------------------
                            for (int i = 0; i < servidor.Jugadores.size(); i++){
                                if (servidor.Jugadores.get(i).getPartidaAlaqueestaUnido().equals(pual)){
                                    servidor.Jugadores.get(i).salida.writeInt(6);
                                    servidor.Jugadores.get(i).salida.writeInt(0);
                                    servidor.Jugadores.get(i).salida.writeInt(tmp.getPintarfilar());
                                    servidor.Jugadores.get(i).salida.writeInt(borrarCartas.size());//cantidad de cartas para el loop
                                    for (int j = 0; j < borrarCartas.size(); j++){
//                                        System.out.println("Color: "+borrarCartas.get(j).getColor());
//                                        System.out.println("Numero: "+borrarCartas.get(j).getColor());
                                        servidor.Jugadores.get(i).salida.writeUTF(borrarCartas.get(j).getColor());
                                        servidor.Jugadores.get(i).salida.writeInt(borrarCartas.get(j).getNumero());
                                    }
                                }
                            }tmp.setPintarfilar((tmp.getPintarfilar()+1));
                            
                            
                            
                        }
                    }else{
                        
                    }
                    
                    //TODO Aqui hacer el for de salida para que los datos ya esten hechos al final y se pasen actualizados
                    
                    break;
                    
                    case 6:
                        nCliente = entrada.readUTF();
                        for (int i = 0; i < servidor.Jugadores.size(); i++){
                            if (servidor.Jugadores.get(i).getNameUser().equals(nCliente))
                                pual = servidor.Jugadores.get(i).getPartidaAlaqueestaUnido();
                        }
                        for (int i = 0; i < servidor.Partidas.size(); i++){
                            if (servidor.Partidas.get(i).getNombrePartida().equals(pual))
                                tmp = servidor.Partidas.get(i);
                        }
                        
                        nuevasCartas = tmp.getCartasdeJugadores(tmp.getTurno());
                        if (nCliente.equals(tmp.getNombres().get(tmp.getTurno()))){
                            Mazo maztmp = tmp.getCartas();
                            nuevasCartas.add(pedirCarta(maztmp.getBaraja()));
                            System.out.println("Ultima Agregada: "+ nuevasCartas.getLast().getNumero());
                            ponerCartas=0;
//                            servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(tmp.getTurno()).size());
                            for (int i = 0; i < servidor.Jugadores.size(); i++){
                                if (nCliente.equals(servidor.Jugadores.get(i).getNameUser())){
                                    servidor.Jugadores.get(i).salida.writeInt(6);
                                    servidor.Jugadores.get(i).salida.writeInt(1);
                                    servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(tmp.getTurno()).size());
                                    System.out.println("Nombre juagdor verificarcion: "+nCliente);
                                    for (int j = 0; j < tmp.getCartasdeJugadores(tmp.getTurno()).size(); j++){
                                        servidor.Jugadores.get(i).salida.writeUTF(tmp.getCartasdeJugadores(ponerCartas).get(j).getColor());
                                        servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(ponerCartas).get(j).getNumero());
                                        servidor.Jugadores.get(i).salida.writeInt(tmp.getCartasdeJugadores(ponerCartas).get(j).getId());
                                        
                                    }
                                }else{
//                                    servidor.Jugadores.get(i).salida.writeInt(2);
                                }ponerCartas++;
                                System.out.println("Ultima Agregada: "+ nuevasCartas.getLast().getNumero());
                            }ponerCartas=0;
                        }//salidas: cantidad cartas actualizadas, datos actualizados, cartas
                        
                        break;
                    

                   
            }
          }
          catch (IOException e) {
                
              System.out.println("El cliente termino la conexion");
              break;
          }
    	}   
    	servidor.ventana.mostrar("Se removio un usuario");
    	
    	try
    	{   
            for (int i = 0; i < servidor.ValidaNombres.size(); i++){
                if (nameUser.equals(servidor.ValidaNombres.get(i))) {
                    servidor.ValidaNombres.remove(i);
                    break;
                }
            }
            servidor.ventana.mostrar("Se desconecto un usuario: "+nameUser);
            cliente.close();
    	}	
        catch(Exception et)
        {servidor.ventana.mostrar("no se puede cerrar el socket");}   
     }
    
    public ArrayList<Mazo.Carta> recibirCartas(ArrayList<Mazo.Carta> cartas) {
        ArrayList<Mazo.Carta> cartasRecibidas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 13; i++) {
            int indiceAleatorio = random.nextInt(cartas.size());
            Mazo.Carta carta = cartas.remove(indiceAleatorio);
            cartasRecibidas.add(carta);
        }
        return cartasRecibidas;
    }
    
    public Mazo.Carta pedirCarta(ArrayList<Mazo.Carta> cartas) {
        Mazo.Carta cartasRecibidas = null;
        Random random = new Random();
        
        int indiceAleatorio = random.nextInt(cartas.size());
        cartasRecibidas = cartas.remove(indiceAleatorio);
//        cartasRecibidas.add(carta);
        
        return cartasRecibidas;
    }
}



//                            for (int i = 0; i < servidor.Jugadores.size(); i++){
//                                if (client.equals(servidor.Jugadores.get(i).getNameUser())){
//                                    servidor.Jugadores.get(i).salida.writeInt(5);
//                                    servidor.Jugadores.get(i).salida.writeInt(1);//Es para que se haga un condi en caso 5
//                                    servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(0)+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
//                                    servidor.Jugadores.get(i).salida.writeUTF(dts);
//                                }
//                            }
//                            
//                            if (tmp.getTurno()>=(tmp.getNombres().size()-1)){
//                                tmp.setTurno(0);
//                                for (int i = 0; i < servidor.Jugadores.size(); i++){
//                                    if (client.equals(servidor.Jugadores.get(i).getNameUser())){
//                                        servidor.Jugadores.get(i).salida.writeInt(5);
//                                        servidor.Jugadores.get(i).salida.writeInt(1);//Es para que se haga un condi en caso 5
//                                        servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(0)+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
//                                        servidor.Jugadores.get(i).salida.writeUTF(dts);
//                                    }
//                                }
//                                
//                            }else {
//                                tmp.setTurno(tmp.getTurno()+1);
//                                for (int i = 0; i < servidor.Jugadores.size(); i++){
//                                    if (client.equals(servidor.Jugadores.get(i).getNameUser())){
//                                        servidor.Jugadores.get(i).salida.writeInt(5);
//                                        servidor.Jugadores.get(i).salida.writeInt(1);//Es para que se haga un condi en caso 5
//                                        servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(0)+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
//                                        servidor.Jugadores.get(i).salida.writeUTF(dts);
//                                    }
//                                }
//                                
//                            }
//                            for (int i = 0; i < servidor.Jugadores.size(); i++){
//                                if (tmp.getNombres().contains(servidor.Jugadores.get(i).getNameUser())){
//                                    servidor.Jugadores.get(i).salida.writeInt(5);
//                                    servidor.Jugadores.get(i).salida.writeInt(0);//Es para que se haga un condi en caso 5
//                                    servidor.Jugadores.get(i).salida.writeUTF("Turno de: " + tmp.getNombres().get(0)+"        Cartas restantes en el mazo: "+tmp.getCartas().getBaraja().size());
//                                    servidor.Jugadores.get(i).salida.writeUTF(dts);
//                                }
//                            }