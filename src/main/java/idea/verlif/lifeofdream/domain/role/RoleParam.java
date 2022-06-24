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
     * 精神
     */
    private final Param spirit;

    /**
     * 专注
     */
    private final Param attentive;

    /**
     * 游泳
     */
    private final Param swimming;

    /**
     * 厨艺
     */
    private final Param cooking;

    /**
     * 驾驶
     */
    private final Param driving;

    /**
     * 音律
     */
    private final Param melody;

    /**
     * 唱功
     */
    private final Param singing;

    /**
     * 协调
     */
    private final Param coordination;

    /**
     * 言语
     */
    private final Param speeching;

    /**
     * 绘画
     */
    private final Param painting;

    /**
     * 文字
     */
    private final Param writing;

    /**
     * 联想
     */
    private final Param association;

    /**
     * 想象
     */
    private final Param imagination;

    /**
     * 记忆
     */
    private final Param memory;

    /**
     * 耐心
     */
    private final Param patience;

    /**
     * 艺术
     */
    private final Param art;

    public RoleParam() {
        charm = new Param("魅力");
        luck = new Param("幸运");
        logic = new Param("逻辑");
        perceptual = new Param("感性");
        spirit = new Param("精神");
        attentive = new Param("专注");
        swimming = new Param("游泳");
        cooking = new Param("厨艺");
        driving = new Param("驾驶");
        melody = new Param("音律");
        singing = new Param("唱功");
        coordination = new Param("协调");
        speeching = new Param("言语");
        painting = new Param("绘画");
        writing = new Param("文字");
        association = new Param("联想");
        imagination = new Param("想象");
        memory = new Param("记忆");
        patience = new Param("耐心");
        art = new Param("艺术");
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

    public Param getSpirit() {
        return spirit;
    }

    public Param getAttentive() {
        return attentive;
    }

    public Param getSwimming() {
        return swimming;
    }

    public Param getCooking() {
        return cooking;
    }

    public Param getDriving() {
        return driving;
    }

    public Param getMelody() {
        return melody;
    }

    public Param getSinging() {
        return singing;
    }

    public Param getCoordination() {
        return coordination;
    }

    public Param getSpeeching() {
        return speeching;
    }

    public Param getPainting() {
        return painting;
    }

    public Param getWriting() {
        return writing;
    }

    public Param getAssociation() {
        return association;
    }

    public Param getImagination() {
        return imagination;
    }

    public Param getMemory() {
        return memory;
    }

    public Param getPatience() {
        return patience;
    }

    public Param getArt() {
        return art;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("cha", charm.save());
        json.put("luck", luck.save());
        json.put("log", logic.save());
        json.put("per", perceptual.save());
        json.put("spi", spirit.save());
        json.put("att", attentive.save());
        json.put("swi", swimming.save());
        json.put("coo", cooking.save());
        json.put("dri", driving.save());
        json.put("mel", melody.save());
        json.put("sin", singing.save());
        json.put("coor", coordination.save());
        json.put("spe", speeching.save());
        json.put("pai", painting.save());
        json.put("wri", writing.save());
        json.put("ast", association.save());
        json.put("ima", imagination.save());
        json.put("mem", memory.save());
        json.put("pat", patience.save());
        json.put("art", art.save());
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
        spirit.load(json.getJSONObject("spi"));
        attentive.load(json.getJSONObject("att"));
        swimming.load(JSONObject.parseObject("swi"));
        cooking.load(JSONObject.parseObject("coo"));
        driving.load(JSONObject.parseObject("dri"));
        melody.load(JSONObject.parseObject("mel"));
        singing.load(JSONObject.parseObject("sin"));
        coordination.load(JSONObject.parseObject("coor"));
        speeching.load(JSONObject.parseObject("spe"));
        painting.load(JSONObject.parseObject("pai"));
        writing.load(JSONObject.parseObject("wri"));
        association.load(JSONObject.parseObject("ast"));
        imagination.load(JSONObject.parseObject("ima"));
        memory.load(JSONObject.parseObject("mem"));
        patience.load(JSONObject.parseObject("pat"));
        art.load(JSONObject.parseObject("art"));
        return true;
    }
}
