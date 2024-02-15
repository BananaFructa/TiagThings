package BananaFructa.TTIEMultiblocks.Utils;

public class CosSinTable {
    static float[] cos = new float[361];
    static float[] sin = new float[361];

    static {
        for (int i = 0; i <= 360; i++) {
            cos[i] = (float)Math.cos(Math.toRadians(i));
            sin[i] = (float)Math.sin(Math.toRadians(i));
        }
    }

    public static float getSin(float angle) {
        int deg = (int)Math.toDegrees(angle) + 360;
        int angleCircle = deg % 360;
        return sin[angleCircle];
    }

    public static float getCos(float angle) {
        int deg = (int)Math.toDegrees(angle) + 360;
        int angleCircle = deg % 360;
        return cos[angleCircle];
    }
}
