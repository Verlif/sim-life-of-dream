package idea.verlif.lifeofdream.domain.branch;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

import java.util.Objects;

/**
 * 支线
 *
 * @author Verlif
 */
public class Branch implements CanSave {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Branch branch = (Branch) o;
        return Objects.equals(getKey(), branch.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("key", key);
        json.put("cha", chance);
        json.put("con", condition);
        json.put("exec", exec);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        name = json.getString("name");
        key = json.getString("key");
        chance = json.getString("cha");
        condition = json.getString("con");
        exec = json.getString("exec");
        return true;
    }
}
