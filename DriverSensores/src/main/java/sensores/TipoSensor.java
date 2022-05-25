package sensores;

public enum TipoSensor {
    PH("pH"),
    OXIGENO("Oxigeno"),
    TEMPERATURA("Temperatura");

    public final String tipo;

    TipoSensor(String tipo){
        this.tipo = tipo;
    }

    public static TipoSensor retrieveTypeByString(String tipo){
        switch (tipo){
            case "pH":
                return TipoSensor.PH;
            case "Oxigeno":
                return TipoSensor.OXIGENO;
            case "Temperatura":
                return TipoSensor.TEMPERATURA;
            default:
                return null;
        }
    }

    public static String retrieveStringByType(TipoSensor tipo){
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
