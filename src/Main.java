package src;
import java.io.File;
import java.sql.ResultSet;
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
        ResultSet rs = database.showFlights();
        try{
            while(rs.next()){
                System.out.println(rs);
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        // FlightManager flightManager = new FlightManager();
        // flightManager.menu();
        database.closeConnection();
    }
}
