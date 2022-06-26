package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.role.extra.Skill;
import idea.verlif.lifeofdream.sys.manager.SkillManager;

/**
 * 角色技能
 *
 * @author Verlif
 */
public class RoleSkill implements CanSave {

    private final CanSavedMap<String, Skill> skillMap;

    public RoleSkill() {
        skillMap = new CanSavedMap<String, Skill>() {
            @Override
            protected Skill getNewValue(String s) {
                return new Skill();
            }
        };
    }

    public boolean has(String key) {
        return skillMap.containsKey(key);
    }

    public boolean hasno(String key) {
        return !skillMap.containsKey(key);
    }

    /**
     * 获取技能值
     *
     * @param key 技能Key
     * @return 技能值。当技能不存在时，返回-1。
     */
    public int value(String key) {
        Skill skill = skillMap.get(key);
        if (skill == null) {
            return -1;
        }
        return skill.value();
    }

    public void set(String key, int value) {
        if (!skillMap.containsKey(key)) {
            add(key);
        }
        Skill skill = skillMap.get(key);
        if (skill != null) {
            skill.set(value);
        }
    }

    public void up(String key, int up) {
        Skill skill = skillMap.get(key);
        if (skill == null) {
            add(key);
            skill = skillMap.get(key);
        }
        if (skill != null) {
            skill.up(up);
        }
    }

    public void down(String key, int down) {
        up(key, -down);
    }

    public boolean eq(String key, int value) {
        Skill skill = skillMap.get(key);
        if (skill != null) {
            return skill.eq(value);
        }
        return false;
    }

    public boolean bt(String key, int value) {
        Skill skill = skillMap.get(key);
        if (skill != null) {
            return skill.bt(value);
        }
        return false;
    }

    public boolean lt(String key, int value) {
        Skill skill = skillMap.get(key);
        if (skill != null) {
            return skill.lt(value);
        }
        return false;
    }

    public boolean ne(String key, int value) {
        Skill skill = skillMap.get(key);
        if (skill != null) {
            return skill.ne(value);
        }
        return false;
    }

    public void add(String key) {
        if (!skillMap.containsKey(key)) {
            Skill skill = SkillManager.getInstance().get(key);
            if (skill != null) {
                skillMap.put(key, skill);
            }
        }
    }

    public void remove(String key) {
        skillMap.remove(key);
    }

    public CanSavedMap<String, Skill> getSkillMap() {
        return skillMap;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("sm", skillMap.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        skillMap.clear();
        if (json == null) {
            return false;
        }
        skillMap.load(json.getJSONObject("sm"));
        return true;
    }
}
