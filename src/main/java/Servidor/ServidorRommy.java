/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Rummy.Partida;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author juand
 */
public class ServidorRommy {
    JFrameServidor ventana;
    ArrayList<threadServidor> Jugadores = new ArrayList<>();
    ArrayList<String> ValidaNombres = new ArrayList<>();
    ArrayList<threadServidor> JCrearPartida = new ArrayList<>();
    ArrayList<Partida> Partidas = new ArrayList<>();
    
    public ServidorRommy(JFrameServidor padre) {
        this.ventana = padre;        
    }
    
    public void runServer() {
        try {
            ServerSocket serv = new ServerSocket(8081);
            while (true) {
                ventana.mostrar(".::Servidor Activo");
                ventana.mostrar(".::Esperando jugadores");

                Socket cliente = serv.accept();
//                ventana.mostrar(".::Cliente: "+ Jugadores.size() + " Aceptado");
                threadServidor user = new threadServidor(cliente, this, Jugadores.size());
//                ventana.mostrar(".::Cliente: "+ Jugadores.size() + " Aceptado");
                System.out.println("Cliente: " + cliente);
                ventana.mostrar(".::Cliente: "+ user.numeroDeJugador + " Aceptado");
                user.start();

                Jugadores.add(user);
                
                
            }
            
        } catch (IOException ex) {
            ventana.mostrar("ERROR ... en el servidor");
        }
    }

    public void agregarnuevapartida(String obj){
        for (int i = 0; i < Jugadores.size(); i++){
            if (Jugadores.get(i).getNameUser().equals(obj)){
                JCrearPartida.add(Jugadores.get(i));
            }
        }
        
    }
    public void nuevapartida(){//Este se usa cuando se crea una partida
        JCrearPartida.removeAll(Jugadores);
    }

    public ArrayList<Partida> getPartidas() {
        return Partidas;
    }

    public void setPartidas(ArrayList<Partida> Partidas) {
        this.Partidas = Partidas;
    }

    
    public void setParti(Partida partidas) {
        Partidas.add(partidas);
    }
    
    public void guardarPartida(String nombre, Partida a){
        Partida guardar = new Partida();
        guardar = a;
        
        FileManager.writeObject(a, nombre+"=.ser");
    }
    
}
