package producerconsumer;


import java.io.*;
import java.net.*;

public class Consumer
{
    public static void main(String args[])throws IOException, InterruptedException
    {
        try {
            DatagramSocket socketUDP = new DatagramSocket();
            byte[] buferp;
            InetAddress hostServidor = InetAddress.getByName("localhost");
            int puertoServidor = 6789;
            boolean flag = true;

            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));



            while(true && flag){
                System.out.println("Want to consume?");
                String console_inp=sc.readLine();

                if(console_inp.equalsIgnoreCase("Yes")){
                    System.out.println("Cuanto quieres consumir?");

                    console_inp=sc.readLine();
                    buferp = console_inp.getBytes(); //lo pasamos a bytes para meterlo en el paquete

                    // Construimos un datagrama para enviar el mensaje al servidor
                    DatagramPacket cuanto = new DatagramPacket(buferp, buferp.length, hostServidor, puertoServidor);

                    // Enviamos el datagrama
                    socketUDP.send(cuanto); //envia por socket la cantidad de consola

                    byte[] buferResSer = new byte[50];
                    DatagramPacket respuesta = new DatagramPacket(buferResSer, buferResSer.length);
                    socketUDP.receive(respuesta);

                    System.out.println("Consumer consumed, quedan " + new String(respuesta.getData()));
                }else{
                    socketUDP.close();
                    flag = false;
                }

            }

        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }
}
