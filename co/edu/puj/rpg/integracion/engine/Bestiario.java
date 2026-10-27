package co.edu.puj.rpg.integracion.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Bestiario {
    private final Map<String, Integer> bestiarioMap;

    public Bestiario() {
        this.bestiarioMap = new HashMap<String, Integer>();
    }

    public boolean registrarVictoria(String nombreEnemigo) {
        String clave = normalizar(nombreEnemigo);
        if (clave.isEmpty()) {
            throw new IllegalArgumentException("El nombre del enemigo no puede estar vacío.");
        }
        boolean esPrimeraVictoria = !bestiarioMap.containsKey(clave);
        bestiarioMap.put(clave, bestiarioMap.getOrDefault(clave, 0) + 1);
        return esPrimeraVictoria;
    }

    public int obtenerDerrotas(String nombreEnemigo) {
        String clave = normalizar(nombreEnemigo);
        if (bestiarioMap.containsKey(clave)) {
            return bestiarioMap.get(clave);
        }
        return 0;
    }

    public int totalDerrotas() {
        int total = 0;
        for (Map.Entry<String, Integer> entry : bestiarioMap.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public Map<String, Integer> getBestiarioMap() {
        return Collections.unmodifiableMap(bestiarioMap);
    }

    private String normalizar(String nombre) {
        if (nombre == null) {
            return "";
        }
        String limpio = nombre.trim();
        if (limpio.isEmpty()) {
            return limpio;
        }
        StringBuilder clave = new StringBuilder();
        for (String palabra : limpio.split("\\s+")) {
            if (clave.length() > 0) {
                clave.append(' ');
            }
            clave.append(Character.toUpperCase(palabra.charAt(0)));
            clave.append(palabra.substring(1).toLowerCase());
        }
        return clave.toString();
    }
}
