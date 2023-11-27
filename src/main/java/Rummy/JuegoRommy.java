/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Rummy;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author juand
 */
public class JuegoRommy extends javax.swing.JFrame {
    int bloquear = 0;
    ArrayList<String> clr = new ArrayList<>();
    ArrayList<Integer> nmr = new ArrayList<>();
    ArrayList<Integer> posiCarta = new ArrayList<>();
    boolean roboMazo = false;
    /**
     * Creates new form JuegoRommy
     */
    public JuegoRommy() {
        try{
            initComponents();
            pnlPrincipal.setLayout(null);
            
            cliente = new Cliente(this);
            cliente.conexion();
        }catch (IOException ex){
        }
    }
    
    Cliente cliente;
    
    public static int DIMENSIONES = 13;
    public static int DIMEN = 13;
    public static int DIMENSION = 13;
    // Tablero con objetos JButton
    JButton[][] tableroLabels = new JButton[DIMENSIONES][DIMENSIONES];
    // tablero logico, indica el status del boton, si disparado o no
    int[][] tableroLogico = new int[DIMENSIONES][DIMENSIONES];
    // crea imagen blanco
//    ImageIcon iconoVacio = new ImageIcon(getClass().getResource("cvacio.GIF"));
//    ImageIcon iconoVacio = new ImageIcon(getClass().getResource("cvacio.GIF"));
    
    
    void generarTablero()
    {
        for(int i=0;i<DIMENSIONES;i++)
        {
            for(int j=0;j<DIMEN;j++)
            {
                // coloca imagen a todos vacio
                tableroLabels[i][j] = new JButton("");
                //añade al panel el boton;
                pnlPrincipal.add(tableroLabels[i][j]);
                // coloca dimensiones y localidad
                tableroLabels[i][j].setBounds(60+30*i, 40+30*j, 30, 30);
//                tableroLabels[i][j].setBounds(100+50*i, 100+50*j, 50, 50);
                // coloca el comand como i , j 
                tableroLabels[i][j].setActionCommand(i+","+j);//i+","+j
                
                //aclickSobreTablero(evt);ñade el listener al boton
                tableroLabels[i][j].addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                        
                    clickSTablero(evt);
                    
                }
                });
                // en logico indica estado en disponible
                tableroLogico[i][j]=0;
            }
        }
    }
    void actualizartablero(ArrayList<String> color, ArrayList<Integer> num, int fc) {
        int indiceCarta = 0;
        for(int i = 0; i < 13; i++) {
//            for(int j = 0; j < 2; j++) {
                tableroLabels[i][fc].setIcon(null);
                tableroLabels[i][fc].setText("");
                if(indiceCarta < color.size()) {
                    tableroLabels[i][fc].setIcon(null);
                    tableroLabels[i][fc].setText("<html><font color='" + color.get(indiceCarta) + "'>" + num.get(indiceCarta) + "</font></html>");
                    indiceCarta++;
                } else {
                    
                }
//            }
        }
    }
    
    JButton[][] tableroperso = new JButton[DIMENSION][DIMENSION];
    int[][] tableroLogicoPerso = new int[DIMENSION][DIMENSION];
    void gTableroMazo()
    {
        for(int i=0;i<13;i++)
        {
            for(int j=0;j<2;j++)
            {
                
                // coloca imagen a todos vacio
                tableroperso[i][j] = new JButton("");
                //añade al panel el boton;
                pnlPrincipal.add(tableroperso[i][j]);
                // coloca dimensiones y localidad
                tableroperso[i][j].setBounds(85+30*i, 610+30*j, 30, 30);
//                tableroLabels[i][j].setBounds(100+50*i, 100+50*j, 50, 50);
                // coloca el comand como i , j 
                tableroperso[i][j].setActionCommand(i+","+j);//i+","+j
                
                //aclickSobreTablero(evt);ñade el listener al boton
                tableroperso[i][j].addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                        
                    clickSobreTablero(evt);
                    
                }
                });
                // en logico indica estado en disponible
                tableroLogicoPerso[i][j]=0;
            }
        }
    }
    
    void actualizarCamposConCartas(ArrayList<String> color, ArrayList<Integer> num) {
        int indiceCarta = 0;
        for(int i = 0; i < 13; i++) {
            for(int j = 0; j < 2; j++) {
                tableroperso[i][j].setIcon(null);
                tableroperso[i][j].setText("");
                if(indiceCarta < color.size()) {
                    tableroperso[i][j].setIcon(null);
                    tableroperso[i][j].setText("<html><font color='" + color.get(indiceCarta) + "'>" + num.get(indiceCarta) + "</font></html>");
                    indiceCarta++;
                } else {
                    
                }
            }
        }
    }
    
    
    // reiniciar el juego es poner todo como en un inicio
    public void reiniciarJuego() {
        for(int i=0;i<DIMENSIONES;i++)
        {
            for(int j=0;j<DIMENSIONES;j++)
            {
                tableroLabels[i][j].setText("");
                tableroLogico[i][j]=0;
            }
        }
    }
    public void clickSTablero(java.awt.event.MouseEvent evt) {
        
        System.out.println("ACA CLICK SOBRE TABLERO");
        // obtiene el boton 
        JButton botonTemp = (JButton)evt.getComponent();
        // obtiene el i,j de action command del boton
        String identificadorBoton = botonTemp.getActionCommand();
        
        // separa el string del action comand para obtener columnas
        int columna = 
          Integer.parseInt(identificadorBoton.substring(0,identificadorBoton.indexOf(",")));
        int fila = 
          Integer.parseInt(identificadorBoton.substring(1+identificadorBoton.indexOf(",")));
        
        // si ya se disparo entonces nada
        if(tableroLogico[columna][fila]!=0)
            return;
        
    }
    
    public void clickSobreTablero(java.awt.event.MouseEvent evt) {
        
        System.out.println("ACA CLICK SOBRE TABLERO");
        // obtiene el boton 
        JButton botonTemp = (JButton)evt.getComponent();
        // obtiene el i,j de action command del boton
        String identificadorBoton = botonTemp.getActionCommand();
        
        // separa el string del action comand para obtener columnas
        int columna = 
          Integer.parseInt(identificadorBoton.substring(0,identificadorBoton.indexOf(",")));
        int fila = 
          Integer.parseInt(identificadorBoton.substring(1+identificadorBoton.indexOf(",")));
        
        // si ya se disparo entonces nada
        if(tableroLogico[columna][fila]!=0)
            return;
        
        Pattern pattern = Pattern.compile("<font\\s+color=['\"](\\w+)['\"]>(\\d+)</font>");
        Matcher matcher = pattern.matcher(tableroperso[columna][fila].getText());
        String color = "";
        String number = "";
        // Verificar si se encuentra el patrón
        if (matcher.find()) {
            // Obtener el color y el número
            color = matcher.group(1);
            number = matcher.group(2);

            // Imprimir los resultados
            System.out.println("Color: " + color);
            System.out.println("Número: " + number);
            System.out.println("Columna: "+ columna+"\nFila: "+fila);
        } else {
            System.out.println("No se encontró el patrón en la cadena HTML.");
        }
        int posi =0;
        if (fila == 0){
            posi = (columna*2);
        }else{
            posi = ((columna*2)+1);
        }
        
        if (color != "" || number != ""){
            clr.add(color);
            nmr.add(Integer.parseInt(number));
            posiCarta.add(posi);
        }
        System.out.println("CAntidad de cartar agregadas: "+posiCarta.size());
        for (int i = 0; i<posiCarta.size(); i++){
            System.out.println(posiCarta.get(i));
        } 
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCargarPartida = new javax.swing.JList<>();
        btnSalirPartida = new javax.swing.JButton();
        btnUnirsePartida = new javax.swing.JButton();
        btnCrearPartida = new javax.swing.JButton();
        btnUnirse = new javax.swing.JButton();
        txfNombrePartida = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaJugadores = new javax.swing.JTextArea();
        btnComenzarNuevo = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaChat = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        txfMensaje = new javax.swing.JTextField();
        btnRobarMazo = new javax.swing.JButton();
        lblTurno = new javax.swing.JLabel();
        lblMazo = new javax.swing.JLabel();
        btnSiguiente = new javax.swing.JButton();
        btnRobar1 = new javax.swing.JButton();
        btnRobar2 = new javax.swing.JButton();
        btnRobar3 = new javax.swing.JButton();
        lblJugadoresCartas = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlPrincipal.setMinimumSize(new java.awt.Dimension(900, 700));

        lstCargarPartida.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 153, 153)));
        lstCargarPartida.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = cargarNomPartida();
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstCargarPartida.setPreferredSize(new java.awt.Dimension(400, 400));
        jScrollPane1.setViewportView(lstCargarPartida);

        btnSalirPartida.setText("Salir");
        btnSalirPartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirPartidaMouseClicked(evt);
            }
        });

        btnUnirsePartida.setText("Unirse a Partida");
        btnUnirsePartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUnirsePartidaMouseClicked(evt);
            }
        });

        btnCrearPartida.setText("Crear Partida");
        btnCrearPartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCrearPartidaMouseClicked(evt);
            }
        });

        btnUnirse.setText("Unirse");
        btnUnirse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUnirseMouseClicked(evt);
            }
        });

        txaJugadores.setEditable(false);
        txaJugadores.setColumns(20);
        txaJugadores.setRows(5);
        jScrollPane2.setViewportView(txaJugadores);

        btnComenzarNuevo.setText("Comenzar Juego");
        btnComenzarNuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnComenzarNuevoMouseClicked(evt);
            }
        });

        txaChat.setEditable(false);
        txaChat.setColumns(20);
        txaChat.setRows(5);
        jScrollPane3.setViewportView(txaChat);

        btnEnviar.setText("Enviar");
        btnEnviar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEnviarMouseClicked(evt);
            }
        });

        btnRobarMazo.setText("Robar de Mazo");
        btnRobarMazo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRobarMazoMouseClicked(evt);
            }
        });

        lblTurno.setText("Turno de: ");

        lblMazo.setText("Tu tablero ->");

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSiguienteMouseClicked(evt);
            }
        });

        btnRobar1.setText("1");

        btnRobar2.setText("2");

        btnRobar3.setText("3");

        lblJugadoresCartas.setText("-");

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSalirPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblJugadoresCartas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(lblTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(btnEnviar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPrincipalLayout.createSequentialGroup()
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnRobar3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(btnRobar2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRobar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(lblMazo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRobarMazo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlPrincipalLayout.createSequentialGroup()
                            .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txfMensaje, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnlPrincipalLayout.createSequentialGroup()
                                    .addComponent(btnCrearPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnUnirse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                                .addComponent(txfNombrePartida, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3))
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                            .addComponent(btnComenzarNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(45, 45, 45)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(btnUnirsePartida, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalirPartida)
                    .addComponent(btnUnirsePartida)
                    .addComponent(lblJugadoresCartas))
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUnirse)
                            .addComponent(btnCrearPartida)))
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(btnRobar1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(btnRobar2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(btnRobar3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(txfNombrePartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnComenzarNuevo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRobarMazo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMazo))
                        .addGap(42, 42, 42)))
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(txfMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSiguiente)
                    .addComponent(lblTurno))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEnviarMouseClicked
        try {
            // se toma lo escrito
            System.out.println("Click boton de enviar-----------");
            String mensaje = txfMensaje.getText();
            
            // se limpia el textfield
            txfMensaje.setText("");

            // envia al server la opcion 4 para que le pase al enemigo
            // lo escrito
            cliente.salida.writeInt(3);
//                // le envia el mensaje
            cliente.salida.writeUTF(cliente.Nombre);
            cliente.salida.writeUTF(cliente.Nombre+": "+mensaje);
            
        } catch (IOException ex) {

        }
    }//GEN-LAST:event_btnEnviarMouseClicked

    private void btnUnirsePartidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnirsePartidaMouseClicked
        if (lstCargarPartida.isSelectionEmpty()){//Si no tiene nada seleccionado, ponga un mensaje de error
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una partida", "Seleccionar Partida", JOptionPane.WARNING_MESSAGE);
        }else{
            //TODO agragar que todos los demas botones se desactiven y que el chat se active

            //TODO activar el boton de salir de partida
        }
    }//GEN-LAST:event_btnUnirsePartidaMouseClicked

    private void btnSalirPartidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirPartidaMouseClicked
          
            try {
                cliente.salida.writeInt(4);//Este envia el nombre de la persona que salio del juego para que guarde la partida
//                // le envia el mensaje
                cliente.salida.writeUTF(cliente.Nombre);      
            } catch (IOException ex) {
                
            }

    }//GEN-LAST:event_btnSalirPartidaMouseClicked

    private void btnCrearPartidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCrearPartidaMouseClicked
        if (bloquear == 0){
            String texto = txfNombrePartida.getText().trim(); // Obtener texto y eliminar espacios en blanco al principio y al final
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Poner nombre a la partida.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    cliente.salida.writeInt(1);
                    cliente.salida.writeUTF(cliente.getNombre());
                    System.out.println("Nombre del cliente prueba: " + cliente.getNombre());
                    System.out.println("Decirle aqui al servidor que puede aceptar jugadores.");
                    
                } catch (IOException ex) {
                    
                }
            }
        }
    }//GEN-LAST:event_btnCrearPartidaMouseClicked

    private void btnUnirseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUnirseMouseClicked
        String texto = txfNombrePartida.getText().trim(); // Obtener texto y eliminar espacios en blanco al principio y al final
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Poner nombre a la partida.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                cliente.salida.writeInt(1);
                cliente.salida.writeUTF(cliente.getNombre());
                System.out.println("Nombre del cliente prueba: " + cliente.getNombre());


                System.out.println("Decirle aqui al servidor que puede aceptar jugadores.");
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_btnUnirseMouseClicked

    private void btnComenzarNuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnComenzarNuevoMouseClicked
        if (txaJugadores.getText() == "") return;
        try {
            //Aqui se debe limpiar las lista de nueva partida
            cliente.salida.writeInt(2);
        
        
            String texto = txaJugadores.getText();
            String[] partes = texto.split("\n");

            List<String> lista = Arrays.asList(partes);
            //TODO validacion para que no entren menos de 2

            // Convertir la lista de cadenas a una sola cadena
            String textoAEnviar = String.join(",", lista);

            // Luego, puedes escribir la cadena en el flujo de salida
            cliente.salida.writeUTF(textoAEnviar);
            cliente.salida.writeUTF(txfNombrePartida.getText());
            
        } catch (IOException ex) {
            
        }
    }//GEN-LAST:event_btnComenzarNuevoMouseClicked

    private void btnSiguienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSiguienteMouseClicked
        try {
            if (validaJugada()==1){//Ya esta hecha
                cliente.salida.writeInt(5);
                cliente.salida.writeUTF(cliente.getNombre());
                cliente.salida.writeInt(1);
                //Pasar posiciones en la lista para ir a buscarlas en la lista del server
                Collections.sort(posiCarta);
                for (int i = 0; i < posiCarta.size(); i++){
                    System.out.println("Posicion: " + posiCarta.get(i));
                    //Poner aqui las posiciones a enviar salida.cliente...
                }
                
                //quitar datos de la lista                
                
            }else if (validaJugada()==2){//Este es para el caso especifico de pasar las cartas por posiciones
                System.out.println("Entra aqui");
                int a = posiCarta.size();
                cliente.salida.writeInt(5);
                cliente.salida.writeUTF(cliente.getNombre());
                cliente.salida.writeInt(2);
                cliente.salida.writeInt(a);//Este es el tamano de la lista lo ocupara saber cuantas veces iterar luego
                for (int i = 0; i < a; i++){
                    cliente.salida.writeInt(posiCarta.get(i));//Este envia las posiciones de las cartas para buscar el id
                }
                posiCarta.clear();
            }else{

            }
        } catch (IOException ex) {
            
        }
        nmr.clear();
        clr.clear();
        posiCarta.clear();
    }//GEN-LAST:event_btnSiguienteMouseClicked

    private void btnRobarMazoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRobarMazoMouseClicked
        if (roboMazo == true){
            JOptionPane.showMessageDialog(null, "Ya tomaste cartas del maso", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            try {
                roboMazo = true;
                cliente.salida.writeInt(6);
                cliente.salida.writeUTF(cliente.getNombre());
            } catch (IOException ex) {
                
            }
        }
    }//GEN-LAST:event_btnRobarMazoMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JuegoRommy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JuegoRommy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JuegoRommy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JuegoRommy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JuegoRommy().setVisible(true);
            }
        });
    }
    
    public void mostrar(String texto)
    {
        txaChat.append(texto+"\n");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnComenzarNuevo;
    private javax.swing.JButton btnCrearPartida;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnRobar1;
    private javax.swing.JButton btnRobar2;
    private javax.swing.JButton btnRobar3;
    private javax.swing.JButton btnRobarMazo;
    private javax.swing.JButton btnSalirPartida;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnUnirse;
    private javax.swing.JButton btnUnirsePartida;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblJugadoresCartas;
    private javax.swing.JLabel lblMazo;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JList<String> lstCargarPartida;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JTextArea txaChat;
    private javax.swing.JTextArea txaJugadores;
    private javax.swing.JTextField txfMensaje;
    private javax.swing.JTextField txfNombrePartida;
    // End of variables declaration//GEN-END:variables

public int desBotonesCrear(){
//        if (num == 2) return 2;//        btnUnirse //Este return es para desactivar los botones cuando se le da crear partida
        txfNombrePartida.setVisible(false);
        btnUnirsePartida.setVisible(false);
        btnCrearPartida.setVisible(false);
        lstCargarPartida.setVisible(false);
        btnComenzarNuevo.setVisible(false);
        btnUnirse.setVisible(false);
        
        generarTablero();
        gTableroMazo();
        
        return 0;
    }
    public String[] cargarNomPartida(){
        String rutaDirectorio = "C:\\Users\\juand\\Documents\\NetBeansProjects\\AA_RommyJJ\\";
        ArrayList<String> archivosSer;
        archivosSer = listarArchivosSer(rutaDirectorio);
        int i =0;
        String[] nArchivo;
        ArrayList<String> listaArchivos = new ArrayList<>();
        for (String nombreArchivo : archivosSer) {
                nArchivo = nombreArchivo.split("=");
                listaArchivos.add(nArchivo[0]);
            i++;
        }
        String[] Partidas = listaArchivos.toArray(new String[0]);
        return Partidas;
    }
    
    public ArrayList<String> listarArchivosSer(String rutaDirectorio) {
        ArrayList<String> archivosSer = new ArrayList<>();

        File directorio = new File(rutaDirectorio);
        File[] archivos = directorio.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isFile() && archivo.getName().endsWith(".ser")) {
                    archivosSer.add(archivo.getName());
                }
            }
        }
        return archivosSer;
    }

    
    public void agregarNombre(String nombre){
        System.out.println("El nombre a agregar es: "+nombre);
        
        String Nombres = ""+ txaJugadores.getText();
        System.out.println("El los nombres son: "+Nombres);
        txaJugadores.setText(Nombres+nombre+"\n");
    }
    
    public void txfenblanco(){
//        txfNombrePartida.setText("");
        txaJugadores.setText("");
        System.out.println("SE SUPONE QUE BORRA.");
    }
    
    public void cambiaTurnos(String jug){
        lblTurno.setText(jug);
    }
    
    public void jugadoresTodos(String jug){
        lblJugadoresCartas.setText(jug);
    }
    
    public int validaJugada(){
        if (roboMazo == true &&  posiCarta.size() < 3){
            return 1;
        }else if (posiCarta.size()<3 && roboMazo != true){//0=NoValida  1=roboPuedePasar  2=jugadaValida
            JOptionPane.showMessageDialog(null, "Esa jugada no es valida\nSelecciona otra jugada o\n roba del maso y siguiente.", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        } else {
            if (coloresIgual() == true){
                if (numerosSecuencia() == true){
                    return 2;
                }else {
                    //aqui se debe poner algo que limpie las listas para que pueda rehacer la jugada
                    nmr.clear();
                    clr.clear();
                    posiCarta.clear();
                    JOptionPane.showMessageDialog(null, "Esa jugada no es valida\nSelecciona otra jugada o\n roba del maso y siguiente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return 0;
                }
            }else if (coloresIgual() == false){
                //seguimos revisando que los numero sean iguales
                if (numerosIgual()==true && coloresDistintos()==true){
                    return 2;
                }else{
                    //aqui se debe poner algo que limpie las listas para que pueda rehacer la jugada
                    nmr.clear();
                    clr.clear();
                    posiCarta.clear();
                    JOptionPane.showMessageDialog(null, "Esa jugada no es valida\nSelecciona otra jugada o\n roba del maso y siguiente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return 0;
                }
            }
            return 0;
        }
    }
    public static final int GAP_FOR_WILDCARD = 2;
    public static final int WILDCARD_NUMBER = 77;

    public boolean numerosSecuencia() {
        for (int i = 1; i < nmr.size(); i++) {
            System.out.println("Numeros :"+ nmr.get(i));
            int diff = nmr.get(i) - nmr.get(i - 1);
            if (i == 1 && nmr.get(0) == WILDCARD_NUMBER) {
                // Primer número es 77, no es necesario verificar la diferencia
                continue;
            }
//            if (diff != 1 && nmr.get(i - 1) != WILDCARD_NUMBER) {
            if (diff != 1 && !(nmr.get(i - 1) == WILDCARD_NUMBER || nmr.get(i) == WILDCARD_NUMBER)) {
                // No son consecutivos y no se cumple la condición del comodín 77
                System.out.println("No son numeros consecutivos");
                return false;
            }
        }
        return true;
    }
    
    public boolean numerosIgual(){
        int num = nmr.get(0);
        for (int i = 1; i < nmr.size(); i++) {
            if (nmr.get(i).equals(num) || nmr.get(i).equals(77)){
                //Aqui es si son iguales
            } else {
                return false;
            }
        }
        return true;
    }
    
    public boolean coloresIgual() {//Probar este caso
        String color = clr.get(0);
        for (int i = 0; i < posiCarta.size();i++){
            if (clr.get(i).equals(color) || clr.get(i).equals("green")){

            }else{
                return false;
            }
        }
        return true;
    }
    
    public boolean coloresDistintos() {
        Set<String> conjunto = new HashSet<>();

        for (String color : clr) {
            if (!conjunto.add(color)) {
                // Si no se puede agregar el color al conjunto, significa que ya existe en la lista
                return false;
            }
        }
        // Si llegamos aquí, todos los elementos son distintos
        return true;
    }
}
