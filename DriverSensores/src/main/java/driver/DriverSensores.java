package driver;

import sensores.Sensor;
import sensores.TipoSensor;

public class DriverSensores {

    public static final String port = "5556";

    public static void main(String[] args) throws Exception {
        Sensor sensor1 = new Sensor(2000, System.getProperty("user.dir") + "/pH_895e81e9-0cbe-4e96-aa84-bf86a8ee07f1_configFile.csv");
        Sensor sensor2 = new Sensor(1000, "");
        Sensor sensor3 = new Sensor(1500, "");
        Sensor sensor4 = new Sensor(3000, "");
        Sensor sensor5 = new Sensor(1000, "");
        Sensor sensor6 = new Sensor(1000, "");
    }
}
