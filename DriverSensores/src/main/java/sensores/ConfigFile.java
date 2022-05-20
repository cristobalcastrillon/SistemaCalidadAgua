package sensores;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfigFile {

    // Ruta del archivo en el sistema.
    private String ruta_archivo;

    private Double p_valorDentroDeRango;
    private Double p_valorFueraDeRango;
    private Double p_valorErroneo;

    public ConfigFile(String ruta_archivo){
        this.ruta_archivo = ruta_archivo;
        extractConfigFromFile(ruta_archivo);
    }

    public ConfigFile(){
        generateConfigFile();
    }

    // Método para extraer los valores de las probabilidades desde un archivo alojado con la referencia que se ha pasado.
    private void extractConfigFromFile(String ruta){
        try {

            // TODO: Read values from configFile

            this.p_valorDentroDeRango = p_valorDentroDeRango;
            this.p_valorFueraDeRango = p_valorFueraDeRango;
            this.p_valorErroneo = p_valorErroneo;
        }
        catch(IOException e){
            e.toString();
        }
    }

    // Método para generar archivos de configuración cuando no se pasa referencia a alguno de ellos
    // (configFilePath.isEmpty() == true).
    private void generateConfigFile(){

        Random rd = new Random();

        this.p_valorDentroDeRango = rd.nextDouble();
        this.p_valorFueraDeRango = rd.nextDouble();
        this.p_valorErroneo = rd.nextDouble();

        try{
            this.ruta_archivo = writeConfigFile(this.p_valorDentroDeRango, this.p_valorFueraDeRango, this.p_valorErroneo);
        }
        catch(IOException e){
            e.toString();
        }
    }

    // Método para escribir los valores de probabilidad generados de manera aleatoria en un archivo.
    // Este método retorna una ruta para el archivo generado.
    private String writeConfigFile(Double dentro, Double fuera, Double erroneo) throws IOException {

        ArrayList<String> headerFields = new ArrayList<>();
        headerFields.add("p_valorDentroDeRango");
        headerFields.add("p_valorFueraDeRango");
        headerFields.add("p_valorErroneo");

        ArrayList<String> values = new ArrayList<>();
        values.add(dentro.toString());
        values.add(fuera.toString());
        values.add(erroneo.toString());

        String configFilePath = System.getProperty("usr.dir") + "/configFile.csv";
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
}
