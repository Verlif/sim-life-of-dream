package idea.verlif.enabletheworld.module.base;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/7/1 15:26
 */
public interface CanSave {

    JSONObject toSave();

    boolean load(JSONObject json);

}