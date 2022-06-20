package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.standard.LevelValue;
import idea.verlif.lifeofdream.standard.NumberValue;

/**
 * 技能类
 *
 * @author Verlif
 */
public class Skill implements NumberValue, LevelValue, CanSave {

    /**
     * 技能名称
     */
    private final String name;

    /**
     * 技能等级
     */
    private int level;

    /**
     * 技能值
     */
    private int value;

    /**
     * 下一等级需要的技能值
     */
    private int next;

    public Skill(String name, int next) {
        this.name = name;
        this.level = 0;
        this.value = 0;
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public void up(int up) {
        value += up;
        while (value >= next) {
            next = next << 1;
            value -= next;
            level ++;
        }
    }

    @Override
    public int level() {
        return level;
    }

    @Override
    public void levelUp(int up) {
        level += up;
        if (level < 0) {
            level = 0;
        }
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("lel", level);
        json.put("val", value);
        json.put("next", next);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        level = json.getIntValue("lel");
        value = json.getIntValue("val");
        next = json.getIntValue("next");
        return true;
    }
}
