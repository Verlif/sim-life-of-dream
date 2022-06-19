package idea.verlif.lifeofdream.domain.story;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

/**
 * 故事类
 *
 * @author Verlif
 */
public class Story implements CanSave {

    /**
     * 故事名字
     */
    private String name;

    /**
     * 故事描述
     */
    private String desc;

    /**
     * 故事初始
     */
    private String exec;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        json.put("name", name);
        json.put("desc", desc);
        json.put("exec", exec);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        name = json.getString("name");
        desc = json.getString("desc");
        exec = json.getString("exec");
        return true;
    }
}
