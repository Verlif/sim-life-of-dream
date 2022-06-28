package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.game.GameRunner;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.sys.manager.TagManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public void up(String key, int up) {
        TagManager tagManager = TagManager.getInstance();
        Set<String> set = tagManager.getGroup(key);
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(key);
        for (String k : set) {
            Tag tag = tagMap.get(k);
            if (tag == null) {
                tag = addOne(k);
                if (tag != null) {
                    tag.up(up);
                }
            } else {
                tag.up(up);
            }
        }
    }

    public void down(String key, int down) {
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

    /**
     * 添加标签及标签组
     *
     * @param key 标签或标签组key
     */
    public void add(String key) {
        Set<Tag> tags = TagManager.getInstance().get(key);
        for (Tag tag : tags) {
            if (tag != null && !tagMap.containsKey(tag.getKey())) {
                tagMap.put(tag.getKey(), tag);
                NoticeRunner.notice(Tip.TAG_ADDED);
                GameRunner gameRunner = GameRunner.getInstance();
                gameRunner.execCmd(tag.getOnAdd());
            }
        }
    }

    public Tag addOne(String key) {
        Tag tag = TagManager.getInstance().getOfKey(key);
        if (tag != null && !tagMap.containsKey(key)) {
            tagMap.put(tag.getKey(), tag);
            NoticeRunner.notice(Tip.TAG_ADDED);
            GameRunner gameRunner = GameRunner.getInstance();
            gameRunner.execCmd(tag.getOnAdd());
        }
        return tag;
    }

    /**
     * 移除标签及标签组
     *
     * @param key 标签或标签组key
     */
    public void remove(String key) {
        // 移除完整key标签
        removeOne(key);
        // 移除标签组
        TagManager tagManager = TagManager.getInstance();
        Set<String> set = tagManager.getGroup(key);
        // 不是分组
        if (set != null) {
            for (String s : set) {
                Tag tag = tagMap.remove(s);
                if (tag != null) {
                    NoticeRunner.notice(Tip.TAG_REMOVED);
                    GameRunner gameRunner = GameRunner.getInstance();
                    gameRunner.execCmd(tag.getOnRemove());
                }
            }
        }
    }

    public void removeOne(String key) {
        // 移除完整key标签
        Tag t = tagMap.remove(key);
        if (t != null) {
            NoticeRunner.notice(Tip.TAG_REMOVED);
            GameRunner gameRunner = GameRunner.getInstance();
            gameRunner.execCmd(t.getOnRemove());
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
