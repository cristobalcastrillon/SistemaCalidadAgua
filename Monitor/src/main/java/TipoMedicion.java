public enum TipoMedicion {
    PH("pH"),
    OXIGENO("Oxigeno"),
    TEMPERATURA("Temperatura");

    public final String tipo;

    TipoMedicion(String tipo){
        this.tipo = tipo;
    }

    public static TipoMedicion retrieveTypeByString(String tipo){
        switch (tipo){
            case "pH":
                return TipoMedicion.PH;
            case "Oxigeno":
                return TipoMedicion.OXIGENO;
            case "Temperatura":
                return TipoMedicion.TEMPERATURA;
            default:
                return null;
        }
    }

    public static String retrieveStringByType(TipoMedicion tipo){
        switch (tipo){
            case PH:
                return "pH";
            case TEMPERATURA:
                return "Temperatura";
            case OXIGENO:
                return "Oxigeno";
            default:
                return null;
        }
    }
}
