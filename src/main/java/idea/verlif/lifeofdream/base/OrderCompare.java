package idea.verlif.lifeofdream.base;

import idea.verlif.lifeofdream.standard.Orderable;

import java.util.Comparator;

/**
 * @author Verlif
 */
public class OrderCompare<T extends Orderable> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
