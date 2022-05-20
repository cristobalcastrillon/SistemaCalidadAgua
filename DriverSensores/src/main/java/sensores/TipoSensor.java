package sensores;

public enum TipoSensor {
    PH("pH"),
    OXIGENO("Oxigeno"),
    TEMPERATURA("Temperatura");

    public final String tipo;

    private TipoSensor(String tipo){
        this.tipo = tipo;
    }
}
