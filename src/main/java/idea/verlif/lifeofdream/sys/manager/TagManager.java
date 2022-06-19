package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.role.extra.Tag;

import java.util.Map;

/**
 * @author Verlif
 */
public class TagManager implements CanSave {

    private static final TagManager TAG_MANAGER = new TagManager();

    private final CanSavedMap<String, Tag> tagMap;

    private TagManager() {
        tagMap = new CanSavedMap<String, Tag>() {
            @Override
            protected Tag getNewValue(String s) {
                return new Tag();
            }
        };
    }

    public static TagManager getInstance() {
        return TAG_MANAGER;
    }

    public Map<String, Tag> getTagMap() {
        return tagMap;
    }

    public Tag getTag(String key) {
        return tagMap.get(key);
    }

    public void addTag(Tag tag) {
        tagMap.put(tag.getKey(), tag);
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
        return tagMap.load(json.getJSONObject("tm"));
    }
}
