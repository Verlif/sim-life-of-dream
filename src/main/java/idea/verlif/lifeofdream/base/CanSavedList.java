package idea.verlif.lifeofdream.base;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;

/**
 * @author Verlif
 * @param <E> 值类型
 */
public abstract class CanSavedList<E extends CanSave> extends ArrayList<E> implements CanSave {

    /**
     * 获取一个类型实例
     *
     * @return 类型实例
     */
    protected abstract E getNewElement();

    @Override
    public boolean load(JSONObject json) {
        clear();
        if (json == null) {
            return false;
        }
        JSONArray array = json.getJSONArray("ar");
        int length = array.size();
        for (int i = 0; i < length; i++) {
            E e = getNewElement();
            e.load(array.getJSONObject(i));
            add(e);
        }
        return false;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (E e : this) {
            array.add(e.save());
        }
        json.put("ar", array);
        return json;
    }
}
