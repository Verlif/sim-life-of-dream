package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.role.extra.Bag;

/**
 * 角色
 *
 * @author Verlif
 */
public class Role implements CanSave {

    private final RoleInfo info;

    private final RoleAttr attr;

    private final RoleSkill skill;

    private final RoleParam param;

    private final RoleTag tag;

    private final Bag bag;

    public Role() {
        info = new RoleInfo();
        attr = new RoleAttr();
        skill = new RoleSkill();
        param = new RoleParam();
        tag = new RoleTag();
        bag = new Bag();
    }

    public RoleInfo getInfo() {
        return info;
    }

    public RoleAttr getAttr() {
        return attr;
    }

    public RoleSkill getSkill() {
        return skill;
    }

    public RoleParam getParam() {
        return param;
    }

    public RoleTag getTag() {
        return tag;
    }

    public Bag getBag() {
        return bag;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("info", info.save());
        json.put("attr", attr.save());
        json.put("ski", skill.save());
        json.put("par", param.save());
        json.put("tag", tag.save());
        json.put("bag", bag.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        info.load(json.getJSONObject("info"));
        attr.load(json.getJSONObject("attr"));
        skill.load(json.getJSONObject("ski"));
        param.load(json.getJSONObject("par"));
        tag.load(json.getJSONObject("tag"));
        bag.load(json.getJSONObject("bag"));
        return true;
    }
}
