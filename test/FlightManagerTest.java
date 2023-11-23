package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import src.FlightManager;
import src.Database;

public class FlightManagerTest {
    private FlightManager flightManager;
    private Database database;
    private static String filePath = "src\\database.db";
    private static File file = new File(filePath);

    @BeforeEach
    public void setUp() {
        database = new Database();
        if(file.exists()){
            file.delete();
        }
        database.connect();
        database.createDatabase();
        flightManager = new FlightManager(database);
    }

    @AfterEach
    public void tearDown() {
        database.closeConnection();
    }

    @Test
    public void testCrearVuelo_ValidInput_InsertFlightCalled() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "31/12/2022-23:59";
        String duration = "2h";
        int seats = 100;

        int a = flightManager.CrearVuelo(origin, destination, departure, duration, seats);

        assertEquals(1, a);
    }

    @Test
    public void testCrearVuelo_InvalidDepartureFormat() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "2022-12-31";
        String duration = "2h30m";
        int seats = 100;

        int a = flightManager.CrearVuelo(origin, destination, departure, duration, seats);
        assertEquals(a, -2);

    }

    @Test
    public void testCrearVuelo_InvalidDepartureFormat2() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "95/15/5231-25:69";
        String duration = "2h";
        int seats = 100;

        int a = flightManager.CrearVuelo(origin, destination, departure, duration, seats);
        assertEquals(-2, a);

    }

    @Test
    public void testCrearVuelo_InvalidDurationFormat() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "31/10/2002-23:59";
        String duration = "2h30m";
        int seats = 100;

        int a = flightManager.CrearVuelo(origin, destination, departure, duration, seats);
        assertEquals(-1, a);

    }

    @Test
    public void testBuscarVuelo_ValidInput_ShowFlightCalled() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "31/12/2022-23:59";
        String duration = "2h";
        int seats = 100;

        flightManager.CrearVuelo(origin, destination, departure, duration, seats);
        int a = flightManager.BuscarVuelo(origin, destination, departure);
        assertEquals(1, a);
    }

    @Test
    public void testBuscarVuelo_InvalidDepartureFormat() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "2022-12-31";

        int a = flightManager.BuscarVuelo(origin, destination, departure);
        assertEquals(-2, a);
    }

    @Test
    public void testEditarVuelo_ValidInput_UpdateFlightCalled() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "31/12/2022-23:59";
        String duration = "2h";
        int seats = 100;

        flightManager.CrearVuelo(origin, destination, departure, duration, seats);

        duration = "5h";
        seats = 150;
        flightManager.EditarVuelo(origin, destination, departure, duration, seats);
    }

    @Test
    public void testEditarVuelo_InvalidDepartureFormat() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "2022/12/31";
        String duration = "2h30m";
        int seats = 100;

        int a = flightManager.EditarVuelo(origin, destination, departure, duration, seats);
        assertEquals(-2, a);
    }

    @Test
    public void testEditarVuelo_InvalidDurationFormat() {
        String origin = "Origin";
        String destination = "Destination";
        String departure = "15/12/2001-23:59";
        String duration = "2h30m";
        int seats = 100;

        int a = flightManager.EditarVuelo(origin, destination, departure, duration, seats);
        assertEquals(-1, a);
    }
}
