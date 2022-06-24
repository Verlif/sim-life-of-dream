package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.ValueType;
import idea.verlif.lifeofdream.standard.NumberValue;

import java.util.Objects;

/**
 * 角色标签
 *
 * @author Verlif
 */
public class Tag implements NumberValue, CanSave {

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签key
     */
    private String key;

    /**
     * 标签值
     */
    private int value;

    /**
     * 标签是否可见
     */
    private boolean visible = true;

    /**
     * 添加标签时触发
     */
    private String onAdd;

    /**
     * 新回合生效
     */
    private String onTurn;

    /**
     * 移除标签时触发
     */
    private String onRemove;

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

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getOnAdd() {
        return onAdd;
    }

    public void setOnAdd(String onAdd) {
        this.onAdd = onAdd;
    }

    public String getOnTurn() {
        return onTurn;
    }

    public void setOnTurn(String onTurn) {
        this.onTurn = onTurn;
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
        Tag tag = (Tag) o;
        return Objects.equals(getKey(), tag.getKey());
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
        json.put("vis", visible);
        json.put("add", onAdd);
        json.put("turn", onTurn);
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
        visible = json.getBooleanValue("vis");
        onAdd = json.getString("add");
        onTurn = json.getString("turn");
        onRemove = json.getString("rem");
        return true;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public void up(int up) {
        value += up;
        if (up != 0) {
            NoticeRunner.notice(name, up, ValueType.TAG);
        }
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    public Tag copy() {
        Tag tag = new Tag();
        tag.load(save());
        return tag;
    }
}
