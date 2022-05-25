import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaDeCalidad {

    public static class UserDataFile{

        public static final String pathName = System.getProperty("user.dir") + "/userDataFile.csv";

        public static final Integer USR_ID_POS = 0;
        public static final Integer USR_SALT_POS = 1;
        public static final Integer PASS_HASH_FILE_POS = 2;
    }

    public static void main(String[] args){

        boolean signedIn = false;

        Scanner scanner = new Scanner(System.in);

        while(!signedIn){

            System.out.println("¿Qué desea hacer?");
            System.out.println("1. Ingresar al sistema.");
            System.out.println("2. Registrarse en el sistema.");

            String eleccion = scanner.nextLine();

            if(eleccion.equals("1")){
                System.out.println("Por favor ingrese su id:");
                String id = scanner.nextLine();

                System.out.println("Por favor ingrese su contraseña:");
                String password = scanner.nextLine();

                signedIn = userSignIn(id, password);
                if(!signedIn){
                    System.out.println("No se ha logrado ingresar al sistema con las credenciales suministradas.");
                }
            }
            else if(eleccion.equals("2")){
                System.out.println("Por favor ingrese un id:");
                String id = scanner.nextLine();

                System.out.println("Por favor ingrese una contraseña:");
                String password = scanner.nextLine();

                userSignUp(id, password);
                signedIn = userSignIn(id, password);
            }
            else{
                System.out.println("La opción seleccionada no es válida.");
            }
        }

        // TODO: Receive data in loop

    }

    public static void userSignUp(String id, String plainTextPassword){

        // salt: cadena de 8 caracteres generados aleatoriamente.
        // Pretende ser única para cada usuario y se guarda en la base de datos (archivo csv en este caso) en «texto plano».
        byte[] salt = new byte[64];
        SecureRandom srd = new SecureRandom();
        srd.nextBytes(salt);

        String saltString = salt.toString();
        String generatedPasswordHash = DigestUtils.sha256Hex(plainTextPassword + saltString);

        try{
            logNewUserInFile(id, saltString, generatedPasswordHash);
        }
        catch(IOException e){
            e.getMessage();
        }

        //return new Usuario(id, generatedPasswordHash);
    }

    private static void logNewUserInFile(String id, String saltString, String generatedPasswordHash) throws IOException {
        // TODO(Not implemented yet)

        File userDataFile = new File(UserDataFile.pathName);

        if(userDataFile.createNewFile()){
            FileWriter csvWriter = new FileWriter(UserDataFile.pathName);

            ArrayList<String> headerFields = new ArrayList<>();
            headerFields.add("id");
            headerFields.add("salt");
            headerFields.add("password_hash");

            ArrayList<String> values = new ArrayList<>();
            values.add(id);
            values.add(saltString);
            values.add(generatedPasswordHash);

            csvWriter.append(stringLineConcatHelper(headerFields));
            csvWriter.append(stringLineConcatHelper(values));

            csvWriter.flush();
            csvWriter.close();
        }

    }

    public static boolean userSignIn(String id, String plainTextPassword){

        try{
            String[] data = readFromFileByUserID(id);
            String passwordAndSaltString = plainTextPassword + data[UserDataFile.USR_SALT_POS];
            String passwordAndSaltHash = DigestUtils.sha256Hex(passwordAndSaltString);

            if(data[UserDataFile.PASS_HASH_FILE_POS].equals(passwordAndSaltHash)){
                return true;
            }
        }
        catch(Exception e){
            e.getMessage();
        }

        return false;
    }

    private static String[] readFromFileByUserID(String id) throws IOException {
        // TODO(Not implemented yet)
        String[] data = null;
        return data;
    }

    private static String stringLineConcatHelper(List<String> toBeFormatted){
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
