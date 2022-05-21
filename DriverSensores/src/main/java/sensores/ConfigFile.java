package sensores;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ConfigFile {

    // Ruta del archivo en el sistema.
    private String ruta_archivo;

    private Double p_valorDentroDeRango;
    private Double p_valorFueraDeRango;
    private Double p_valorErroneo;

    // Array que contiene en
    // [0]: límite inferior del rango.
    // [1]: límite superior del rango.
    private Double[] rangoAceptable;

    public ConfigFile(String ruta_archivo){
        setRuta_archivo(ruta_archivo);
        extractConfigFromFile(ruta_archivo);
    }

    public ConfigFile(UUID idSensor, String tipoSensor){
        generateConfigFile(idSensor.toString(), tipoSensor);
    }

    // Método para extraer los valores de las probabilidades desde un archivo alojado con la referencia que se ha pasado.
    private void extractConfigFromFile(String ruta){
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(ruta));

            String row;
            String[] data = null;
            while ((row = csvReader.readLine()) != null) {
                data = row.split(",");
            }

            // OJO: Se presupone que el formato siempre es idéntico
            // (línea 1: header; línea 2: dentro, fuera, erróneo, límite inferior del rango, límite superior del rango)
            setP_valorDentroDeRango(Double.parseDouble(data[0]));
            setP_valorFueraDeRango(Double.parseDouble(data[1]));
            setP_valorErroneo(Double.parseDouble(data[2]));
            setRangoAceptable(new Double[]{Double.parseDouble(data[3]), Double.parseDouble(data[4])});

            csvReader.close();
        }
        catch(IOException e){
            e.toString();
        }
    }

    // Método para generar archivos de configuración cuando no se pasa referencia a alguno de ellos
    // (configFilePath.isEmpty() == true).
    private void generateConfigFile(String idSensor, String tipoSensor){

        Random rd = new Random();

        // Asegurando que la sumatoria de las probabilidades sea igual a 1.
        setP_valorDentroDeRango(rd.nextDouble());
        do{
            setP_valorFueraDeRango(rd.nextDouble());
        } while(( p_valorDentroDeRango + p_valorFueraDeRango) > 1d);
        setP_valorErroneo(1d - p_valorFueraDeRango - p_valorDentroDeRango);
        setRangoAceptable(generateAdmissibleRange());

        try{
            setRuta_archivo(writeConfigFile(getP_valorDentroDeRango(), getP_valorFueraDeRango(), getP_valorErroneo(), getRangoAceptable(), idSensor, tipoSensor));
        }
        catch(IOException e){
            e.toString();
        }
    }

    private Double[] generateAdmissibleRange() {
        Double[] admissibleRange = {0d, 0d};
        Random rd = new Random();
        do{
            admissibleRange[0] = rd.nextDouble();
            admissibleRange[1] = rd.nextDouble();
        }while(admissibleRange[0] >= admissibleRange[1]);
        return admissibleRange;
    }

    // Método para escribir los valores de probabilidad generados de manera aleatoria en un archivo.
    // Este método retorna una ruta para el archivo generado.
    private String writeConfigFile(Double dentro, Double fuera, Double erroneo, Double[] rangoAceptable, String idSensor, String tipoSensor) throws IOException {

        ArrayList<String> headerFields = new ArrayList<>();
        headerFields.add("p_valorDentroDeRango");
        headerFields.add("p_valorFueraDeRango");
        headerFields.add("p_valorErroneo");
        headerFields.add("limite_inferior_rango");
        headerFields.add("limite_superior_rango");

        ArrayList<String> values = new ArrayList<>();
        values.add(dentro.toString());
        values.add(fuera.toString());
        values.add(erroneo.toString());
        values.add(rangoAceptable[0].toString());
        values.add(rangoAceptable[1].toString());

        // Formato de nombramiento para los archivos de configuración:
        // "tipoSensor_idSensor_configFile.csv"
        String configFilePath = System.getProperty("user.dir") + File.separator +
                tipoSensor + "_" + idSensor + "_" + "configFile.csv";
        FileWriter csvWriter = new FileWriter(configFilePath);
        csvWriter.append(stringLineConcatHelper(headerFields));
        csvWriter.append(stringLineConcatHelper(values));

        csvWriter.flush();
        csvWriter.close();

        return configFilePath;
    }

    private String stringLineConcatHelper(List<String> toBeFormatted){
        String toBeAppended = new String();
        int index = 0;
        for(String token : toBeFormatted){
            toBeAppended += token;
            if(index == toBeFormatted.size() - 1)
                break;
            toBeAppended += ',';
            index++;
        }
        toBeAppended += '\n';
        return toBeAppended;
    }

    public String getRuta_archivo() {
        return ruta_archivo;
    }

    public Double getP_valorDentroDeRango() {
        return p_valorDentroDeRango;
    }

    public Double getP_valorFueraDeRango() {
        return p_valorFueraDeRango;
    }

    public Double getP_valorErroneo() {
        return p_valorErroneo;
    }

    public Double[] getRangoAceptable() {
        return rangoAceptable;
    }

    public void setRuta_archivo(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }

    public void setP_valorDentroDeRango(Double p_valorDentroDeRango) {
        this.p_valorDentroDeRango = p_valorDentroDeRango;
    }

    public void setP_valorFueraDeRango(Double p_valorFueraDeRango) {
        this.p_valorFueraDeRango = p_valorFueraDeRango;
    }

    public void setP_valorErroneo(Double p_valorErroneo) {
        this.p_valorErroneo = p_valorErroneo;
    }

    public void setRangoAceptable(Double[] rangoAceptable) {
        this.rangoAceptable = rangoAceptable;
    }
}
