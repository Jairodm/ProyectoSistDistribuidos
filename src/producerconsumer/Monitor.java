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
        ServerSocket s=new ServerSocket(7000);
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

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

        //Accept consumer
        Socket ss2=s.accept();

        // Create consumer thread
        Thread consumer = new Thread(new Runnable()
        {
            PrintStream out = new PrintStream(ss2.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(ss2.getInputStream()));

            @Override
            public void run()
            {
                int cantidad = 0;
                while(true){
                    String mensaje = null;
                    try {
                        mensaje = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cantidad = Integer.parseInt(String.valueOf(mensaje));

                    int item = tienda.get(cantidad);

                    out.println(item);
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
