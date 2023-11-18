package src;
import java.util.Scanner;

public class FlightManager {

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void printMenu(){
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

    public boolean validateOption(int option){
        if (option < 1 || option > 4){
            return false;
        }
        return true;
    }

    public void menu(){
        clearScreen();
        Scanner input = new Scanner(System.in);
        while (true){
            printMenu();
            try{
                int option = input.nextInt();
                if (validateOption(option) == false){
                    System.out.println("Opción inválida, por favor intente de nuevo");
                    continue;
                }

                if (option == 4){
                    input.close();
                    System.out.println("Gracias por usar el sistema de reservas de vuelos");
                    break;
                }
                try{
                    action(option);
                } catch (Exception e){
                    input.close();
                    System.out.println("Ocurrió un error inesperado, " + e.getMessage());
                    break;
                }
            } catch (Exception e){
                System.out.println("Ocurrió un error inesperado, " + e.getMessage());
                input.close();
                break;
            }
        }
    }

    private void action(int option){
        switch (option){
            case 1:
                System.out.println("Gestionar vuelos");
                gestionarVuelo();
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
    }

    private void gestionarVuelo(){

    }
}
