package sensores;

import com.sun.tools.javac.util.Pair;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.Random;
import java.util.UUID;

/**
 * Esta clase Sensor extiende Thread: cada sensor corre en un hilo independiente.
 */
public class Sensor extends Thread {

    private final UUID idSensor = UUID.randomUUID();
    private final TipoSensor tipoSensor;
    private ConfigFile archivoConfig;
    private Integer temporizador;

    public Sensor(TipoSensor tipoSensor, Integer temporizador, String configFilePath) {

        // Unidades del temporizador en milisegundos.
        this.temporizador = temporizador;

        // TODO: ¿En qué formato recibimos el tipo de medición / sensor?
        this.tipoSensor = tipoSensor;

        // configFilePath: ruta del archivo de configuración en el sistema de archivos de la máquina.
        // Puede ser 'null'.
        this.archivoConfig = configFileEvaluateConditional(configFilePath, this.idSensor, tipoSensor.tipo);

        // Iniciando la ejecución del hilo...
        this.start();
    }

    @Override
    public void run(){
        // TODO: Desarrollar correctamente el siguiente método.
        try{
            ZMQ.Socket publisher = pub_connectZMQ("tcp://localhost:5556");
            generateAndSendReading(publisher, this.temporizador);
        }
        catch(ZMQException e){
            e.getMessage();
        }
    }

    /**
     * @param configFilePath : String de la ruta (¿absoluta o relativa?) del archivo de configuración
     *                            (archivo csv, con los valores correspondientes a las probabilidades
     *                            de que un valor esté dentro o fuera del rango aceptado; o sea erróneo).
     *                       Si el configFilePath es nula, se crea un archivo de configuración con valores aleatorios.
     * @param idSensor : ID del sensor.
     * @param tipoSensor : ID del sensor.
     * @return ConfigFile: Objeto que contiene las probabilidades mencionadas.
     */
    public ConfigFile configFileEvaluateConditional(String configFilePath, UUID idSensor, String tipoSensor){
        if(!configFilePath.isEmpty())
            return new ConfigFile(configFilePath);
        else
            return new ConfigFile(idSensor, tipoSensor);
    }

    /***
     * Método para conectar el sensor al servicio de mensajería ZMQ y posteriormente iniciar la generación
     * y comunicación de datos.
     * @param address : dirección en formato "tcp://hostname:port" a la que se va a hacer el binding.
     */
    public ZMQ.Socket pub_connectZMQ(String address) throws ZMQException {
        ZContext context = new ZContext();
        ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
        publisher.bind(address);
        return publisher;
    }

    public void generateAndSendReading(ZMQ.Socket publisher, Integer temporizador){

        Double readingData;
        Double[] generatedProbabilityFreqArray = generateProbabilityFreqArray();
        while(!Thread.currentThread().isInterrupted()){
            try{
                sleep(temporizador);
                Pair<Double, Integer> generatedPair = generateReading(generatedProbabilityFreqArray);
                readingData = generatedPair.fst;
                generatedProbabilityFreqArray[generatedPair.snd]--;
                String readingDataFormatted = String.format(
                        "%s %s %f", tipoSensor.tipo, idSensor, readingData
                );
                publisher.send(readingDataFormatted, 0);
            }
            catch(ProbabilityFreqArrayCountDownFinished finished){
                generatedProbabilityFreqArray = generateProbabilityFreqArray();
            }
            catch(InterruptedException e){
                // TODO: Find out if something else should be done in this case.
                e.getMessage();
            }
        }
    }

    private Double[] generateProbabilityFreqArray(){
        // Posición:
        // 0: Valores erróneos
        // 1: Valores fuera de rango
        // 2: Valores dentro de rango
        Double[] resultingArray = {0d,0d,0d};
        resultingArray[0] = 10d * Math.round(archivoConfig.getP_valorErroneo());
        resultingArray[1] = 10d * Math.round(archivoConfig.getP_valorFueraDeRango());
        resultingArray[2] = 10d * Math.round(archivoConfig.getP_valorDentroDeRango());
        return resultingArray;
    }

    public Pair<Double, Integer> generateReading(Double[] generatedProbabilityFreqArray) throws ProbabilityFreqArrayCountDownFinished {

        Integer indexOfProbabilityCounter;
        Random rd = new Random();
        Double generatedValue = rd.nextDouble() * Math.pow(-1, rd.nextInt(2));
        if(generatedValue < 0d && generatedProbabilityFreqArray[0] > 0){
            // El valor generado es erróneo.
            indexOfProbabilityCounter = 0;
        }
        else if((generatedValue < archivoConfig.getRangoAceptable()[0] || generatedValue > archivoConfig.getRangoAceptable()[1])
                && generatedProbabilityFreqArray[1] > 0){
            // El valor generado está fuera del rango aceptable.
            indexOfProbabilityCounter = 1;
        }
        else if((generatedValue > archivoConfig.getRangoAceptable()[0] || generatedValue < archivoConfig.getRangoAceptable()[1])
                && generatedProbabilityFreqArray[2] > 0){
            // El valor generado está dentro del rango aceptable.
            indexOfProbabilityCounter = 2;
        }
        else if (generatedProbabilityFreqArray[0] == 0 && generatedProbabilityFreqArray[1] == 0 && generatedProbabilityFreqArray[2] == 0){
            throw new ProbabilityFreqArrayCountDownFinished();
        }
        else {
            // TODO: Handle null pointer exception
            indexOfProbabilityCounter = null;
        }
        return Pair.of(generatedValue, indexOfProbabilityCounter);
    }
}
