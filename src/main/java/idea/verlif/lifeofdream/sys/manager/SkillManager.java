package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.extra.Skill;

import java.util.Map;

/**
 * @author Verlif
 */
public class SkillManager implements CanSave {

    private static final SkillManager SKILL_MANAGER = new SkillManager();

    private final CanSavedMap<String, Skill> skillMap;

    private SkillManager() {
        skillMap = new CanSavedMap<String, Skill>() {
            @Override
            protected Skill getNewValue(String s) {
                return new Skill();
            }
        };
    }

    public static SkillManager getInstance() {
        return SKILL_MANAGER;
    }

    public Map<String, Skill> getSkillMap() {
        return skillMap;
    }

    public Skill get(String key) {
        Skill skill = skillMap.get(key);
        if (skill != null) {
            return skill.copy();
        }
        return null;
    }

    public void add(Skill skill) {
        skillMap.put(skill.getKey(), skill);
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("sm", skillMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        skillMap.clear();
        return skillMap.load(json.getJSONObject("sm"));
    }
}
