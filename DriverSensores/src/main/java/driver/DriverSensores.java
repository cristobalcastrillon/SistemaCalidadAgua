package driver;

import sensores.Sensor;
import sensores.TipoSensor;

public class DriverSensores {

    public static final String port = "5556";

    public static void main(String[] args) throws Exception {
        //Sensor sensor1 = new Sensor(2000, "/Users/cristobalcastrilonbalcazar/Dev/SistemaCalidadAgua/DriverSensores/pH_895e81e9-0cbe-4e96-aa84-bf86a8ee07f1_configFile.csv");
        Sensor sensor2 = new Sensor(1000, "");
    }
}
