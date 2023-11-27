/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rummy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author juand
 */
public class Cliente {
    public static String IP_SERVER = "localhost";
    JuegoRommy ventanaCliente; // Ventana del cliente
    
    DataInputStream entrada = null;//leer comunicacion
    DataOutputStream salida = null;//escribir comunicacion
    
    public Socket cliente = null;//para la comunicacion

    String Nombre;// nombre del user

    public Cliente(JuegoRommy vent) throws IOException{
        this.ventanaCliente = vent;
    }

    public void conexion() throws IOException{
        try{
            cliente = new Socket(Cliente.IP_SERVER, 8081);
            entrada = new DataInputStream(cliente.getInputStream());
            salida = new DataOutputStream(cliente.getOutputStream());

            Nombre = JOptionPane.showInputDialog("Introducir Nick :");
            ventanaCliente.setTitle(Nombre);
            salida.writeUTF(Nombre);
            System.out.println("Envia el nombre del cliente: "+Nombre);
        }catch (IOException e){
            System.out.println("\tEl servidor no esta levantado");
            System.out.println("\t=============================");
        }
        new threadCliente(entrada, ventanaCliente).start();
    }
    
    public String getNombre() {
        return Nombre;
    }
}
