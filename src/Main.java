package src;
import java.io.File;
public class Main {
    public static void main(String[] args) {
        String filePath = "src\\database.db"; // Reemplaza con la ruta y nombre de tu archivo
        Database database = new Database();
        File file = new File(filePath);
        if(!file.exists()){
            // database.createDatabase("database.db");
            database.connect();
            database.createDatabase();
        } else {
            database.connect();
        }
        FlightManager flightManager = new FlightManager();
        flightManager.menu();
        database.closeConnection();
    }
}
