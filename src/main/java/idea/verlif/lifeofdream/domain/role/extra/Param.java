package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.ValueType;
import idea.verlif.lifeofdream.standard.NumberValue;

/**
 * 判定属性类
 *
 * @author Verlif
 */
public class Param implements NumberValue, CanSave {

    /**
     * 属性名称
     */
    private final String name;

    /**
     * 属性值
     */
    private int value;

    public Param(String name) {
        this.name = name;
        this.value = 0;
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

    @Override
    public int value() {
        return value;
    }

    @Override
    public void up(int up) {
        value += up;
        if (value < 0) {
            value = 0;
        }
        if (up != 0) {
            NoticeRunner.notice(name, up, ValueType.PARAM);
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
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        value = json.getIntValue("val");
        return true;
    }
}
