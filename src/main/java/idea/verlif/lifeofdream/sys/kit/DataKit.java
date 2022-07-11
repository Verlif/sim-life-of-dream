package idea.verlif.lifeofdream.sys.kit;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 */
public class DataKit implements CanSave {

    private final Map<String, Object> dataMap;

    public DataKit() {
        dataMap = new HashMap<>();
    }

    public String getText(String key) {
        Object val = dataMap.get(key);
        return val == null ? "" : val instanceof String ? (String) val : val.toString();
    }

    public int getInt(String key) {
        Object val = dataMap.get(key);
        if (val == null) {
            return 0;
        }
        if (val instanceof Integer) {
            return (int) val;
        } else {
            try {
                return Integer.parseInt(val.toString());
            } catch (Exception ignored) {
                return 0;
            }
        }
    }

    public void setText(String key, String val) {
        dataMap.put(key, val);
    }

    public void setInt(String key, int val) {
        dataMap.put(key, val);
    }

    public void remove(String key) {
        dataMap.remove(key);
    }

    public void clear() {
        dataMap.clear();
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("data", new HashMap<>(dataMap));
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        clear();
        JSONObject data = json.getJSONObject("data");
        dataMap.putAll(data);
        return true;
    }
}
