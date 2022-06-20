package idea.verlif.lifeofdream.tool;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Verlif
 */
public class ChanceRandom<T> {

    private final ArrayList<Integer> idx;

    private final ArrayList<T> list;

    private int total;

    private final Random random;

    public ChanceRandom() {
        idx = new ArrayList<>();
        list = new ArrayList<>();
        total = 0;
        random = new Random();
    }

    public void add(int chance, T t) {
        idx.add(chance + total);
        list.add(t);
        total += chance;
    }

    public T random() {
        if (list.size() == 0) {
            return null;
        }
        int target = random.nextInt(total);
        for (int i = 0; i < idx.size(); i++) {
            if (idx.get(i) >= target) {
                return list.get(i);
            }
        }
        return list.get(0);
    }
}
