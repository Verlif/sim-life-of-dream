package idea.verlif.lifeofdream.domain.item;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.standard.NumberValue;

import java.util.Objects;

/**
 * 道具
 *
 * @author Verlif
 */
public class Item implements NumberValue, CanSave {

    /**
     * 道具名称
     */
    private String name;

    /**
     * 道具key
     */
    private String key;

    /**
     * 道具数量
     */
    private int value = 0;

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

    public int getValue() {
        return value;
    }

    @Override
    public int value() {
        return value;
    }

    public void up(int up) {
        this.value += up;
    }

    public void setValue(int value) {
        this.value = value;
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
        json.put("val", value);
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
        value = json.getIntValue("val");
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
