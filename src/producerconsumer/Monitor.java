package producerconsumer;

/*Clase que contiene void main para crear el thread del productor
e iniciar la conexion con el cliente. Dentro tiene la clase anidada tienda que hace
el papel de monitor al notificar al productor o colocar en espera al consumidor.
*/

import java.io.*;
import java.net.*;

public class Monitor
{
    public static void main(String args[])throws IOException, InterruptedException
    {
        final Tienda tienda = new Tienda();
        DatagramSocket socketUDP = new DatagramSocket(6789);

        // Create producer thread
        Thread producer = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true){
                    try {
                        tienda.put(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("PRODUCE");
                    System.out.println("Hay: " + tienda.cont);
                }
            }
        });

        producer.start();

        // Create consumer thread
        Thread consumer = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                try {
                    while(true){
                        byte[] buffer = new byte[2];
                        DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                        socketUDP.receive(peticion);

                        System.out.print("Datagrama recibido del host: " + peticion.getAddress());
                        System.out.println(" desde el puerto remoto: " + peticion.getPort() + " " + new String(peticion.getData()));

                        String cantidadString = new String(peticion.getData());             //Obtener el mensaje en bytes y pasarlo a string
                        if (cantidadString != null) {                                       //Por alguna razon el mensaje al pasarlo a string
                            cantidadString = cantidadString.trim();                         //contine espacios en blanco y se debe recortar
                        }
                        int cantidad = Integer.valueOf(cantidadString);                     //pasar de string a int

                        int item = tienda.get(cantidad);
                        String respuesta = Integer.toString(item);
                        buffer = respuesta.getBytes();

                        DatagramPacket respuestaDatagrama = new DatagramPacket(buffer, buffer.length, peticion.getAddress(), peticion.getPort());

                        // Enviamos la respuesta, que es un eco
                        socketUDP.send(respuestaDatagrama);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        consumer.start();
    }


    public static class Tienda {
        private int cont = 0;

        public synchronized void put(int value) throws InterruptedException
        {
            while (cont > 0)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    System.err.println("Contenedor: Error en put -> " + e.getMessage());
                }
            }
            Thread.sleep(5000);
            cont = cont + value;
            if (cont > 0) {
                notifyAll();
            }
        }

        public synchronized int get(int cantidad)
        {
            cont = cont - cantidad;     //descuenta primero la cantidad
            notify();                   //notifica al productor que consumió, y el productor verifica si hay suficientes
            while (cont <= 0)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    System.err.println("Contenedor: Error en get -> " + e.getMessage());
                }
            }
            return cont;
        }

    }
}
