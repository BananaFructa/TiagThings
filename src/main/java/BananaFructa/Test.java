package BananaFructa;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(5);
        a.add(7);
        List<Integer> b = new ArrayList<>();
        b.add(2);
        b.add(4);
        b.add(6);
        System.out.println(compareIds(a,b));
    }

    public static int compareIds(List<Integer> first, List<Integer> second) {
        int secondIndex = 0;
        for (Integer integer : first) {
            if (secondIndex == second.size()) break;
            while (secondIndex < second.size()) {
                if (second.get(secondIndex) == integer) {
                    secondIndex++;
                    break;
                }
            }
        }
        return secondIndex;
    }

}
