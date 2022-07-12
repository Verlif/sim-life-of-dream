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

    /**
     * 获得技能时触发。每个道具数量都会触发一次。
     */
    private String onAdd;

    /**
     * 失去技能时触发。每个道具数量都会触发一次。
     */
    private String onRemove;

    /**
     * 在经验值或等级小于零时自动移除技能
     */
    private boolean autoRemove = false;

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

    public boolean isAutoRemove() {
        return autoRemove;
    }

    public void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
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
    public void down(int down) {
        value -= down;
        while (value < 0 && level > 0) {
            next = next >> 1;
            value += next;
            level--;
            NoticeRunner.notice(Tip.SKILL_LEVEL_DOWN);
        }
        if (down != 0) {
            NoticeRunner.notice(name, -down, ValueType.SKILL);
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
        json.put("key", key);
        json.put("lel", level);
        json.put("val", value);
        json.put("next", next);
        json.put("add", onAdd);
        json.put("rem", onRemove);
        json.put("ar", autoRemove);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        name = json.getString("name");
        key = json.getString("key");
        level = json.getIntValue("lel");
        value = json.getIntValue("val");
        next = json.getIntValue("next");
        onAdd = json.getString("add");
        onRemove = json.getString("rem");
        autoRemove = json.getBooleanValue("ar");
        return true;
    }

    public Skill copy() {
        Skill skill = new Skill();
        skill.load(save());
        return skill;
    }
}
