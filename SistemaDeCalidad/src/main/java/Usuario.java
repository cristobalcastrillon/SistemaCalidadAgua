import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;

public class Usuario {

    // idUsuario: credencial que junto a la contraseña sirve para autorización de acceso al servicio.
    private String idUsuario;

    // passwordHash: hash generado a partir de la contraseña en texto plano + el randomSalt.
    // Se hace uso del algoritmo SHA2-256.
    private String passwordHash;

    // Constructor for signing up a new User.
    Usuario(String id, String passwordHash){
        this.idUsuario = id;
        this.passwordHash = passwordHash;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
