package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.role.extra.Attr;

/**
 * 角色基础属性
 *
 * @author Verlif
 */
public class RoleAttr implements CanSave {

    /**
     * 健康值
     */
    private final Attr health;

    /**
     * 体力值
     */
    private final Attr endurance;

    /**
     * 脑力值
     */
    private final Attr brain;

    /**
     * 心情值
     */
    private final Attr mood;

    public RoleAttr() {
        health = new Attr("健康值", 100);
        endurance = new Attr("体力值", 100);
        brain = new Attr("脑力值", 100);
        mood = new Attr("心情值", 100);
    }

    public Attr getHealth() {
        return health;
    }

    public Attr getEndurance() {
        return endurance;
    }

    public Attr getBrain() {
        return brain;
    }

    public Attr getMood() {
        return mood;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("hea", health.save());
        json.put("end", endurance.save());
        json.put("bra", brain.save());
        json.put("mood", mood.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        health.load(json.getJSONObject("hea"));
        endurance.load(json.getJSONObject("end"));
        brain.load(json.getJSONObject("bra"));
        mood.load(json.getJSONObject("mood"));
        return true;
    }
}
