package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.event.Option;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Verlif
 */
public class OptionManager implements CanSave {

    private static final OptionManager OPTION_MANAGER = new OptionManager();

    private final CanSavedMap<String, Option> allOptionMap;

    private final Map<String, Set<Option>> allEventOptionMap;

    private OptionManager() {
        allOptionMap = new CanSavedMap<String, Option>() {
            @Override
            protected Option getNewValue(String s) {
                return new Option();
            }
        };
        allEventOptionMap = new HashMap<>();
    }

    public static OptionManager getInstance() {
        return OPTION_MANAGER;
    }

    public Map<String, Option> getAllOptionMap() {
        return allOptionMap;
    }

    public Option getOption(String key) {
        Option option = allOptionMap.get(key);
        if (option != null) {
            return option.copy();
        }
        return null;
    }

    public Set<Option> getOptionOfEvent(String key) {
        Set<Option> set = allEventOptionMap.computeIfAbsent(key, k -> new HashSet<>());
        Set<Option> setCopy = new HashSet<>();
        for (Option option : set) {
            setCopy.add(option.copy());
        }
        return setCopy;
    }

    public Map<String, Set<Option>> getAllEventOptionMap() {
        return allEventOptionMap;
    }

    public void addOptionToAll(Option option) {
        allOptionMap.put(option.getKey(), option);
        for (String event : option.getFollowEvents()) {
            Set<Option> set = allEventOptionMap.computeIfAbsent(event, k -> new HashSet<>());
            set.add(option);
        }
    }

    public void clear() {
        allOptionMap.clear();
        allEventOptionMap.clear();
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("aom", allOptionMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        allOptionMap.clear();
        allEventOptionMap.clear();
        if (json == null) {
            return false;
        }
        allOptionMap.load(json.getJSONObject("aom"));
        for (Option option : allOptionMap.values()) {
            for (String event : option.getFollowEvents()) {
                Set<Option> set = allEventOptionMap.computeIfAbsent(event, k -> new HashSet<>());
                set.add(option);
            }
        }
        return true;
    }
}
