package idea.verlif.lifeofdream.domain.rule;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

import java.util.Objects;

/**
 * 规则
 *
 * @author Verlif
 */
public class Rule implements CanSave {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则key
     */
    private String key;

    /**
     * 规则描述
     */
    private String desc;

    /**
     * 执行条件
     */
    private String condition;

    /**
     * 规则效果
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        Rule rule = (Rule) o;
        return Objects.equals(getKey(), rule.getKey());
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
        json.put("desc", desc);
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
        desc = json.getString("desc");
        condition = json.getString("con");
        exec = json.getString("exec");
        return true;
    }
}
