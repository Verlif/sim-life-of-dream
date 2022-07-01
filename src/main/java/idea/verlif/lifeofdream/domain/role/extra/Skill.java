package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.notice.entity.ValueType;
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
    private String name;

    /**
     * 技能唯一键值
     */
    private String key;

    /**
     * 技能等级
     */
    private int level = 0;

    /**
     * 技能值
     */
    private int value = 0;

    /**
     * 下一等级需要的技能值
     */
    private int next = 20;

    public Skill() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
            value -= next;
            next = next << 1;
            level++;
            NoticeRunner.notice(Tip.SKILL_LEVEL_UP);
        }
        if (up != 0) {
            NoticeRunner.notice(name, up, ValueType.SKILL);
        }
    }

    @Override
    public void set(int value) {
        this.value = 0;
        up(value);
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
        json.put("name", name);
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
        name = json.getString("name");
        level = json.getIntValue("lel");
        value = json.getIntValue("val");
        next = json.getIntValue("next");
        return true;
    }

    public Skill copy() {
        Skill skill = new Skill();
        skill.load(save());
        return skill;
    }
}
