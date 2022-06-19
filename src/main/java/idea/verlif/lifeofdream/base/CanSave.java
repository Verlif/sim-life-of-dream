package idea.verlif.lifeofdream.base;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Verlif
 * @version 1.0
 */
public interface CanSave {

    /**
     * 保存数据
     *
     * @return 数据json
     */
    JSONObject save();

    /**
     * 加载数据
     *
     * @param json 数据json
     * @return 是否成功加载
     */
    boolean load(JSONObject json);
}