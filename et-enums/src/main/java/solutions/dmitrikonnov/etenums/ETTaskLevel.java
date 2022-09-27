package solutions.dmitrikonnov.etenums;

public enum ETTaskLevel {
    A0,
    A1,A1_1,A1_2,A1_3,
    A2,A2_1,A2_2,A2_3,
    B1,B1_1,B1_2,B1_3,
    B1plus,B1plus_1,B1plus_2,B1plus_3,
    B2,B2_1,B2_2,B2_3,
    C1,C1_1,C1_2,C1_3,
    C2,C2_1,C2_2,C2_3,
    A0andA1, A1andA2,A2andB1,B1andB1plus,B1plusAndB2,B2andC1,C1andC2;


    public static ETTaskLevel getNiveauMitPostfix1 (ETTaskLevel n){

        return ETTaskLevel.valueOf(n.toString().concat("_1"));
    }
    public static ETTaskLevel getNiveauMitPostfix2 (ETTaskLevel n){

        return ETTaskLevel.valueOf(n.toString().concat("_2"));
    }
    public static ETTaskLevel getNiveauMitPostfix3 (ETTaskLevel n){

        return ETTaskLevel.valueOf(n.toString().concat("_3"));
    }
}
