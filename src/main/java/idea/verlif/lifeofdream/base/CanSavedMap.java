package idea.verlif.enabletheworld.module.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import idea.verlif.enabletheworld.utils.PrintUtil;

public abstract class CanSavedMap<K, V extends CanSave> extends HashMap<K, V> implements CanSave {

    protected abstract V getNewValue(K k);

    @Override
    public boolean load(JSONObject json) {
        clear();
        try {
            JSONArray array = json.getJSONArray("array");
            int length = array.size();
            for (int i = 0; i < length; i++) {
                JSONObject j = array.getJSONObject(i);
                K k = (K) j.get("key");
                V v = getNewValue(k);
                v.load(j.getJSONObject("val"));
                put(k, v);
            }
        } catch (JSONException e) {
            PrintUtil.printException(e);
        }
        return false;
    }

    @Override
    public JSONObject toSave() {
        JSONObject json = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (K k : keySet()) {
                CanSave canSave = get(k);
                if (canSave != null) {
                    JSONObject j = new JSONObject();
                    j.put("key", k);
                    j.put("val", canSave.toSave());
                    array.add(j);
                }
            }
            json.put("array", array);
        } catch (JSONException e) {
            PrintUtil.printException(e);
        }
        return json;
    }
}
