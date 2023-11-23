package src;

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
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
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
                System.out.println("Option");
                int option = input.nextInt();
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
                    System.out.println("Action");
                    action(option);
                } catch (Exception e) {
                    input.close();
                    System.out.println("Ocurrió un error inesperado MM2, " + e.toString());
                    break;
                }
                System.out.println("Action fin");
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado MM1, " + e.toString());
                input.close();
                break;
            }
        }
    }

    public void GVMenu() {
        // clearScreen();
        // Scanner input2 = new Scanner(System.in);
        while (true) {
            printMenuGV();
            try {
                int option = input.nextInt();
                if (validateOption_GV(option) == false) {
                    System.out.println("Opción inválida, por favor intente de nuevo");
                    continue;
                }

                if (option == 6) {
                    // input.close();
                    // System.out.println("Gracias por usar el sistema de reservas de vuelos");
                    break;
                }
                try {
                    action_GV(option);
                    System.out.println("Opción no implementada");
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

    private void action(int option) {
        switch (option) {
            case 1:
                System.out.println("Gestionar vuelos");
                printMenuGV();
                GVMenu();
                break;
            case 2:
                System.out.println("Consultar vuelo");
                // ConsultarVuelo();
                break;
            case 3:
                System.out.println("Reservar vuelo");
                // ReservarVuelo();
                break;
        }
        return;
    }

    public boolean validateOption_GV(int option) {
        if (option < 1 || option > 6) {
            return false;
        }
        return true;
    }

    private void printMenuGV() {
        System.out.println("Seleccione una de las siguientes opciones:");
        System.out.println("--------------------------------------------");
        System.out.println("1. Crear vuelo");
        System.out.println("2. Buscar vuelo");
        System.out.println("3. Mostrar todos los vuelos");
        System.out.println("4. Editar vuelo");
        System.out.println("5. Eliminar vuelo");
        System.out.println("6. Volver al menú principal");
        System.out.println("--------------------------------------------");
        System.out.println("Ingrese el número de la opción seleccionada:");

    }

    private void action_GV(int option){
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
                CrearVuelo(origin, destination, departure, duration, seats);
                break;
            case 2:
                System.out.println("Buscar vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin2 = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination2 = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure2 = input.nextLine();
                BuscarVuelo(origin2, destination2, departure2);
                break;
            case 3:
                System.out.println("Mostrar todos los vuelos");
                database.showAllFlights();
                break;
            case 4:
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
                EditarVuelo(origin3, destination3, departure3, duration3, seats3);
                break;
            case 5:
                System.out.println("Eliminar vuelo");
                System.out.println("--------------------------------------------");
                System.out.println("Ingrese el origen del vuelo:");
                String origin4 = input.nextLine();
                System.out.println("Ingrese el destino del vuelo:");
                String destination4 = input.nextLine();
                System.out.println("Ingrese la fecha de salida del vuelo (formato dd/mm/yyyy-hh:mm):");
                String departure4 = input.nextLine();
                // deleteFlight(origin4, destination4, departure4);
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
        //split departure
        String[] parts = departure.split("-");
        String[] dates = parts[0].split("/");
        String[] hours = parts[1].split(":");
        //validar fecha
        valid = verificarFecha(dates);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        //validar hora
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

    public int BuscarVuelo(String origin, String destination, String departure) {
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        database.showFlight(origin, destination, departure);
        return 1;
    };

    public int EditarVuelo(String origin, String destination, String departure, String duration, int seats) {
        boolean valid = Pattern.matches(departurePattern, departure);
        if (valid == false) {
            System.out.println("Formato de fecha inválido");
            return -2;
        }
        valid = Pattern.matches(durationPattern, duration);
        if (valid == false) {
            System.out.println("Formato de duración inválido");
            return -1;
        }
        database.updateFlight(origin, destination, departure, duration, seats);
        return 1;
    };

    
}
