package driver;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.SocketType;

import sensores.Sensor;
import sensores.TipoSensor;

import java.util.Random;

public class DriverSensores {
    public static void main(String[] args) throws Exception {
        // Sensor ejemplo
        Sensor sensor1 = new Sensor(TipoSensor.PH, 20, "usr/configFiles/configFile1.txt");

    }
}
