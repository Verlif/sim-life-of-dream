package idea.verlif.lifeofdream.domain.world;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.option.Option;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.sys.manager.OptionManager;
import idea.verlif.lifeofdream.sys.manager.RuleManager;

import java.util.Map;

/**
 * 世界信息
 *
 * @author Verlif
 */
public class World implements CanSave {

    /**
     * 每回合规则
     */
    private final CanSavedMap<String, Rule> ruleMap;

    private final CanSavedMap<String, Option> optionMap;

    public World() {
        ruleMap = new CanSavedMap<String, Rule>() {
            @Override
            protected Rule getNewValue(String s) {
                return new Rule();
            }
        };
        optionMap = new CanSavedMap<String, Option>() {
            @Override
            protected Option getNewValue(String s) {
                return new Option();
            }
        };
    }

    public Map<String, Rule> getRuleMap() {
        return ruleMap;
    }

    public CanSavedMap<String, Option> getOptionMap() {
        return optionMap;
    }

    public void addRule(String key) {
        RuleManager ruleManager = RuleManager.getInstance();
        Rule rule = ruleManager.get(key);
        if (rule != null) {
            ruleMap.put(key, rule);
        }
    }

    public void removeRule(String key) {
        ruleMap.remove(key);
    }

    public void addOption(String key) {
        Option option = OptionManager.getInstance().getOption(key);
        if (option != null) {
            optionMap.put(key, option);
        }
    }

    public void removeOption(String key) {
        optionMap.remove(key);
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("rot", ruleMap.save());
        json.put("opt", optionMap.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        ruleMap.load(json.getJSONObject("rot"));
        optionMap.load(json.getJSONObject("opt"));
        return true;
    }
}
