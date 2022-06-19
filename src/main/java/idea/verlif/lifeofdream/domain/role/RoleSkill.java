package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

/**
 * 角色技能
 *
 * @author Verlif
 */
public class RoleSkill implements CanSave {
    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        return true;
    }
}
