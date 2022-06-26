package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.event.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 事件集管理器
 *
 * @author Verlif
 */
public class EventManager implements CanSave {

    private static final EventManager EVENT_MANAGER = new EventManager();

    /**
     * 所有的事件集。key - 分支key；value - 分支附加的所有事件
     */
    private final Map<String, Set<Event>> allBranchEventMap;

    /**
     * 所有事件。key - 事件key；value - 事件
     */
    private final CanSavedMap<String, Event> allEventMap;

    private EventManager() {
        allBranchEventMap = new HashMap<>();
        allEventMap = new CanSavedMap<String, Event>() {
            @Override
            protected Event getNewValue(String s) {
                return new Event();
            }
        };
    }

    public static EventManager getInstance() {
        return EVENT_MANAGER;
    }

    /**
     * 向总事件集中添加事件
     *
     * @param event 事件对象
     */
    public void addEventToAll(Event event) {
        if (event.getAfterBranches().size() == 0) {
            Set<Event> set = allBranchEventMap.computeIfAbsent(null, k -> new HashSet<>());
            set.add(event);
        } else {
            for (String branch : event.getAfterBranches()) {
                Set<Event> set = allBranchEventMap.computeIfAbsent(branch, k -> new HashSet<>());
                set.add(event);
            }
        }
        allEventMap.put(event.getKey(), event);
    }

    public Event getEvent(String key) {
        Event event = allEventMap.get(key);
        if (event != null) {
            return event.copy();
        }
        return null;
    }

    public Event getRawEvent(String key) {
        return allEventMap.get(key);
    }

    public Set<Event> getEventOfBranch(String key) {
        return allBranchEventMap.get(key);
    }

    /**
     * 获取当前的事件集
     *
     * @return 当前事件集
     */
    public Map<String, Event> getAllEventMap() {
        return allEventMap;
    }

    /**
     * 重置事件集数据
     */
    public void reset() {
        allBranchEventMap.clear();
        allEventMap.clear();
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("aem", allEventMap.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        allEventMap.clear();
        allBranchEventMap.clear();
        if (json == null) {
            return false;
        }
        allEventMap.load(json.getJSONObject("aem"));
        for (Event event : allEventMap.values()) {
            for (String branch : event.getAfterBranches()) {
                Set<Event> set = allBranchEventMap.computeIfAbsent(branch, k -> new HashSet<>());
                set.add(event);
            }
        }
        return true;
    }
}
