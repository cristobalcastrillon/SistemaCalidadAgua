import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.util.StringTokenizer;

public class Monitor {
    private double[] receivedData = {};
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
     * @param address : dirección del proxy (last-value cache) en formato "tcp://hostname:port".
     * @param tipoMedicion : String del tipo de medición al que se subscribirá el monitor.
     */
    private static void zmqSubscribe(String address, String tipoMedicion){
        try (ZContext context = new ZContext()) {
            // Conexión al servidor
            System.out.println("Recibiendo datos de sensores...");

            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect(address);
            subscriber.subscribe(tipoMedicion.getBytes(ZMQ.CHARSET));

            while(!Thread.currentThread().isInterrupted()){
                String string = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(string, " ");
                String tipo = sscanf.nextToken();
                String idSensor = sscanf.nextToken();

                // TODO: Castear medición (String) a Double para evaluar
                //  y generar alarma al Sistema de Calidad si es del caso.
                String medicion = sscanf.nextToken();

                System.out.println(
                        tipo + '\t' + idSensor + '\t' + medicion
                );
            }
            // TODO: Find out if this is the correct usage of the following methods.
            subscriber.close();
            context.destroy();
        }
    }

    /**
     * @param args: La dirección (host + nro. de puerto) del proxy se pasa como primer argumento al momento de ejecutar el programa.
     *            Además, el tipo de sensor al que estará suscrito el Monitor se pasa como segundo argumento.
     *            Si no es así, se toma como dirección por defecto: localhost + puerto 5557;
     *            y como tipo de medición, pH.
     */
    public static void main(String[] args){
        zmqSubscribe("tcp://localhost:5557", tipoMedicion.PH.tipo);
    }

}
