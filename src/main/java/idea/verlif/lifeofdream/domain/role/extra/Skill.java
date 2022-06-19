package idea.verlif.lifeofdream.domain.role.extra;

/**
 * 技能类
 *
 * @author Verlif
 */
public class Skill {

    /**
     * 技能名称
     */
    private final String name;

    /**
     * 技能等级
     */
    private int level;

    /**
     * 技能值
     */
    private int value;

    /**
     * 下一等级需要的技能值
     */
    private int next;

    public Skill(String name, int next) {
        this.name = name;
        this.level = 0;
        this.value = 0;
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
