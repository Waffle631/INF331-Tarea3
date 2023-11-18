package src;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Connection connection;

    public void createDatabase(String fileName) {
        String url = "jdbc:sqlite:C:\\INF331-Tarea3\\src\\" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void connect(){
        String url = "jdbc:sqlite:C:\\INF331-Tarea3\\src\\database.db";
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Conexión establecida");
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión con la base de datos");
            System.out.println(e.getMessage());
        }
    }

    public void createDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS flights (\n" +
                "    id integer PRIMARY KEY,\n" +
                "    origin text NOT NULL,\n" +
                "    destination text NOT NULL,\n" +
                "    departure text NOT NULL,\n" +
                "    arrival text NOT NULL,\n" +
                "    price real NOT NULL\n" +
                ");";

        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos");
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la base de datos");
            System.out.println(e.getMessage());
        }
    }

}
