package src;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FlightManager {

    Scanner input = new Scanner(System.in);
    String departurePattern = "\\d{2}/\\d{2}/\\d{4}-\\d{2}:\\d{2}";
    String durationPattern = "\\d{1}[h]";
    Database database;

    public FlightManager(Database db) {
        database = db;
    }

    public boolean verificarFecha(String[] dates) {
        if (dates.length != 3) {
            return false; // El arreglo debe tener exactamente 3 elementos
        }

        int day, month, year;
        try {
            day = Integer.parseInt(dates[0]);
            month = Integer.parseInt(dates[1]);
            year = Integer.parseInt(dates[2]);
        } catch (NumberFormatException e) {
            return false; // Los elementos del arreglo deben ser números enteros
        }

        // Verificar si el día, mes y año son válidos
        if (year < 0 || month < 1 || month > 12 || day < 1 || day > getDaysInMonth(month, year)) {
            return false;
        }

        return true;
    }

    private int getDaysInMonth(int month, int year) {
        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // Verificar si es un año bisiesto
        if (month == 2 && (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)) {
            return 29;
        }

        return daysInMonth[month - 1];
    }

    public boolean verificarHora(String[] time) {
        if (time.length != 2) {
            return false; // El arreglo debe tener exactamente 2 elementos
        }

        int hour, minute;
        try {
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);
        } catch (NumberFormatException e) {
            return false; // Los elementos del arreglo deben ser números enteros
        }

        // Verificar si la hora y los minutos son válidos
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            return false;
        }

        return true;
    }

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

    public static void PrintResult(ResultSet result, int type){
        switch (type) {
            case 1: // Vuelos
                try {
                    while (result.next()) {
                        System.out.println("Origen: " + result.getString("origin"));
                        System.out.println("Destino: " + result.getString("destination"));
                        System.out.println("Fecha de salida: " + result.getString("departure"));
                        System.out.println("Duración: " + result.getString("duration"));
                        System.out.println("Asientos: " + result.getString("seats"));
                        System.out.println("--------------------------------------------");
                    }
                } catch (Exception e) {
                    System.out.println("Ocurrió un error inesperado, " + e.toString());
                }
                break;
            case 2: // Reservas
                try {
                    while (result.next()) {
                        System.out.println("Nombre del pasajero: " + result.getString("Name"));
                        System.out.println("Asientos: " + result.getString("seats"));
                        System.out.println("Número de confirmación: " + result.getString("confirmation_number"));
                        System.out.println("Id del vuelo: " + result.getString("flight_id"));
                        System.out.println("--------------------------------------------");
                    }
                } catch (Exception e) {
                    System.out.println("Ocurrió un error inesperado, " + e.toString());
                }
                break;
        }

    };

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printMenu() {
        System.out.println("Bienvenido al Sistema de reservas de vuelos");
        System.out.println("--------------------------------------------");
        System.out.println("Por favor, seleccione una opción:");
        System.out.println("1. Gestionar vuelos");
        System.out.println("2. Consultar vuelo");
        System.out.println("3. Reservar vuelo");
        System.out.println("4. Salir");
        System.out.println("--------------------------------------------");
        System.out.println("Ingrese el número de la opción seleccionada:");
    }

    public boolean validateOption(int option) {
        if (option < 1 || option > 4) {
            return false;
        }
        return true;
    }

    public void MainMenu() {
        clearScreen();
        // Scanner input = new Scanner(System.in);
        while (true) {
            printMenu();
            try {
                int option = input.nextInt();
                input.nextLine();
                if (validateOption(option) == false) {
                    System.out.println("Opción inválida, por favor intente de nuevo");
                    continue;
                }

                if (option == 4) {
                    input.close();
                    System.out.println("Gracias por usar el sistema de reservas de vuelos");
                    break;
                }
                try {
                    action(option);
                } catch (Exception e) {
                    input.close();
                    System.out.println("Ocurrió un error inesperado MM2, " + e.toString());
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado MM1, " + e.toString());
                input.close();
                break;
            }
        }
    }

    private void action(int option) {
        switch (option) {
            case 1:
                System.out.println("Gestionar vuelos");
                GVMenu();
                break;
            case 2:
                System.out.println("Consultar vuelo");
                CVMenu();
                // ConsultarVuelo();
                break;
            case 3:
                System.out.println("Reservar vuelo");
                RVMenu();
                // ReservarVuelo();
                break;
        }
        return;
    }

    public void GVMenu() {
        // clearScreen();
        // Scanner input2 = new Scanner(System.in);
        while (true) {
            printMenuGV();
            try {
                int option = input.nextInt();
                input.nextLine();
                if (validateOption_GV(option) == false) {
                    System.out.println("Opción inválida, por favor intente de nuevo");
                    continue;
                }

                if (option == 5) {
                    // input.close();
                    // System.out.println("Gracias por usar el sistema de reservas de vuelos");
                    break;
                }
                try {
                    action_GV(option);
                } catch (Exception e) {
                    input.close();
                    System.out.println("Ocurrió un error inesperado 2GV, " + e.getMessage());
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado 1GV, " + e.getMessage());
                input.close();
                break;
            }
        }
    }

    public boolean validateOption_GV(int option) {
        if (option < 1 || option > 5) {
            return false;
        }
        return true;
    }

    private void printMenuGV() {
        System.out.println("Seleccione una de las siguientes opciones:");
        System.out.println("--------------------------------------------");
        System.out.println("1. Crear vuelo");
        System.out.println("2. Editar vuelo");
        System.out.println("3. Eliminar vuelo");
        System.out.println("4. Eliminar todos los vuelos");
        System.out.println("5. Volver al menú principal");
        System.out.println("--------------------------------------------");
        System.out.println("Ingrese el número de la opción seleccionada:");

    }

    private void action_GV(int option) {
        switch (option) {
            case 1:
                System.out.println("Crear vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure = input.nextLine();
                System.out.println("Ingrese la duración del vuelo:");
                String duration = input.nextLine();
                System.out.println("Ingrese la cantidad de asientos del vuelo:");
                int seats = input.nextInt();
                input.nextLine();
                CrearVuelo(origin, destination, departure, duration, seats);
                break;
            case 2:
                System.out.println("Editar vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin3 = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination3 = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure3 = input.nextLine();
                System.out.println("Ingrese la nueva duración del vuelo:");
                String duration3 = input.nextLine();
                System.out.println("Ingrese la nueva cantidad de asientos del vuelo:");
                int seats3 = input.nextInt();
                input.nextLine();
                EditarVuelo(origin3, destination3, departure3, duration3, seats3);
                break;
            case 3:
                System.out.println("Eliminar vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin4 = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination4 = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure4 = input.nextLine();
                deleteFlight(origin4, destination4, departure4);
                break;
            case 4:
                System.out.println("Eliminar todos los vuelos");
                System.out.println("--------------------------------------------");
                boolean flag = true;
                while (flag) {
                    System.out.println("¿Está seguro que desea eliminar todos los vuelos? (s/n)");
                    String confirm = input.nextLine();
                    if (confirm.equals("s")) {
                        System.out.println("Eliminando todos los vuelos...");
                        database.deleteAllFlights();
                        flag = false;
                    } else if (confirm.equals("n")) {
                        System.out.println("No se eliminaron los vuelos");
                        flag = false;
                    } else {
                        System.out.println("Opción inválida, por favor intente de nuevo");
                    }
                }
                break;
        }
        return;
    }

    public int CrearVuelo(String origin, String destination, String departure, String duration, int seats) {
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // split departure
        String[] parts = departure.split("-");
        String[] dates = parts[0].split("/");
        String[] hours = parts[1].split(":");
        // validar fecha
        valid = verificarFecha(dates);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // validar hora
        valid = verificarHora(hours);
        if (valid == false) {
            System.out.println("Formato de hora inválido");
            return -3;
        }
        valid = Pattern.matches(durationPattern, duration);
        if (valid == false) {
            System.out.println("Formato de duración inválido");
            return -1;
        }
        database.insertFlight(origin, destination, departure, duration, seats);
        return 1;
    };

    public int EditarVuelo(String origin, String destination, String departure, String duration, int seats) {
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // split departure
        String[] parts = departure.split("-");
        String[] dates = parts[0].split("/");
        String[] hours = parts[1].split(":");
        // validar fecha
        valid = verificarFecha(dates);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // validar hora
        valid = verificarHora(hours);
        if (valid == false) {
            System.out.println("Formato de hora inválido");
            return -3;
        }
        valid = Pattern.matches(durationPattern, duration);
        if (valid == false) {
            System.out.println("Formato de duración inválido");
            return -1;
        }
        database.updateFlight(origin, destination, departure, duration, seats);
        return 1;
    };

    public int deleteFlight(String origin, String destination, String departure) {
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // split departure
        String[] parts = departure.split("-");
        String[] dates = parts[0].split("/");
        String[] hours = parts[1].split(":");
        // validar fecha
        valid = verificarFecha(dates);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // validar hora
        valid = verificarHora(hours);
        if (valid == false) {
            System.out.println("Formato de hora inválido");
            return -3;
        }
        database.deleteFlight(origin, destination, departure);
        return 1;
    };

    public void CVMenu() {

        while (true) {
            printMenuCV();
            try {
                int option = input.nextInt();
                input.nextLine();
                if (validateOption_CV(option) == false) {
                    System.out.println("Opción inválida, por favor intente de nuevo");
                    continue;
                }

                if (option == 3) {
                    // input.close();
                    // System.out.println("Gracias por usar el sistema de reservas de vuelos");
                    break;
                }
                try {
                    action_CV(option);
                } catch (Exception e) {
                    input.close();
                    System.out.println("Ocurrió un error inesperado 2CV, " + e.getMessage());
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado 1CV, " + e.getMessage());
                input.close();
                break;
            }
        }

    }

    public boolean validateOption_CV(int option) {
        if (option < 1 || option > 3) {
            return false;
        }
        return true;

    }

    public void printMenuCV() {
        System.out.println("Seleccione una de las siguientes opciones:");
        System.out.println("--------------------------------------------");
        System.out.println("1. Buscar vuelo");
        System.out.println("2. Mostrar todos los vuelos");
        System.out.println("3. Volver al menú principal");
        System.out.println("--------------------------------------------");
        System.out.println("Ingrese el número de la opción seleccionada:");
    }

    public void action_CV(int option) {
        switch (option) {
            case 1:
                System.out.println("Buscar vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure = input.nextLine();
                BuscarVuelo(origin, destination, departure);
                break;
            case 2:
                System.out.println("Mostrar todos los vuelos");
                System.out.println("--------------------------------------------");
                ResultSet result = database.showAllFlights();
                PrintResult(result, 1);
                break;
        }
        return;
    }

    public int BuscarVuelo(String origin, String destination, String departure) {
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // split departure
        String[] parts = departure.split("-");
        String[] dates = parts[0].split("/");
        String[] hours = parts[1].split(":");
        // validar fecha
        valid = verificarFecha(dates);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // validar hora
        valid = verificarHora(hours);
        if (valid == false) {
            System.out.println("Formato de hora inválido");
            return -3;
        }
        ResultSet result = database.showFlight(origin, destination, departure);
        PrintResult(result, 1);
        return 1;
    };

    public void RVMenu() {
        while (true) {
            printMenuRV();
            try {
                int option = input.nextInt();
                input.nextLine();
                if (validateOption_RV(option) == false) {
                    System.out.println("Opción inválida, por favor intente de nuevo");
                    continue;
                }

                if (option == 5) {
                    // input.close();
                    // System.out.println("Gracias por usar el sistema de reservas de vuelos");
                    break;
                }
                try {
                    action_RV(option);
                } catch (Exception e) {
                    input.close();
                    System.out.println("Ocurrió un error inesperado 2RV, " + e.getMessage());
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado 1RV, " + e.getMessage());
                input.close();
                break;
            }
        }
    }

    public boolean validateOption_RV(int option) {
        if (option < 1 || option > 5) {
            return false;
        }
        return true;
    }

    public void printMenuRV() {
        System.out.println("Seleccione una de las siguientes opciones:");
        System.out.println("--------------------------------------------");
        System.out.println("1. Reservar un vuelo");
        System.out.println("2. Mostrar una reserva");
        System.out.println("3. Mostrar todas las reservas");
        System.out.println("4. Borrar una reserva");
        System.out.println("5. Borrar todas las reservas");
        System.out.println("--------------------------------------------");
        System.out.println("Ingrese el número de la opción seleccionada:");
    }

    public void action_RV(int option) {
        ResultSet result;
        switch (option) {
            case 1:
                System.out.println("Reservar un vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure = input.nextLine();
                System.out.println("Ingrese la cantidasd de asientos:");
                int seats = input.nextInt();
                input.nextLine();
                System.out.println("Ingrese el nombre del pasajero:");
                String passenger = input.nextLine();
                ReservarVuelo(origin, destination, departure, seats, passenger);
                break;
            case 2:
                System.out.println("Mostrar una reserva");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el nombre de la persona que hizo la reserva:");
                String passenger2 = input.nextLine();
                System.out.println("Ingrese el número de confirmación de la reserva:");
                int confirmation_number = input.nextInt();
                input.nextLine();
                result = database.showReservation(passenger2, confirmation_number);
                PrintResult(result, 2);
                break;
            case 3:
                System.out.println("Mostrar todas las reservas");
                System.out.println("--------------------------------------------");
                result = database.showAllReservations();
                PrintResult(result, 2);
                break;
            case 4:
                System.out.println("Borrar una reserva");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el nombre de la persona que hizo la reserva:");
                String passenger3 = input.nextLine();
                System.out.println("Ingrese el número de confirmación de la reserva:");
                int confirmation_number2 = input.nextInt();
                input.nextLine();
                database.deleteReservation(passenger3, confirmation_number2);
            case 5:
                System.out.println("Borrar todas las reservas");
                System.out.println("--------------------------------------------");
                boolean flag = true;
                while (flag) {
                    System.out.println("¿Está seguro que desea eliminar todas las reservas? (s/n)");
                    String confirm = input.nextLine();
                    if (confirm.equals("s")) {
                        System.out.println("Eliminando todas las reservas...");
                        database.deleteAllReservations();
                        flag = false;
                    } else if (confirm.equals("n")) {
                        System.out.println("No se eliminaron las reservas");
                        flag = false;
                    } else {
                        System.out.println("Opción inválida, por favor intente de nuevo");
                    }
                }
                break;
        }
    }

    public int ReservarVuelo(String origin, String destination, String departure, int seats, String passenger) {        
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // split departure
        String[] parts = departure.split("-");
        String[] dates = parts[0].split("/");
        String[] hours = parts[1].split(":");
        // validar fecha
        valid = verificarFecha(dates);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        // validar hora
        valid = verificarHora(hours);
        if (valid == false) {
            System.out.println("Formato de hora inválido");
            return -3;
        }
        String input = passenger + String.valueOf(seats);
        int conformation_number = generateNumericHash(input);
        int a = database.reserveFlight(origin, destination, departure, seats, passenger, conformation_number);
        if (a == 1){
            System.out.println("La reserva de " + passenger + " fue realizada con éxito, su número de confirmación es: " + conformation_number);
        }
        return 1;
    };

}
