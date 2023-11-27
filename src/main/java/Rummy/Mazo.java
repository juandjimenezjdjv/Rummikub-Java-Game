/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author juand
 */
public class Mazo implements Serializable{
    private String[] colores = {"black", "blue", "yellow", "red"};
    private int[] numeros = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    private String comodin = "comodin";

    private ArrayList<Carta> baraja;
    private int contadorID; // Contador para generar IDs únicos

    public Mazo() {
        baraja = new ArrayList<>();
        Random random = new Random();
        contadorID = 1; // Inicializar el contador de IDs

        // Añadir dos comodines
        baraja.add(new Carta("green", 77, obtenerID()));
        baraja.add(new Carta("green", 77, obtenerID()));

        for (String color : colores) {
            for (int numero : numeros) {
                baraja.add(new Carta(color, numero, obtenerID()));
                baraja.add(new Carta(color, numero, obtenerID())); // Se duplica cada carta para tener 104 cartas
            }
        }

        // Añadir dos cartas del mismo número y color
        int numeroElegido = numeros[random.nextInt(numeros.length)];
        String colorElegido = colores[random.nextInt(colores.length)];
    }

    private int obtenerID() {
        return contadorID++;
    }

    public ArrayList<Carta> getBaraja() {
        return baraja;
    }

    public class Carta implements Serializable{
        private String color;
        private int numero;
        private int id; // Agregar un campo para el ID

        public Carta(String color, int numero, int id) {
            this.color = color;
            this.numero = numero;
            this.id = id;
        }

        public String getColor() {
            return color;
        }

        public int getNumero() {
            return numero;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Carta{" +
                    "id=" + id +
                    ", color='" + color + '\'' +
                    ", numero=" + numero +
                    '}';
        }
    }
    
}
