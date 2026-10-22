package co.edu.puj.rpg.relaciones;

public class CorazonRPG {
    private int saludBase;
    private int manaBase;

    public CorazonRPG(int saludBase, int manaBase) {
        this.saludBase = saludBase;
        this.manaBase = manaBase;
    }

    public int getSaludBase() {
        return saludBase;
    }

    public int getManaBase() {
        return manaBase;
    }

    void recibirDanio(int cantidad) {
        if (cantidad > 0) {
            saludBase = Math.max(0, saludBase - cantidad);
        }
    }

    void curar(int cantidad) {
        if (cantidad > 0) {
            saludBase += cantidad;
        }
    }

    boolean consumirMana(int cantidad) {
        if (cantidad > 0 && manaBase >= cantidad) {
            manaBase -= cantidad;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CorazonRPG[saludBase=").append(saludBase);
        sb.append(", manaBase=").append(manaBase).append("]");
        return sb.toString();
    }
}
