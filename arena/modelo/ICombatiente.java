package arena.modelo;

/*
 * Contrato de comportamiento puro: define la acción defensiva mínima que
 * cualquier entidad del área de combate debe ofrecer, sin imponer estado.
 */
public interface ICombatiente {

    // Procesa la reducción de daño según la armadura o mitigación base de la entidad
    void defender(int danoRecibido);
}
