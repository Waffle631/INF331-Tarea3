package test;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import src.Database;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {

    private static Database database;
    private static String filePath = "src\\database.db";
    private static File file = new File(filePath);

    public static int generateNumericHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());

            // Convertir el hash en una representación numérica
            int numericHash = 0;
            for (int i = 0; i < 8; i++) {
                numericHash |= (hashBytes[i] & 0xFFL) << (8 * i);
            }

            return numericHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @BeforeEach
    public void setUp() {
        database = new Database();
        if(file.exists()){
            file.delete();
        }
        database.connect();
        database.createDatabase();
    }

    @AfterEach
    public void tearDown() {
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
        database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
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
        int a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        a = database.updateFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "6h", 150);
        assertEquals(1, a);
    }

    @Test
    public void testD_DDeleteFlight() {
        System.out.println("Test deleteFlight");
        // Verificar que se elimina un vuelo correctamente
        database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        int a = database.deleteFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00");
        assertEquals(1, a);
    }

    @Test
    public void testE_EUpdateFlightMinusOne() {
        System.out.println("Test updateFlight not found -1");
        // int a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        int a = database.updateFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "6h", 150);
        assertEquals(-1, a);
    }

    @Test
    public void testF_FUpdateFlightMinusThree() {
        System.out.println("Test updateFlight more than one -3");
        int a = database.insertFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        a = database.insertFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "7h", 130);
        a = database.updateFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00", "5h", 150);
        assertEquals(-3, a);
    }

    @Test
    public void testG_GDeleteFlightMinusOne() {
        System.out.println("Test deleteFlight not found -1");
        int a = database.deleteFlight("Aeropuerto3", "Aeropuerto2", "2020-01-01-10:00");
        assertEquals(-1, a);
    }

    @Test
    public void testH_HReserveFlight(){
        System.out.println("Test reserveFlight");
        database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        String input = "Juan" + String.valueOf(3);
        int conformation_number = generateNumericHash(input);
        int a = database.reserveFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", 3, "Juan", conformation_number);
        assertEquals(1, a);
    }

    @Test
    public void testI_IReserveFlightMinusOne(){
        System.out.println("Test reserveFlight not found -1");
        int a = database.reserveFlight("Aeropuerto43", "Aeropuerto2", "2020-01-01-10:00", 3, "Pepe", 123456789);
        assertEquals(-1, a);
    }

    @Test
    public void test_J_ReserveFlightMinusTwo(){
        System.out.println("Test reserveFlight not enough seats -2");
        database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 100);
        String input = "Pedro" + String.valueOf(101);
        int conformation_number = generateNumericHash(input);
        int a = database.reserveFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", 101, "Pedro", conformation_number);
        assertEquals(-2, a);
    }

    @Test
    public void test_K_InsertFlightMinusTwo(){
        System.out.println("Test insertFlight flight already exists -1");
        int a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 101);
        a = database.insertFlight("Aeropuerto1", "Aeropuerto2", "2020-01-01-10:00", "5h", 101);
        assertEquals(-1, a);
    }


}