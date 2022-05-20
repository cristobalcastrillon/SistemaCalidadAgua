package driver;

import sensores.Sensor;
import sensores.TipoSensor;

public class DriverSensores {
    public static void main(String[] args) throws Exception {
        // TODO: Averiguar cómo se debe hacer el manejo de hilos (cada sensor debe correr en paralelo a los demás).
        Sensor sensor1 = new Sensor(TipoSensor.PH, 20, "usr/configFiles/configFile1.txt");
    }
}
