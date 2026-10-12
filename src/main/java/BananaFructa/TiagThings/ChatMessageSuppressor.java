package BananaFructa.TiagThings;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageSuppressor {

    public static List<String> suppressedPattern = new ArrayList<>();

    public static void registerPattern(String p) {
        suppressedPattern.add(p);
    }

}
