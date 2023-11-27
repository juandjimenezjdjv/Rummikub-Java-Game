/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rummy;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author juand
 */
public class threadCliente extends Thread{
   //solo de lectura
    DataInputStream entrada;
   
    JuegoRommy vcli; //referencia acliente
   
    public threadCliente (DataInputStream entrada,JuegoRommy vcli) throws IOException {
        this.entrada=entrada;
        this.vcli=vcli;
    }
    public void run() {
        String menser="",amigo="";
        int opcion=0;
        ArrayList<String> color = new ArrayList<>();
        ArrayList<Integer> numero = new ArrayList<>();
        ArrayList<Integer> id = new ArrayList<>();
      boolean isRunning = true;
      // solamente lee lo que el servidor threadServidor le envia
      while(isRunning) {         
            try{
             // esta leyendo siempre la instruccion, un int
                opcion=entrada.readInt();
            
            switch(opcion) {
                case 1://mensaje enviado
                    String nombreLista = entrada.readUTF();
                    System.out.println("El cliente recibe el nombre correctamente.");
                    vcli.agregarNombre(nombreLista);
                    break;
                    
                case 2://mensaje enviado
                    int bloquearbotones = entrada.readInt();
                    if (bloquearbotones == 1){
                        int cantidadCarta = entrada.readInt();
                        vcli.txfenblanco();
                        vcli.desBotonesCrear();
                        for (int j = 0; j < cantidadCarta; j++){
                            color.add(entrada.readUTF());
                            numero.add(entrada.readInt());
                            id.add(entrada.readInt());
                        }
                        String turno = entrada.readUTF();
                        vcli.cambiaTurnos(turno);
                        vcli.jugadoresTodos(entrada.readUTF());
                        //Ahora aqui hacer que las cartas aparescan, osea mostrarlas en el recuadro
                        vcli.actualizarCamposConCartas(color, numero);
                        
                    }else {
                        vcli.txfenblanco();
                    }
                    break;

                case 3:
                    String mensaje = entrada.readUTF();
                    
                    vcli.mostrar(mensaje);
                    break;
                    
                case 4:
//                    String mensaje = entrada.readUTF();
                    JOptionPane.showMessageDialog(null, "Se guardo la partida.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
                    isRunning=false;
                    vcli.dispose();
                    break;
                
                case 5:
                    int condi = entrada.readInt();
                    if (condi == 0){
                        
                        vcli.cambiaTurnos(entrada.readUTF());
                        vcli.jugadoresTodos(entrada.readUTF());
                        
                    }else if (condi == 1){
                        
                    }else if (condi==2){
                        
                    }
                    //este va a recibir las cartas y los cambios que pudieron ser
                    //si la jugada no sigue las reglas no pasa de turno
                    vcli.roboMazo=false;
                    break;
                    
                case 6://pedir de mazo
                    int desiciones = entrada.readInt();
                    if (desiciones == 1){
                        int cantCata = entrada.readInt();
                        color.clear(); numero.clear(); id.clear();
                        for (int j = 0; j < cantCata; j++){
                            color.add(entrada.readUTF());
                            numero.add(entrada.readInt());
                            id.add(entrada.readInt());
                        }
                        //Ahora aqui hacer que las cartas aparescan, osea mostrarlas en el recuadro
                        vcli.actualizarCamposConCartas(color, numero);
                    }else if (desiciones == 0){
                        int filade = entrada.readInt();
                        int cata = entrada.readInt();
                        color.clear(); numero.clear(); id.clear();
                        for (int j = 0; j < cata; j++){
                            color.add(entrada.readUTF());
                            numero.add(entrada.readInt());
                        }
                        //Ahora aqui hacer que las cartas aparescan, osea mostrarlas en el recuadro
                        vcli.actualizartablero(color, numero, filade);
                    }
                    break;
            }
         }
         catch (IOException e){
            JOptionPane.showMessageDialog(null, "Se cerro el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            vcli.dispose();
            System.out.println("Error en la comunicaci�n "+"Informaci�n para el usuario");
            break;
         }
      }
      System.out.println("se desconecto el servidor");
   }
   
}
