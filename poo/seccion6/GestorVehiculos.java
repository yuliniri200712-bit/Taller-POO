package poo.seccion6;

// Caso de Estudio 2 corregido: el método de instancia se invoca a través de un objeto
public class GestorVehiculos {

    public void encenderMotor() {
        System.out.println("El motor del vehículo ha sido encendido.");
    }

    public static void main(String[] args) {
        GestorVehiculos gestor = new GestorVehiculos();
        gestor.encenderMotor();
    }
}
