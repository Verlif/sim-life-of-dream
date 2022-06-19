package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.role.extra.Param;

/**
 * 角色判定属性
 *
 * @author Verlif
 */
public class RoleParam implements CanSave {

    /**
     * 魅力
     */
    private final Param charm;

    /**
     * 幸运
     */
    private final Param luck;

    /**
     * 逻辑
     */
    private final Param logic;

    /**
     * 感性
     */
    private final Param perceptual;

    /**
     * 运动
     */
    private final Param sport;

    /**
     * 精神
     */
    private final Param spirit;

    public RoleParam() {
        charm = new Param("魅力");
        luck = new Param("幸运");
        logic = new Param("逻辑");
        perceptual = new Param("感性");
        sport = new Param("运动");
        spirit = new Param("精神");
    }

    public Param getCharm() {
        return charm;
    }

    public Param getLuck() {
        return luck;
    }

    public Param getLogic() {
        return logic;
    }

    public Param getPerceptual() {
        return perceptual;
    }

    public Param getSport() {
        return sport;
    }

    public Param getSpirit() {
        return spirit;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("cha", charm.save());
        json.put("luck", luck.save());
        json.put("log", logic.save());
        json.put("per", perceptual.save());
        json.put("spo", sport.save());
        json.put("spi", spirit.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        charm.load(json.getJSONObject("cha"));
        luck.load(json.getJSONObject("luck"));
        logic.load(json.getJSONObject("log"));
        perceptual.load(json.getJSONObject("per"));
        sport.load(json.getJSONObject("spo"));
        spirit.load(json.getJSONObject("spi"));
        return true;
    }
}
