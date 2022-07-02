package idea.verlif.lifeofdream.domain.option;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.standard.Chancable;
import idea.verlif.lifeofdream.standard.Conditionable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 事件选项
 *
 * @author Verlif
 */
public class Option implements CanSave, Chancable, Conditionable {

    /**
     * 衔接的事件Key列表
     */
    private final List<String> followEvents;

    /**
     * 衔接的道具Key列表
     */
    private final List<String> followItems;

    /**
     * 选项key
     */
    private String key;

    /**
     * 选项名称
     */
    private String name;

    /**
     * 选项描述。用于选择生效后的显示。
     */
    private String desc;

    /**
     * 选项打印
     */
    private String print;

    /**
     * 概率描述
     */
    private String chance = "10000";

    /**
     * 生效条件
     */
    private String condition;

    /**
     * 触发效果组
     */
    private final List<OptionResult> resultList;

    public Option() {
        followEvents = new ArrayList<>();
        followItems = new ArrayList<>();
        resultList = new ArrayList<>();
    }

    public String getKey() {
        if (key == null) {
            key = name;
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    @Override
    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<OptionResult> getResultList() {
        return resultList;
    }

    public List<String> getFollowEvents() {
        return followEvents;
    }

    public List<String> getFollowItems() {
        return followItems;
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
        json.put("name", name);
        json.put("key", key);
        json.put("desc", desc);
        json.put("pri", print);
        json.put("cha", chance);
        json.put("con", condition);
        json.put("res", resultList);
        json.put("fes", followEvents);
        json.put("fis", followItems);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        resultList.clear();
        followEvents.clear();
        followItems.clear();
        if (json == null) {
            return false;
        }
        name = json.getString("name");
        key = json.getString("key");
        desc = json.getString("desc");
        print = json.getString("pri");
        chance = json.getString("cha");
        condition = json.getString("con");
        if (json.containsKey("res")) {
            resultList.addAll(json.getList("res", OptionResult.class));
        }
        if (json.containsKey("fes")) {
            followEvents.addAll(json.getList("fes", String.class));
        }
        if (json.containsKey("fis")) {
            followItems.addAll(json.getList("fis", String.class));
        }
        return true;
    }

    public Option copy() {
        Option option = new Option();
        option.load(save());
        return option;
    }
}
