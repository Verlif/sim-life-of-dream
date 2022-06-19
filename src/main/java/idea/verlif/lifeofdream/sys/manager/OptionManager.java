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
        return allOptionMap.get(key);
    }

    public Set<Option> getOptionOfEvent(String key) {
        return allEventOptionMap.computeIfAbsent(key, k -> new HashSet<>());
    }

    public Map<String, Set<Option>> getAllEventOptionMap() {
        return allEventOptionMap;
    }

    public void addOptionToAll(Option option) {
        allOptionMap.put(option.getKey(), option);
        for (String event : option.getAfterEvents()) {
            Set<Option> set = allEventOptionMap.computeIfAbsent(event, k -> new HashSet<>());
            set.add(option);
        }
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("aom", allOptionMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        allOptionMap.clear();
        allEventOptionMap.clear();
        allOptionMap.load(json.getJSONObject("aom"));
        for (Option option : allOptionMap.values()) {
            for (String event : option.getAfterEvents()) {
                Set<Option> set = allEventOptionMap.computeIfAbsent(event, k -> new HashSet<>());
                set.add(option);
            }
        }
        return true;
    }
}
