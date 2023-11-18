package test;
import org.junit.Test;

import src.FlightManager;

import static org.junit.Assert.*;

public class FlightManagerTest {
    @Test
    public void testValidateOption(){
        FlightManager flightManager = new FlightManager();
        assertEquals(true, flightManager.validateOption(1));
        assertEquals(true, flightManager.validateOption(2));
        assertEquals(true, flightManager.validateOption(3));
        assertEquals(true, flightManager.validateOption(4));
        assertEquals(false, flightManager.validateOption(0));
        assertEquals(false, flightManager.validateOption(5));
    }
}
