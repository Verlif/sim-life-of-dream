package idea.verlif.lifeofdream.domain.event;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

/**
 * 选项结果
 *
 * @author Verlif
 */
public class OptionResult implements CanSave {

    /**
     * 触发条件
     */
    private String condition;

    /**
     * 触发描述
     */
    private String desc;

    /**
     * 触发几率
     */
    private String chance = "10000";

    /**
     * 触发效果
     */
    private String exec;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getExec() {
        return exec;
    }

    public void setExec(String exec) {
        this.exec = exec;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("con", condition);
        json.put("desc", desc);
        json.put("cha", chance);
        json.put("exec", exec);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        condition = json.getString("con");
        desc = json.getString("desc");
        chance = json.getString("cha");
        exec = json.getString("exec");
        return true;
    }
}
