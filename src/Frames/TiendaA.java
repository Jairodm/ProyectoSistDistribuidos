package Frames;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import producerconsumer.Consumer;
import producerconsumer.Monitor;

public class TiendaA {
    private JButton button1;
    private JPanel panel1;
    public JTextArea textArea1;
    private JSpinner spinner1;
    private JButton enviarButton;
    public String cantidadProducto;

    public TiendaA() {
        textArea1.setSize(200,200);
        spinner1.setVisible(false);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //JOptionPane.showMessageDialog(null, "Hello World");
                textArea1.append("hola que hace \n");
                spinner1.setVisible(true);
                textArea1.append("¿Cuanto quieres consumir? \n");
            }
        });

        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cantidadProducto = spinner1.getValue().toString();
            }
        });

        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Consumer.Consumer(textArea1, cantidadProducto);
                spinner1.setVisible(false);
            }
        });
    }


    public static void main(String args[]) throws IOException, InterruptedException {
        JFrame frame = new JFrame("TiendaA");
        TiendaA tienda = new TiendaA();
        frame.setContentPane(tienda.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.setSize(500, 500);
        //frame.setLocation(200, 200);
        //frame.setBounds(400,400, 400, 400);
        frame.setLocationRelativeTo(null);



        Monitor.main(args, tienda);
        //Consumer.main(args);

    }





}
