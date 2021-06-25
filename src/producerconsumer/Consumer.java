package producerconsumer;

import Frames.TiendaA;

import javax.rmi.CORBA.Tie;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.io.*;
import java.net.*;

public class Consumer
{

    public static void Consumer(JTextArea textArea1, String cantidadProducto){
        try {
            DatagramSocket socketUDP = new DatagramSocket();
            byte[] buferp;
            InetAddress hostServidor = InetAddress.getByName("localhost");
            int puertoServidor = 6789;

            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));


            System.out.println("Cuanto quieres consumir?");
            //tienda.textArea1.append("\n Cuanto quieres consumir?");


            buferp = cantidadProducto.getBytes(); //lo pasamos a bytes para meterlo en el paquete

            // Construimos un datagrama para enviar el mensaje al servidor
            DatagramPacket cuanto = new DatagramPacket(buferp, buferp.length, hostServidor, puertoServidor);

            // Enviamos el datagrama
            socketUDP.send(cuanto); //envia por socket la cantidad de consola

            byte[] buferResSer = new byte[50];
            DatagramPacket respuesta = new DatagramPacket(buferResSer, buferResSer.length);
            socketUDP.receive(respuesta);

            System.out.println("Consumer consumed, quedan ");
            textArea1.append("\n Consumer consumed "+ cantidadProducto +", quedan " + new String(respuesta.getData()));
            socketUDP.close();

        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }

    public static String getLastLine(JTextArea textArea) throws BadLocationException {
        int end = textArea.getDocument().getLength();
        int start = Utilities.getRowStart(textArea, end);

        while (start == end)
        {
            end--;
            start = Utilities.getRowStart(textArea, end);
        }

        String text = textArea.getText(start, end - start);


        return text;
    }
}
