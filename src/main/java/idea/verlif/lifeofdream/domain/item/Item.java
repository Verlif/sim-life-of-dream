package idea.verlif.lifeofdream.domain.item;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.option.Option;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.ValueType;
import idea.verlif.lifeofdream.standard.Conditionable;
import idea.verlif.lifeofdream.standard.NumberValue;
import idea.verlif.lifeofdream.sys.manager.OptionManager;

import java.util.Objects;
import java.util.Set;

/**
 * 道具
 *
 * @author Verlif
 */
public class Item implements NumberValue, CanSave, Conditionable {

    private static final OptionManager OPTION_MANAGER = OptionManager.getInstance();

    /**
     * 道具名称
     */
    private String name;

    /**
     * 道具key
     */
    private String key;

    /**
     * 道具描述
     */
    private String desc;

    /**
     * 道具数量
     */
    private int value = 0;

    /**
     * 道具使用条件
     */
    private String condition;

    /**
     * 使用时触发
     */
    private String onUse;

    /**
     * 添加道具时触发。每个道具数量都会触发一次。
     */
    private String onAdd;

    /**
     * 移除道具时触发。每个道具数量都会触发一次。
     */
    private String onRemove;

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getValue() {
        return value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public void up(int up) {
        if (up != 0) {
            NoticeRunner.notice(name, up, ValueType.ITEM);
        }
        this.value += up;
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOnUse() {
        return onUse;
    }

    public void setOnUse(String onUse) {
        this.onUse = onUse;
    }

    public String getOnAdd() {
        return onAdd;
    }

    public void setOnAdd(String onAdd) {
        this.onAdd = onAdd;
    }

    public String getOnRemove() {
        return onRemove;
    }

    public void setOnRemove(String onRemove) {
        this.onRemove = onRemove;
    }

    public Set<Option> getOptions() {
        return OPTION_MANAGER.getOptionOfItem(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(getKey(), item.getKey());
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
        json.put("val", value);
        json.put("con", condition);
        json.put("use", onUse);
        json.put("add", onAdd);
        json.put("rem", onRemove);
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
        value = json.getIntValue("val");
        condition = json.getString("con");
        onUse = json.getString("use");
        onAdd = json.getString("add");
        onRemove = json.getString("rem");
        return true;
    }

    public Item copy() {
        Item item = new Item();
        item.load(save());
        return item;
    }
}
