package producerconsumer;


import java.io.*;
import java.net.*;

public class Consumer
{
    public static void main(String args[])throws IOException, InterruptedException
    {
        Socket s=new Socket("localhost",7000);
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

        //Streams
        PrintStream out = new PrintStream(s.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        while(true){
            System.out.println("Want to consume?");
            String console_inp=sc.readLine();

            if(console_inp.equalsIgnoreCase("Yes")){
                System.out.println("Cuanto quieres consumir?");
                console_inp=sc.readLine();
                out.println(console_inp);       //envia por socket la cantidad por consola

                String item=in.readLine();      //recibe por socket

                System.out.println("Consumer consumed, quedan "  + item);
            }


        }

    }
}
