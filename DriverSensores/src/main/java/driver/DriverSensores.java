package driver;

import sensores.Sensor;
import sensores.TipoSensor;

public class DriverSensores {
    public static void main(String[] args) throws Exception {
        // TODO: Averiguar cómo se debe hacer el manejo de hilos (cada sensor debe correr en paralelo a los demás).
        Sensor sensor1 = new Sensor(TipoSensor.PH, 2000, "/Users/cristobalcastrilonbalcazar/Dev/SistemaCalidadAgua/DriverSensores/pH_895e81e9-0cbe-4e96-aa84-bf86a8ee07f1_configFile.csv");
        Sensor sensor2 = new Sensor(TipoSensor.PH, 1000, "");
    }
}
