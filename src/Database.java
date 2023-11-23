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

    public void connect() {
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
                "    duration text NOT NULL,\n" +
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

    public int insertFlight(String origin, String destination, String departure, String duration, int seats) {

        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ? AND duration = ?";
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, duration);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("El vuelo ya existe");
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "INSERT INTO flights(origin, destination, departure, duration, seats) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            preparedStatement.setString(4, duration);
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

    public ResultSet showFlight(String origin, String destination, String departure) {
        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("El vuelo no existe");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error al mostrar el vuelo");
            throw new RuntimeException(e);
        }
    }

    public int updateFlight(String origin, String destination, String departure, String duration, int seats) {
        // System.out.println(
        //         "Vuelo a actualizar: " + origin + " " + destination + " " + departure + " " + duration + " " + seats);
        try {
            String selectQuery = "SELECT count(*) FROM flights WHERE origin = ? AND destination = ? AND departure = ?";
            // System.out.println("despues string");
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, origin);
            selectStatement.setString(2, destination);
            selectStatement.setString(3, departure);
            ResultSet resultSet = selectStatement.executeQuery();
            // System.out.println("query ejecutado");
            // chequear si hay más de un resultado
            int count = resultSet.getInt(1);
            if (count > 1) {
                System.out.println("Hay más de un vuelo con los mismos datos");
                return -3;
            } else if (count == 0) {
                System.out.println("No se encontró el vuelo");
                return -1;
            }

            String updateQuery = "UPDATE flights SET duration = ?, seats = ? WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, duration);
            updateStatement.setInt(2, seats);
            updateStatement.setString(3, origin);
            updateStatement.setString(4, destination);
            updateStatement.setString(5, departure);
            int rowsAffected = updateStatement.executeUpdate();
            // System.out.println(rowsAffected);

            if (rowsAffected > 0) {
                System.out.println("Vuelo actualizado correctamente");
                return rowsAffected;
            } else {
                System.out.println("No se pudo actualizar el vuelo");
                return -2;
            }
        } catch (SQLException e) {
            System.out.println("Error updating flight record");
            throw new RuntimeException(e);
        }
    }

    public int deleteFlight(String origin, String destination, String departure) {
        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("El vuelo no existe");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "DELETE FROM flights WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
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

    public int reserveFlight(String origin, String destination, String departure, int seats, String name) {
        int flightId;
        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, origin);
            preparedStatement.setString(2, destination);
            preparedStatement.setString(3, departure);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int seatsAvailable = resultSet.getInt("seats");
                flightId = resultSet.getInt("id");
                if (seatsAvailable < seats) {
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
            String sql = "UPDATE flights SET seats = seats - ? WHERE origin = ? AND destination = ? AND departure = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, seats);
            preparedStatement.setString(2, origin);
            preparedStatement.setString(3, destination);
            preparedStatement.setString(4, departure);
            preparedStatement.executeUpdate();
            insertReservation(name, seats, flightId);
            System.out.println("Reserva realizada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al realizar la reserva");
            throw new RuntimeException(e);
        }
        return 1;
    }



    public int insertReservation(String name, int seats, int flightId) {
        try {
            String sql = "INSERT INTO reservations(Name, seats, flight_id) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, seats);
            preparedStatement.setInt(3, flightId);
            preparedStatement.executeUpdate();
            System.out.println("Reserva insertada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar la reserva");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public ResultSet showReservations() {
        try {
            String sql = "SELECT * FROM reservations";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error al mostrar las reservas");
            throw new RuntimeException(e);
        }
    }

    public ResultSet showReservation(String name, int flightId) {
        try {
            String sql = "SELECT * FROM flights WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("El vuelo no existe");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si el vuelo existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "SELECT * FROM reservations WHERE Name = ? AND flight_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error al mostrar la reserva");
            throw new RuntimeException(e);
        }
    }

    public int deleteReservation(String name, int flightId) {
        try {
            String sql = "SELECT * FROM reservations WHERE Name = ? AND flight_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("La reserva no existe");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si la reserva existe");
            throw new RuntimeException(e);
        }

        try {
            String sql = "DELETE FROM reservations WHERE Name = ? AND flight_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, flightId);
            preparedStatement.executeUpdate();
            System.out.println("Reserva eliminada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar la reserva");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public int deleteAllReservations() {
        try {
            String sql = "DELETE FROM reservations";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            System.out.println("Reservas eliminadas correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar las reservas");
            throw new RuntimeException(e);
        }
        return 1;
    }

    public int deleteAll() {
        deleteAllReservations();
        deleteAllFlights();
        return 1;
    }

}
