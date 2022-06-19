package idea.verlif.lifeofdream.domain.role.extra;

/**
 * 基础属性类
 *
 * @author Verlif
 */
public class Attr {

    /**
     * 属性名称
     */
    private final String name;

    /**
     * 当前值
     */
    private int value;

    /**
     * 最大值
     */
    private int max;

    /**
     *  成长值
     */
    private int grow;

    public Attr(String name, int value) {
        this.name = name;
        this.value = value;
        this.max = value;
        this.grow = 0;
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getGrow() {
        return grow;
    }

    public void setGrow(int grow) {
        this.grow = grow;
    }

    public void nextLevel() {
        this.max = max + grow;
    }
}
