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
    private void generateAlarm(){}

    // TODO: Desarrollar método verificarUltimoValor().
    private void verifyLatestInput(){}

    /**
     * @param address : dirección en formato "tcp://hostname:port".
     * @param tipoMedicion : String del tipo de medición al que se subscribirá el monitor.
     */
    private static void zmqSubscribe(String address, String tipoMedicion){
        try (ZContext context = new ZContext()) {
            // Conexión al servidor
            System.out.println("Recibiendo datos de sensores...");

            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect(address);
            subscriber.subscribe(tipoMedicion.getBytes(ZMQ.CHARSET));

            // TODO: Replace following chunk of code with business logic.
            // TEST: Se imprimen los primeros 100 valores de la medición
            for(int i = 0; i < 100; i++){
                String string = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(string, "  ");
                String tipo = sscanf.nextToken();
                int medicion = Integer.valueOf(sscanf.nextToken());

                System.out.println(
                        String.format("%s   %d", tipo, medicion)
                );
            }
        }
    }

    // Cada Monitor es un proceso corriendo sobre el SO, por ende, debe tener un punto de entrada (función main).
    public static void main(String[] args){
        zmqSubscribe("tcp://localhost:5556", tipoMedicion.tipo);
    }

}
