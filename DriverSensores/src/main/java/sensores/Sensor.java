package sensores;

import com.sun.tools.javac.util.Pair;
import driver.DriverSensores;
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

        this.tipoSensor = tipoSensor;

        // configFilePath: ruta del archivo de configuración en el sistema de archivos de la máquina.
        // Puede ser 'null'.
        this.archivoConfig = configFileEvaluateConditional(configFilePath, this.idSensor, tipoSensor.tipo);

        // Iniciando la ejecución del hilo...
        this.start();
    }

    @Override
    public void run(){
        try{
            // El número de puerto a utilizar es estático y constante (final)
            ZMQ.Socket publisher = pub_connectZMQ("tcp://localhost:" + DriverSensores.port);
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
        if(!configFilePath.isEmpty()){
            return new ConfigFile(configFilePath);
        }
        else {
            return new ConfigFile(idSensor, tipoSensor);
        }
    }

    /***
     * Método para conectar el sensor al servicio de mensajería ZMQ y posteriormente iniciar la generación
     * y comunicación de datos.
     * @param address : dirección en formato "tcp://hostname:port" a la que se va a hacer el binding.
     */
    public ZMQ.Socket pub_connectZMQ(String address) throws ZMQException {
        ZContext context = new ZContext();
        ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
        publisher.connect(address);
        return publisher;
    }

    public void generateAndSendReading(ZMQ.Socket publisher, Integer temporizador){

        Double readingData;
        Integer[] generatedProbabilityFreqArray = generateProbabilityFreqArray();

        while(!Thread.currentThread().isInterrupted()){
            try{
                sleep(temporizador);
                Pair<Double, Integer> generatedPair = null;
                Integer typeOfReadingIndex = null;
                try{
                    do{
                        generatedPair = generateReading();
                    } while(generatedProbabilityFreqArray[generatedPair.snd] <= 0);
                    typeOfReadingIndex = generatedPair.snd;
                    generatedProbabilityFreqArray[typeOfReadingIndex]--;
                    if(generatedProbabilityFreqArray[0] <= 0 && generatedProbabilityFreqArray[1] <= 0 && generatedProbabilityFreqArray[2] <= 0){
                        throw new ProbabilityFreqArrayCountDownFinished();
                    }
                }
                catch(ProbabilityFreqArrayCountDownFinished finished){
                    System.out.println("Countdown finished. Generating a new array...");
                    generatedProbabilityFreqArray = generateProbabilityFreqArray();
                }

                readingData = generatedPair.fst;

                //TODO: Comment the following DEBUG lines
                System.out.println(readingData);
                System.out.println(typeOfReadingIndex);

                String readingDataFormatted = String.format(
                        "%s %s %f", tipoSensor.tipo, idSensor, readingData
                );
                publisher.send(readingDataFormatted, 0);
            }
            catch(InterruptedException e){
                // TODO: Find out if something else should be done in this case.
                e.getMessage();
            }
        }
    }

    private Integer[] generateProbabilityFreqArray(){
        // Posición:
        // 0: Valores erróneos
        // 1: Valores fuera de rango
        // 2: Valores dentro de rango
        Integer[] resultingArray = {0,0,0};
        resultingArray[0] = Math.toIntExact(Math.round(10 * this.archivoConfig.getP_valorErroneo()));
        resultingArray[1] = Math.toIntExact(Math.round(10 * this.archivoConfig.getP_valorFueraDeRango()));
        resultingArray[2] = Math.toIntExact(Math.round(10 * this.archivoConfig.getP_valorDentroDeRango()));

        //TODO: Comment the following DEBUG lines.
        System.out.println("Erróneos " + resultingArray[0]);
        System.out.println("Fuera " + resultingArray[1]);
        System.out.println("Dentro " + resultingArray[2]);
        System.out.println('\n');

        return resultingArray;
    }

    public Pair<Double, Integer> generateReading() {

        Integer indexOfProbabilityCounter = null;
        Random rd = new Random();
        Double generatedValue;
        generatedValue = rd.nextDouble() * Math.pow(-1, rd.nextInt(2));

        if(generatedValue < 0){
            // El valor generado es erróneo.
            indexOfProbabilityCounter = 0;
        }
        else if(generatedValue < archivoConfig.getRangoAceptable()[0] || generatedValue > archivoConfig.getRangoAceptable()[1]){
            // El valor generado está fuera del rango aceptable.
            indexOfProbabilityCounter = 1;
        }
        else if(generatedValue >= archivoConfig.getRangoAceptable()[0] || generatedValue <= archivoConfig.getRangoAceptable()[1]){
            // El valor generado está dentro del rango aceptable.
            indexOfProbabilityCounter = 2;
        }

        return Pair.of(generatedValue, indexOfProbabilityCounter);
    }
}
