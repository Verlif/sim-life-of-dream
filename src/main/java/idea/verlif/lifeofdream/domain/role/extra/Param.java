package idea.verlif.lifeofdream.domain.role.extra;

/**
 * 判定属性类
 *
 * @author Verlif
 */
public class Param {

    /**
     * 属性名称
     */
    private final String name;

    /**
     * 属性值
     */
    private int value;

    public Param(String name) {
        this.name = name;
        this.value = 0;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
