package idea.verlif.enabletheworld.module.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import idea.verlif.enabletheworld.utils.PrintUtil;

public abstract class CanSavedList<E extends CanSave> extends ArrayList<E> implements CanSave {

    protected abstract E getNewElement();

    @Override
    public boolean load(JSONObject json) {
        clear();
        try {
            JSONArray array = json.getJSONArray("array");
            int length = array.size();
            for (int i = 0; i < length; i++) {
                E e = getNewElement();
                e.load(array.getJSONObject(i));
                add(e);
            }
        } catch (JSONException e) {
            PrintUtil.printException(e);
        }
        return false;
    }

    @Override
    public JSONObject toSave() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (E e : this) {
            array.add(e.toSave());
        }
        try {
            json.put("array", array);
        } catch (JSONException e) {
            PrintUtil.printException(e);
        }
        return json;
    }
}
