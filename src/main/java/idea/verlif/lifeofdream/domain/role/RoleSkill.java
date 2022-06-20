package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.role.extra.Skill;

/**
 * 角色技能
 *
 * @author Verlif
 */
public class RoleSkill implements CanSave {

    private final Skill swim;

    public RoleSkill() {
        swim = new Skill("游泳", 20);
    }

    public Skill getSwim() {
        return swim;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("swim", swim.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        swim.load(json.getJSONObject("swim"));
        return true;
    }
}
