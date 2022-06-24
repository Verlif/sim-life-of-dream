package idea.verlif.lifeofdream.notice.entity;

/**
 * 数值消息
 *
 * @author Verlif
 */
public class ValueNotice {

    private final String name;

    private final int change;

    private final ValueType type;

    public ValueNotice(String name, int change, ValueType type) {
        this.name = name;
        this.change = change;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getChange() {
        return change;
    }

    public ValueType getType() {
        return type;
    }
}
