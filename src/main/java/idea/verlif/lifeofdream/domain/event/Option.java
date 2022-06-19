package idea.verlif.lifeofdream.domain.event;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 事件选项
 *
 * @author Verlif
 */
public class Option implements CanSave {

    /**
     * 衔接的事件Key列表
     */
    private final List<String> afterEvents;

    /**
     * 选项key
     */
    private String key;

    /**
     * 选项标题
     */
    private String title;

    /**
     * 选项描述。用于选择生效后的显示。
     */
    private String desc;

    /**
     * 概率描述
     */
    private String chance = "10000";

    /**
     * 生效条件
     */
    private String condition;

    /**
     * 触发效果
     */
    private String exec;

    public Option() {
        afterEvents = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public List<String> getAfterEvents() {
        return afterEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Option option = (Option) o;
        return Objects.equals(getKey(), option.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("tit", title);
        json.put("key", key);
        json.put("desc", desc);
        json.put("cha", chance);
        json.put("con", condition);
        json.put("exec", exec);
        json.put("aes", afterEvents);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        title = json.getString("tit");
        key = json.getString("key");
        desc = json.getString("desc");
        chance = json.getString("cha");
        condition = json.getString("con");
        exec = json.getString("exec");
        afterEvents.clear();
        afterEvents.addAll(json.getList("aes", String.class));
        return true;
    }
}
