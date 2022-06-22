package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.standard.NumberValue;

/**
 * 基础属性类
 *
 * @author Verlif
 */
public class Attr implements NumberValue, CanSave {

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

    @Override
    public int value() {
        return value;
    }

    @Override
    public void up(int up) {
        this.value += up;
        if (value > max) {
            value = max;
        } else if (value < 0) {
            value = 0;
        }
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("val", value);
        json.put("max", max);
        json.put("grow", grow);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        value = json.getIntValue("val");
        max = json.getIntValue("max");
        grow = json.getIntValue("grow");
        return true;
    }
}
