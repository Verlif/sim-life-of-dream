package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.justsimmand.anno.SimmParam;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.game.GameRunner;
import idea.verlif.lifeofdream.sys.manager.ItemManager;

import java.util.Map;

/**
 * 角色背包
 *
 * @author Verlif
 */
public class Bag implements CanSave {

    private int size;

    private final CanSavedMap<String, Item> itemMap;

    public Bag() {
        itemMap = new CanSavedMap<String, Item>() {
            @Override
            protected Item getNewValue(String s) {
                return new Item();
            }
        };
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void size(int size) {
        this.size += size;
    }

    public Map<String, Item> getItemMap() {
        return itemMap;
    }

    public Item get(String key) {
        return itemMap.get(key);
    }

    /**
     * 背包中是否有足量的此道具
     *
     * @param key   道具Key
     * @return 是否有此道具。true - 有此道具；false - 没有此道具
     */
    public boolean has(String key) {
        return itemMap.containsKey(key);
    }

    public int value(String key) {
        Item item = itemMap.get(key);
        if (item == null) {
            return -1;
        }
        return item.value();
    }

    public boolean eq(String key, int value) {
        Item item = itemMap.get(key);
        if (item != null) {
            return item.eq(value);
        }
        return false;
    }

    public boolean bt(String key, int value) {
        Item item = itemMap.get(key);
        if (item != null) {
            return item.bt(value);
        }
        return false;
    }

    public boolean lt(String key, int value) {
        Item item = itemMap.get(key);
        if (item != null) {
            return item.lt(value);
        }
        return false;
    }

    public boolean ne(String key, int value) {
        Item item = itemMap.get(key);
        if (item != null) {
            return item.ne(value);
        }
        return false;
    }

    /**
     * 背包中是否没有此道具
     *
     * @param key 道具Key
     * @return 是否没有此道具。true - 没有此道具；false - 有此道具
     */
    public boolean hasno(String key) {
        return !itemMap.containsKey(key);
    }

    /**
     * 向背包中添加道具
     *
     * @param key   道具Key
     * @param count 道具数量。在指令中默认值为1。
     */
    public Item add(String key, @SimmParam(defaultVal = "1") int count) {
        Item item = itemMap.get(key);
        if (item == null) {
            ItemManager itemManager = ItemManager.getInstance();
            item = itemManager.get(key);
        }
        if (item != null) {
            item.up(count);
            itemMap.put(key, item);
            GameRunner gameRunner = GameRunner.getInstance();
            for (int i = 0; i < count; i++) {
                gameRunner.execCmd(item.getOnAdd());
            }
        }
        return item;
    }

    /**
     * 从背包移除道具
     *
     * @param key   道具Key
     * @param count 道具数量。在指令中默认值为所有。
     */
    public void remove(String key, @SimmParam(defaultVal = "1") int count) {
        Item item = itemMap.get(key);
        if (item != null) {
            if (count == -1 || item.lt(count)) {
                count = item.value();
            }
            item.down(count);
            GameRunner gameRunner = GameRunner.getInstance();
            for (int i = 0; i < count; i++) {
                gameRunner.execCmd(item.getOnRemove());
            }
            if (item.getValue() == 0) {
                itemMap.remove(key);
            }
        }
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("im", itemMap.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        size = json.getIntValue("size");
        return itemMap.load(json.getJSONObject("im"));
    }
}
