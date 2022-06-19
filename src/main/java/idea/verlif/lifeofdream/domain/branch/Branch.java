package idea.verlif.lifeofdream.domain.story.branch;

/**
 * 支线
 *
 * @author Verlif
 */
public class Branch {

    /**
     * 支线名称
     */
    private String name;

    /**
     * 支线key
     */
    private String key;

    /**
     * 概率描述
     */
    private String chance;

    /**
     * 触发条件
     */
    private String condition;

    /**
     * 触发效果
     */
    private String exec;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getExec() {
        return exec;
    }

    public void setExec(String exec) {
        this.exec = exec;
    }
}
