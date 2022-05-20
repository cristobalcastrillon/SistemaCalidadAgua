import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.util.StringTokenizer;

public class Monitor {
    private double[] receivedData = {};
    private TipoMedicion tipoMedicion;

    public Monitor(TipoMedicion tipoMedicion){
        this.tipoMedicion = tipoMedicion;
    }

    // TODO: Desarrollar método generarAlarma().
    private void generateAlarm(){}

    // TODO: Desarrollar método verificarUltimoValor().
    private void verifyLatestInput(){}

    private static void zmqSubscribe(TipoMedicion tipoMedicion){
        try (ZContext context = new ZContext()) {
            // Conexión al servidor
            System.out.println("Recibiendo datos de sensores...");

            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:5556");
            subscriber.subscribe((tipoMedicion.tipo).getBytes(ZMQ.CHARSET));

            // TEST: Se imprimen los primeros 100 valores de la medición
            for(int i = 0; i < 100; i++){
                String string = subscriber.recvStr(0).trim();

                StringTokenizer sscanf = new StringTokenizer(string, "  ");
                String tipoMedicion = sscanf.nextToken();
                int medicion = Integer.valueOf(sscanf.nextToken());

                System.out.println(
                        String.format("%s   %d", tipoMedicion, medicion)
                );
            }
        }
    }

    // Cada Monitor es un proceso corriendo sobre el SO, por ende, debe tener un punto de entrada (función main).
    public static void main(String[] args){
        zmqSubscribe();
    }

}
