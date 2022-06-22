package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.role.extra.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Verlif
 */
public class TagManager implements CanSave {

    private static final TagManager TAG_MANAGER = new TagManager();

    private final CanSavedMap<String, Tag> tagMap;

    private final Map<String, Set<String>> tagGroupMap;

    private TagManager() {
        tagMap = new CanSavedMap<String, Tag>() {
            @Override
            protected Tag getNewValue(String s) {
                return new Tag();
            }
        };
        tagGroupMap = new HashMap<>();
    }

    public static TagManager getInstance() {
        return TAG_MANAGER;
    }

    public Map<String, Tag> getTagMap() {
        return tagMap;
    }

    /**
     * 获取标签Key下的所有标签，包括子分组
     *
     * @param key 标签key
     * @return 标签集
     */
    public Set<Tag> get(String key) {
        Set<Tag> set = new HashSet<>();
        Tag tag = tagMap.get(key);
        if (tag != null) {
            set.add(tag.copy());
        }
        Set<String> keys = tagGroupMap.get(key);
        if (keys != null) {
            for (String s : keys) {
                tag = tagMap.get(s);
                if (tag != null) {
                    set.add(tag.copy());
                }
            }
        }
        return set;
    }

    public Tag getOfKey(String key) {
        Tag tag = tagMap.get(key);
        if (tag != null) {
            return tag.copy();
        }
        return null;
    }

    public boolean isGroup(String key) {
        return !tagGroupMap.containsKey(key);
    }

    public Set<String> getGroup(String key) {
        return tagGroupMap.get(key);
    }

    public void addTag(Tag tag) {
        String key = tag.getKey();
        tagMap.put(key, tag);
        addKeyToGroup(key);
    }

    private void addKeyToGroup(String key) {
        // 分组确定
        String[] ss = key.split("\\.");
        if (ss.length > 1) {
            String k = ss[0];
            Set<String> top = tagGroupMap.computeIfAbsent(k, k1 -> new HashSet<>());
            top.add(key);
            for (int i = 1; i < ss.length - 1; i++) {
                k = k + "." + ss[i];
                Set<String> set = tagGroupMap.computeIfAbsent(k, k1 -> new HashSet<>());
                set.add(key);
            }
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
        tagMap.clear();
        tagMap.load(json.getJSONObject("tm"));
        for (String key : tagMap.keySet()) {
            addKeyToGroup(key);
        }
        return true;
    }
}
