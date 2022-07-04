package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.event.Event;

import java.util.*;

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
     * 事件链集。key - 事件key；value - 事件后续的所有事件
     */
    private final Map<String, Set<Event>> allLinkedEventMap;

    /**
     * 所有事件。key - 事件key；value - 事件
     */
    private final CanSavedMap<String, Event> allEventMap;

    private EventManager() {
        allBranchEventMap = new HashMap<>();
        allLinkedEventMap = new HashMap<>();
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
        Event raw = allEventMap.get(event.getKey());
        if (raw != null) {
            event.setRemain(raw.getRemain());
        }
        addEventToSet(event);
        allEventMap.put(event.getKey(), event);
    }

    private void addEventToSet(Event event) {
        if (event.getFollowBranches().size() == 0) {
            Set<Event> set = allBranchEventMap.computeIfAbsent(null, k -> new HashSet<>());
            set.add(event);
        } else {
            for (String branch : event.getFollowBranches()) {
                Set<Event> set = allBranchEventMap.computeIfAbsent(branch, k -> new HashSet<>());
                set.add(event);
            }
        }
        List<String> followEvents = event.getFollowEvents();
        if (followEvents.size() > 0) {
            for (String key : followEvents) {
                Set<Event> set = allLinkedEventMap.computeIfAbsent(key, k -> new HashSet<>());
                set.add(event);
            }
        }
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
        Set<Event> set = allBranchEventMap.computeIfAbsent(key, k -> new HashSet<>());
        Set<Event> setCopy = new HashSet<>();
        for (Event event : set) {
            setCopy.add(event.copy());
        }
        return setCopy;
    }

    public Set<Event> getEventOfEvent(String key) {
        Set<Event> set = allLinkedEventMap.computeIfAbsent(key, k -> new HashSet<>());
        Set<Event> setCopy = new HashSet<>();
        for (Event event : set) {
            setCopy.add(event.copy());
        }
        return setCopy;
    }

    /**
     * 获取当前的事件集
     *
     * @return 当前事件集
     */
    public Map<String, Event> getAllEventMap() {
        return allEventMap;
    }

    public void clear() {
        allBranchEventMap.clear();
        allLinkedEventMap.clear();
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
        allLinkedEventMap.clear();
        allBranchEventMap.clear();
        if (json == null) {
            return false;
        }
        allEventMap.load(json.getJSONObject("aem"));
        for (Event event : allEventMap.values()) {
            addEventToSet(event);
        }
        return true;
    }
}
