/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rummy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author juand
 */
public class Partida implements Serializable{
    int turno = 0;
    int pintarfilar = 0;
    String NombrePartida = "";
    ArrayList<String> nombres = new ArrayList<>();//Puede servir para saber nombres
    ArrayList<Mazo.Carta> cartasdeJugadores = new ArrayList<>();
    ArrayList<Mazo.Carta> cartasdeJugadores2 = new ArrayList<>();
    ArrayList<Mazo.Carta> cartasdeJugadores3 = new ArrayList<>();
    ArrayList<Mazo.Carta> cartasdeJugadores4 = new ArrayList<>();
    
//    ArrayList<threadServidor> jugadores = new ArrayList<>();
    
    ArrayList<Mazo.Carta> cartasJugadas = new ArrayList<>();
    Mazo cartas;
    private String estado = "En curso"; // Inicializado a un estado por defecto

    public Partida(Mazo cartas) {
        this.cartas = cartas;
    }
    public Partida() {
        
    }
    
    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getPintarfilar() {
        return pintarfilar;
    }

    public void setPintarfilar(int pintarfilar) {
        this.pintarfilar = pintarfilar;
    }

    public ArrayList<String> getNombres() {
        return nombres;
    }

    public void setNombres(ArrayList<String> nombres) {
        this.nombres = nombres;
    }
    
    public void agregarNombres(String nombres) {
        this.nombres.add(nombres);
    }

//    public ArrayList<threadServidor> getJugadores() {
//        return jugadores;
//    }
//
//    public void setJugadores(threadServidor juga) {
//        this.jugadores.add(juga);
//    }

    public ArrayList<Mazo.Carta> getCartasJugadas() {
        return cartasJugadas;
    }

    public void setCartasJugadas(ArrayList<Mazo.Carta> cartasJugadas) {
        this.cartasJugadas = cartasJugadas;
    }

    public Mazo getCartas() {
        return cartas;
    }

    public void setCartas(Mazo cartas) {
        this.cartas = cartas;
    }
    
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    } 

    public String getNombrePartida() {
        return NombrePartida;
    }

    public void setNombrePartida(String NombrePartida) {
        this.NombrePartida = NombrePartida;
    }

    public ArrayList<Mazo.Carta> getCartasdeJugadores(int i) {
        switch (i) {
            case 0:
                return cartasdeJugadores;
            case 1:
                return cartasdeJugadores2;
            case 2:
                return cartasdeJugadores3;
            default:
                return cartasdeJugadores4;
        }
    }

    public void setCartasdeJugadores(ArrayList<Mazo.Carta> cartasdeJugadores, int i) {
        switch (i) {
            case 0:
                this.cartasdeJugadores = cartasdeJugadores;
                break;
            case 1:
                this.cartasdeJugadores2 = cartasdeJugadores;
                break;
            case 2:
                this.cartasdeJugadores3 = cartasdeJugadores;
                break;
            default:
                this.cartasdeJugadores4 = cartasdeJugadores;
                break;
        }
    }
}
