import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

public class Monitor {
    private static LinkedList<Double> receivedData = new LinkedList<>();
    private static TipoMedicion tipoMedicion;

    public Monitor(TipoMedicion tipoMedicion){
        this.tipoMedicion = tipoMedicion;
    }

    // TODO: Desarrollar método generarAlarma().
    // Título final : generateAlarm()
    private void generarAlarm(){}

    // TODO: Desarrollar método verificarUltimoValor().
    // Título final : verifyLatestInput()
    private void verificarUltimoValor(){}

    /**
     * @param subAddress : dirección del proxy (last-value cache) en formato "tcp://hostname:port".
     * @param tipoMedicion : String del tipo de medición al que se subscribirá el monitor.
     */
    private static void zmqHandle(String subAddress, String clientAddress, String tipoMedicion){
        try (ZContext context = new ZContext()) {
            // Conexión al servidor
            System.out.println("Recibiendo datos de sensores...");

            ZMQ.Socket client = context.createSocket(SocketType.REQ);
            client.connect(clientAddress);

            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect(subAddress);
            subscriber.subscribe(tipoMedicion.getBytes(ZMQ.CHARSET));

            while(!Thread.currentThread().isInterrupted()){
                String stringFromSub = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(stringFromSub, " ");
                String tipo = sscanf.nextToken();
                String idSensor = sscanf.nextToken();
                String medicion = sscanf.nextToken();

                System.out.println(
                        tipo + '\t' + idSensor + '\t' + medicion
                );

                // Cambiando el formato de los números de tipo Double de manera que no ocurra una NumberFormatException...
                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                Number number = format.parse(medicion);

                // Se guardan en una lista enlazada todas las mediciones realizadas
                // por los sensores del tipo al que está suscrito el monitor.
                Monitor.receivedData.add(number.doubleValue());
                if(receivedData.getLast() < 0){
                    client.send(stringFromSub);
                    String recibido = client.recvStr(0);
                    // TODO: Debug
                    System.out.println(recibido);
                }
            }
            // TODO: Find out if this is the correct usage of the following methods.
            subscriber.close();
            client.close();
            context.destroy();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args: La dirección (host + nro. de puerto) del proxy se pasa como primer argumento al momento de ejecutar el programa.
     *            Además, el tipo de sensor al que estará suscrito el Monitor se pasa como segundo argumento.
     *            Si no es así, se toma como dirección por defecto: localhost + puerto 5557;
     *            y como tipo de medición, un tipo de medición seleccionado aleatoriamente.
     */
    public static void main(String[] args){

        String subAddress;
        String clientAddress = "tcp://localhost:5558";
        String tipoMedicion;

        if (args.length == 0) {
            System.out.println("No se ha ingresado ningún parametro en la línea de comandos.\nSe generará un Monitor aleatorio.");
            Random rd = new Random();
            subAddress = "tcp://localhost:5557";
            clientAddress = "tcp://localhost:5558";
            tipoMedicion = TipoMedicion.retrieveStringByType(TipoMedicion.values()[rd.nextInt(3)]);
        } else {
            subAddress = args[0];
            //clientAddress = args[1];
            tipoMedicion = args[1];
        }

        zmqHandle(subAddress, clientAddress, tipoMedicion);
    }

}
