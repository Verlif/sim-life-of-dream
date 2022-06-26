package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.item.Item;

import java.util.Map;

/**
 * @author Verlif
 */
public class ItemManager implements CanSave {

    private static final ItemManager ITEM_MANAGER = new ItemManager();

    private final CanSavedMap<String, Item> itemMap;

    private ItemManager() {
        itemMap = new CanSavedMap<String, Item>() {
            @Override
            protected Item getNewValue(String s) {
                return new Item();
            }
        };
    }

    public static ItemManager getInstance() {
        return ITEM_MANAGER;
    }

    public Map<String, Item> getItemMap() {
        return itemMap;
    }

    public Item get(String key) {
        Item item = itemMap.get(key);
        if (item != null) {
            return item.copy();
        }
        return null;
    }

    public void add(Item item) {
        itemMap.put(item.getKey(), item);
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("im", itemMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        itemMap.clear();
        if (json == null) {
            return false;
        }
        return itemMap.load(json.getJSONObject("im"));
    }
}
