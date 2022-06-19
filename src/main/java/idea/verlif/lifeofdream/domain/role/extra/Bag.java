package idea.verlif.lifeofdream.domain.role.extra;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.justsimmand.anno.SimmParam;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.sys.exec.ExecRunner;
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
     * 背包中是否有足量的此物品
     *
     * @param key   物品Key
     * @param count 物品数量。在指令中默认值为1。
     * @return 是否有此物品。true - 有此物品；false - 没有此物品
     */
    public boolean has(String key, @SimmParam(defaultVal = "1") int count) {
        Item item = itemMap.get(key);
        if (item == null) {
            return count == 0;
        }
        return item.getValue() >= count;
    }

    /**
     * 背包中是否没有此物品
     *
     * @param key 物品Key
     * @return 是否没有此物品。true - 没有此物品；false - 有此物品
     */
    public boolean hasno(String key) {
        return !itemMap.containsKey(key);
    }

    /**
     * 向背包中添加物品
     *
     * @param key   物品Key
     * @param count 物品数量。在指令中默认值为1。
     */
    public void add(String key, @SimmParam(defaultVal = "1") int count) {
        Item item = itemMap.get(key);
        if (item == null) {
            ItemManager itemManager = ItemManager.getInstance();
            item = itemManager.get(key);
        }
        if (item != null) {
            item.up(count);
            itemMap.put(key, item);
            ExecRunner execRunner = ExecRunner.getInstance();
            for (int i = 0; i < count; i++) {
                execRunner.execCmd(item.getOnAdd());
            }
        }
    }

    /**
     * 从背包移除物品
     *
     * @param key   物品Key
     * @param count 物品数量。在指令中默认值为所有。
     */
    public void remove(String key, @SimmParam(defaultVal = "-1") int count) {
        Item item = itemMap.get(key);
        if (item != null) {
            if (count == -1 || item.lt(count)) {
                count = item.value();
            }
            item.down(count);
            ExecRunner execRunner = ExecRunner.getInstance();
            for (int i = 0; i < count; i++) {
                execRunner.execCmd(item.getOnRemove());
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
