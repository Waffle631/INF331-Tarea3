package test;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.sql.ResultSet;

import src.Database;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {

    private static Database database;
    private static String filePath = "src\\database.db";
    private static File file = new File(filePath);

    @BeforeAll
    public static void setUp() {
        database = new Database();
        if(file.exists()){
            file.delete();
        }
        database.connect();
        database.createDatabase();
    }

    @AfterAll
    public static void tearDown() {
        database.closeConnection();
    }

    @Test
    public void testA_AInsertFlight() {
        System.out.println("Test insertFlight");
        // Verificar que se inserta un vuelo correctamente
        int a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        assertEquals(1, a);
    }

    @Test
    public void testB_BShowFlight(){
        System.out.println("Test showFlight");
        ResultSet a = database.showFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00");
        try{
            while(a.next()){
                assertEquals("Aeropuerto1", a.getString("origin"));
                assertEquals("Aeropuerto2", a.getString("destination"));
                assertEquals("2020-01-01-10:00", a.getString("departure"));
                assertEquals("5h", a.getString("duration"));
                assertEquals(100, a.getInt("seats"));
            }
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    public void testC_CUpdateFlight() {
        System.out.println("Test updateFlight");
        // Verificar que se modifica un vuelo correctamente
        // int a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        int a = database.updateFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "6h", 150);
        assertEquals(1, a);
    }

    @Test
    public void testD_DDeleteFlight() {
        System.out.println("Test deleteFlight");
        // Verificar que se elimina un vuelo correctamente
        int a = database.deleteFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00");
        assertEquals(1, a);
    }

    @Test
    public void testE_EUpdateFlightMinusOne() {
        System.out.println("Test updateFlight not found -1");
        // Verificar que se modifica un vuelo correctamente
        // int a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        int a = database.updateFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "6h", 150);
        assertEquals(-1, a);
    }

    @Test
    public void testF_FUpdateFlightMinusThree() {
        System.out.println("Test updateFlight more than one -3");
        // Verificar que se modifica un vuelo correctamente
        int a = database.insertFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        a = database.insertFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "7h", 130);
        a = database.updateFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "5h", 150);
        assertEquals(-3, a);
    }


}