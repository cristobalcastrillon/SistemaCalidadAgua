public enum TipoMedicion {
    PH("pH"),
    OXIGENO("Oxigeno"),
    TEMPERATURA("Temperatura");

    public final String tipo;

    private TipoMedicion(String tipo){
        this.tipo = tipo;
    }
}
