package idea.verlif.lifeofdream.base;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;

/**
 * @param <K> 键值类型
 * @param <V> 值类型
 * @author Verlif
 */
public abstract class CanSavedMap<K, V extends CanSave> extends HashMap<K, V> implements CanSave {

    /**
     * 获取一个类型实例
     *
     * @param k 对应泛型K，键值
     * @return 类型实例
     */
    protected abstract V getNewValue(K k);

    @Override
    public boolean load(JSONObject json) {
        clear();
        if (json == null) {
            return false;
        }
        JSONArray array = json.getJSONArray("ar");
        int length = array.size();
        for (int i = 0; i < length; i++) {
            JSONObject j = array.getJSONObject(i);
            K k = (K) j.get("k");
            V v = getNewValue(k);
            v.load(j.getJSONObject("v"));
            put(k, v);
        }
        return true;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (K k : keySet()) {
            CanSave canSave = get(k);
            if (canSave != null) {
                JSONObject j = new JSONObject();
                j.put("k", k);
                j.put("v", canSave.save());
                array.add(j);
            }
        }
        json.put("ar", array);
        return json;
    }
}
