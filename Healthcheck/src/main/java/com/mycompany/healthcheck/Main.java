/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.healthcheck;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 *
 * @author Jose
 */
public class Main {
    
    private static Integer receivedData[] = null;

    private static void zmqHandle(String clientAddress1, String clientAddress2, String clientAddress3) throws IOException{
        try (ZContext context = new ZContext()) {
            // Conexión al servidor
            System.out.println("Chequeando monitores");
            
            ZMQ.Socket client1 = context.createSocket(SocketType.REQ);//pH
            client1.connect(clientAddress1);
            ZMQ.Socket client2 = context.createSocket(SocketType.REQ);//Temperatura
            client2.connect(clientAddress2);
            ZMQ.Socket client3 = context.createSocket(SocketType.REQ);//Oxigeno
            client3.connect(clientAddress3);
            Runtime run ;
            Process proc;
            //C:\Users\jomit\SistemaCalidadAgua\Healthcheck\jdk1.8.0_201\bin
            //"C:\Users\jomit\SistemaCalidadAgua\Healthcheck\apache-maven-3.8.5\bin\mvn"
            String commandCompile = System.getProperty("user.dir")+File.separator+"jdk1.8.0_201"+File.separator+
                 "bin"+File.separator+"javac ";
            String commandExecute = System.getProperty("user.dir")+File.separator+"jdk1.8.0_201"+File.separator+
                 "bin"+File.separator+"java -jar ";
            String commandMavenCompile = System.getProperty("user.dir")+File.separator+"apache-maven-3.8.5"+File.separator+
                 "bin"+File.separator+"mvn\sclean\scompile\spackage";

            while(!Thread.currentThread().isInterrupted()){
                client1.setReceiveTimeOut(500);
                if(client1.send("1")){
                   run  = Runtime.getRuntime();
                   //proc = run.exec(commandMavenCompile.concat("Monitor"));
                   proc = run.exec(commandExecute+"target"+File.separator+"Monitor-1.0-SNAPSHOT.jar tcp://localhost:5557 tcp://localhost:5558 pH");
                   System.out.println("dale");
                }
                
//                client2.send("2");
//                if(client2.recvStr().isEmpty()){
//                   run  = Runtime.getRuntime();
//                   proc = run.exec(commandMavenCompile+"Monitor");
//                   proc = run.exec(commandExecute+"target"+File.separator+"Monitor-1.0-SNAPSHOT.jar tcp://localhost:5557 tcp://localhost:5558 Temperatura");
//                }
//                
//                client3.send("3");
//                if(client3.recvStr().isEmpty()){
//                   run  = Runtime.getRuntime();
//                   proc = run.exec(commandMavenCompile+"Monitor");
//                   proc = run.exec(commandExecute+"target"+File.separator+"Monitor-1.0-SNAPSHOT.jar tcp://localhost:5557 tcp://localhost:5558 Oxigeno");
//                }
            }
            // TODO: Find out if this is the correct usage of the following methods.
            
            client1.close();
            client2.close();
            client3.close();
            context.destroy();
        }    }
    public static void main(String[] args) throws IOException {
        zmqHandle("tcp://localhost:5559", "tcp://localhost:5560", "tcp://localhost:5561");
        
    }
}
