package sensores;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Random;

public class Sensor {

    ConfigFile archivoConfig;
    TipoSensor tipoSensor;
    Integer temporizador;

    public Sensor(TipoSensor tipoSensor, Integer temporizador, String configFilePath) {
        // configFilePath: ruta del archivo de configuración en el sistema de archivos de la máquina.
        this.archivoConfig = createConfigFile(configFilePath);

        // ¿Qué unidad de medida utilizamos para el temporizador? ¿segundos, milisegundos,...?
        this.temporizador = temporizador;

        // ¿En qué formato recibimos el tipo de medición / sensor?
        this.tipoSensor = tipoSensor;

        // TODO: Desarrollar correctamente método connectToPubSub().
        connectToPubSub();
    }

    public ConfigFile createConfigFile(String configFilePath){

        // TODO: Abrir archivo, leer archivo y extraer los valores correspondientes a las siguientes variables.
        Double p_valorDentroDeRango = 0.0;
        Double p_valorFueraDeRango = 0.0;
        Double p_valorErroneo = 0.0;

        return new ConfigFile(p_valorDentroDeRango, p_valorFueraDeRango, p_valorErroneo);
    };

    public void connectToPubSub(){
        try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5556");

            Random srandom = new Random(System.currentTimeMillis());
            while (!Thread.currentThread().isInterrupted()) {
                // 'medicion': variable genérica y temporal (en el proyecto) para pruebas (primera entrega)
                int medicion;
                medicion = 10000 + srandom.nextInt(10000);

                //  Enviando mensaje a los suscriptores (monitores)...
                String update = String.format(
                        "%d", medicion
                );
                publisher.send(update, 0);
            }
        }
    }
}
