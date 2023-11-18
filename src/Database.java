package src;
import java.sql.Connection;
// import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

    private Connection connection;

    // public void createDatabase(String fileName) {
    //     String url = "jdbc:sqlite:C:\\INF331-Tarea3\\src\\" + fileName;

    //     try (Connection conn = DriverManager.getConnection(url)) {
    //         if (conn != null) {
    //             DatabaseMetaData meta = conn.getMetaData();
    //             System.out.println("The driver name is " + meta.getDriverName());
    //             System.out.println("A new database has been created.");
    //         }

    //     } catch (SQLException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    public void connect(){
        String url = "jdbc:sqlite:C:\\INF331-Tarea3\\src\\database.db";
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Conexión establecida");
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión con la base de datos");
            throw new RuntimeException(e);
        }
    }

    public void createDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS flights (\n" +
                "    id integer PRIMARY KEY,\n" +
                "    origin text NOT NULL,\n" +
                "    destination text NOT NULL,\n" +
                "    departure text NOT NULL,\n" +
                "    arrival text NOT NULL,\n" +
                "    seats int NOT NULL\n" +
                ");";

        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos 1");
            throw new RuntimeException(e);
        }

        sql = "CREATE TABLE IF NOT EXISTS reservations (\n" +
                "    id integer PRIMARY KEY,\n" +
                "    Name text NOT NULL,\n" +
                "    seats int NOT NULL,\n" +
                "    flight_id int NOT NULL\n" +
                ");";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos 2");
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión con la base de datos");
            throw new RuntimeException(e);
        }
    }

    public int insertFlight(String origin, String destination, String departure, String arrival, int seats) {

        //verificar que el vuelo no exista
        try{
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ? AND arrival = ?";
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                System.out.println("El vuelo ya existe");
                return -1;
            }
        } catch (Exception e){
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "INSERT INTO flights(origin, destination, departure, arrival, seats) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            preparedStatement.setInt(5, seats);
            preparedStatement.executeUpdate();
            System.out.println("Vuelo insertado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar el vuelo");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public ResultSet showFlights() {
        try {
            String sql = "SELECT * FROM flights";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error al mostrar los vuelos");
            throw new RuntimeException(e);
        }
    }

    public ResultSet showFlight(String origin, String destination) {
        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error al mostrar el vuelo");
            throw new RuntimeException(e);
        }
    }

    public int updateFlight(int id, String origin, String destination, String departure, String arrival, int seats) {

        //verificar que el vuelo no exista
        try{
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ? AND arrival = ?";
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                System.out.println("El vuelo ya existe");
                return -1;
            }
        } catch (Exception e){
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        
        try {
            String sql = "UPDATE flights SET origin = ?, destination = ?, departure = ?, arrival = ?, seats = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            preparedStatement.setInt(5, seats);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
            System.out.println("Vuelo actualizado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el vuelo");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public int deleteFlight(String origin, String destination, String departure, String arrival) {
        try {
            String sql = "DELETE FROM flights WHERE origin = ? AND destination = ? AND departure = ? AND arrival = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            preparedStatement.executeUpdate();
            System.out.println("Vuelo eliminado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el vuelo");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public int deleteAllFlights() {
        try {
            String sql = "DELETE FROM flights";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println("Vuelos eliminados correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar los vuelos");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public int reserveFlight(String origin, String destination, String departure, String arrival, int seats) {
        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ? AND arrival = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, arrival);
            ResultSet resultSet = preparedStatement.executeQuery();
            //TODO: en la tabla de reservas agregar la reserva. Si no hay asientos disponibles, no agregar la reserva
            if (resultSet.next()){
                int seatsAvailable = resultSet.getInt("seats");
                if (seatsAvailable < seats){
                    System.out.println("No hay suficientes asientos disponibles");
                    return -2;
                }
            } else {
                System.out.println("El vuelo no existe");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "UPDATE flights SET seats = seats - ? WHERE origin = ? AND destination = ? AND departure = ? AND arrival = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, seats);
            preparedStatement.setString(2, origin);
            preparedStatement.setString(3, destination);
            preparedStatement.setString(4, departure);
            preparedStatement.setString(5, arrival);
            preparedStatement.executeUpdate();
            System.out.println("Reserva realizada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al realizar la reserva");
            throw new RuntimeException(e);
        }
        return 1;
    }

    // TODO: agregar metodos CRUD para la tabla de reservas

}
