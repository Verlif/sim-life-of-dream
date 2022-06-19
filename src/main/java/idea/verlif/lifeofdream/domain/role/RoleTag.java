package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.sys.exec.ExecRunner;
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

    public void add(String key) {
        TagManager tagManager = TagManager.getInstance();
        Tag tag = tagManager.getTag(key);
        if (tag != null) {
            tagMap.put(key, tag);
            ExecRunner execRunner = ExecRunner.getInstance();
            execRunner.execCmd(tag.getOnAdd());
        }
    }

    public void remove(String key) {
        Tag tag = tagMap.remove(key);
        if (tag != null) {
            ExecRunner execRunner = ExecRunner.getInstance();
            execRunner.execCmd(tag.getOnRemove());
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
