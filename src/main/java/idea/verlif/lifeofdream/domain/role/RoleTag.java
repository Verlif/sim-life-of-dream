package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.justsimmand.anno.SimmParam;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.game.GameRunner;
import idea.verlif.lifeofdream.sys.manager.TagManager;

import java.util.Map;

/**
 * @author Verlif
 */
public class RoleTag implements CanSave {

    private final CanSavedMap<String, Tag> tagMap;

    public RoleTag() {
        tagMap = new CanSavedMap<String, Tag>() {
            @Override
            protected Tag getNewValue(String s) {
                return new Tag();
            }
        };
    }

    public Map<String, Tag> getTagMap() {
        return tagMap;
    }

    public boolean has(String key) {
        return tagMap.containsKey(key);
    }

    public boolean hasno(String key) {
        return !tagMap.containsKey(key);
    }

    /**
     * 获取标签值
     *
     * @param key 标签Key
     * @return 标签值。当标签不存在时，返回-1。
     */
    public int value(String key) {
        Tag tag = tagMap.get(key);
        if (tag == null) {
            return -1;
        }
        return tag.value();
    }

    public void up(String key, @SimmParam(defaultVal = "1") int up) {
        Tag tag = tagMap.get(key);
        if (tag == null) {
            tag = add(key);
        }
        if (tag != null) {
            tag.up(up);
        }
    }

    public void down(String key, @SimmParam(defaultVal = "1") int down) {
        up(key, -down);
    }

    public boolean eq(String key, int value) {
        Tag tag = tagMap.get(key);
        if (tag != null) {
            return tag.eq(value);
        }
        return false;
    }

    public boolean bt(String key, int value) {
        Tag tag = tagMap.get(key);
        if (tag != null) {
            return tag.bt(value);
        }
        return false;
    }

    public boolean lt(String key, int value) {
        Tag tag = tagMap.get(key);
        if (tag != null) {
            return tag.lt(value);
        }
        return false;
    }

    public boolean ne(String key, int value) {
        Tag tag = tagMap.get(key);
        if (tag != null) {
            return tag.ne(value);
        }
        return false;
    }

    public Tag add(String key) {
        TagManager tagManager = TagManager.getInstance();
        Tag tag = tagManager.get(key);
        if (tag != null) {
            tagMap.put(key, tag);
            GameRunner gameRunner = GameRunner.getInstance();
            gameRunner.execCmd(tag.getOnAdd());
        }
        return tag;
    }

    public void remove(String key) {
        Tag tag = tagMap.remove(key);
        if (tag != null) {
            GameRunner gameRunner = GameRunner.getInstance();
            gameRunner.execCmd(tag.getOnRemove());
        }
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("tm", tagMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        return tagMap.load(json.getJSONObject("tm"));
    }
}
