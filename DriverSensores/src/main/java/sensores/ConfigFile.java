package sensores;

public class ConfigFile {
    public final Double p_valorDentroDeRango;
    public final Double p_valorFueraDeRango;
    public final Double p_valorErroneo;

    public ConfigFile(Double p_valorDentroDeRango, Double p_valorFueraDeRango, Double p_valorErroneo){
        this.p_valorDentroDeRango = p_valorDentroDeRango;
        this.p_valorFueraDeRango = p_valorFueraDeRango;
        this.p_valorErroneo = p_valorErroneo;
    }
}
