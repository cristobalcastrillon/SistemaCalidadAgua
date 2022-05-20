package sensores;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Random;

public class Sensor {

    private ConfigFile archivoConfig;
    private TipoSensor tipoSensor;
    private Integer temporizador;

    public Sensor(TipoSensor tipoSensor, Integer temporizador, String configFilePath) {
        // configFilePath: ruta del archivo de configuración en el sistema de archivos de la máquina.
        // Puede ser 'null'.
        this.archivoConfig = configFileEvaluateConditional(configFilePath);

        // ¿Qué unidad de medida utilizamos para el temporizador? ¿segundos, milisegundos,...?
        this.temporizador = temporizador;

        // ¿En qué formato recibimos el tipo de medición / sensor?
        this.tipoSensor = tipoSensor;

        // TODO: Desarrollar correctamente método connectToPubSub().
        connectToPubSub();
    }

    /**
     * @param configFilePath : String de la ruta (¿absoluta o relativa?) del archivo de configuración
     *                            (archivo csv, con los valores correspondientes a las probabilidades
     *                            de que un valor estédentro o fuera del rango aceptado; o sea erróneo).
     *                       Si el configFilePath es nula, se crea un archivo de configuración con valores aleatorios.
     * @return ConfigFile : Objeto que contiene las probabilidades mencionadas.
     */
    public ConfigFile configFileEvaluateConditional(String configFilePath){
        if(!configFilePath.isEmpty())
            return new ConfigFile(configFilePath);
        else
            return new ConfigFile();
    }

    public void connectToPubSub(){
        try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://localhost:5556");

            Random srandom = new Random(System.currentTimeMillis());

            // OJO: Valores quemados
            String tipoMedicion = "PH";

            // La siguiente condición es la correcta. Sin embargo, utilizamos un límite de 100 datos generados para PRUEBAS.
            while (!Thread.currentThread().isInterrupted()) {
                // 'medicion': variable genérica y temporal (en el proyecto) para pruebas (primera entrega)
                int medicion = 10000 + srandom.nextInt(10000);

                //  Formateando mensaje para enviar a los suscriptores (monitores)...
                String update = String.format(
                        "%s %d", tipoMedicion, medicion
                );

                // Siguiente línea: DEBUGGING
                System.out.println(medicion);

                //  Enviando mensaje a los suscriptores (monitores)...
                publisher.send(update, 0);
            }
        }
    }
}
