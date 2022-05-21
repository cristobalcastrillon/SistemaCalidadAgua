package driver;

import sensores.Sensor;
import sensores.TipoSensor;

public class DriverSensores {

    public static Integer port = 5555;

    public static void main(String[] args) throws Exception {
        // TODO: Averiguar cómo se debe hacer el manejo de hilos (cada sensor debe correr en paralelo a los demás).
        Sensor sensor1 = createSensor(TipoSensor.PH, 2000, "/Users/cristobalcastrilonbalcazar/Dev/SistemaCalidadAgua/DriverSensores/pH_895e81e9-0cbe-4e96-aa84-bf86a8ee07f1_configFile.csv");
        Sensor sensor2 = createSensor(TipoSensor.PH, 1000, "");
    }

    public static Sensor createSensor(TipoSensor tipoSensor, Integer temporizador, String configFilePath){
        // Los números de puerto a utilizar comienzan a partir de 5556.
        port++;
        return new Sensor(tipoSensor, temporizador, configFilePath);
    }
}
