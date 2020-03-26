package com.isograd.exercise.y_model;

public class PogeType implements Comparable<PogeType> {
    String type;

    public PogeType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(PogeType o) {
        switch (type) {
            case FEU:
                if (o.type.equals(PLANTE) || o.type.equals(GLACE)) return 1;
                if (o.type.equals(EAU)) return -1;
                return 0;
            case EAU:
                if (o.type.equals(PLANTE) || o.type.equals(SOL)) return -1;
                if (o.type.equals(FEU)) return 1;
                return 0;
            case PLANTE:
                if (o.type.equals(POISON) || o.type.equals(VOL) || o.type.equals(EAU)) return 1;
                if (o.type.equals(SOL)) return -1;
                return 0;
            case GLACE:
                if (o.type.equals(FEU)) return -1;
                return 0;
            case POISON:
            case VOL:
                if (o.type.equals(PLANTE)) return -1;
                return 0;
            case SOL:
                if (o.type.equals(EAU) || o.type.equals(PLANTE)) return 1;
                return 0;
        }
        return 0;
    }

    public static final String FEU = "feu";
    public static final String EAU = "eau";
    public static final String PLANTE = "plante";
    public static final String GLACE = "Glace";
    public static final String POISON = "poison";
    public static final String SOL = "sol";
    public static final String VOL = "vol";

    @Override
    public String toString() {
        return type;
    }
}
