package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.rule.Rule;

import java.util.Map;

/**
 * 规则管理器
 *
 * @author Verlif
 */
public class RuleManager implements CanSave {

    private static final RuleManager RULE_MANAGER = new RuleManager();

    private final CanSavedMap<String, Rule> ruleMap;

    private RuleManager() {
        ruleMap = new CanSavedMap<String, Rule>() {
            @Override
            protected Rule getNewValue(String s) {
                return new Rule();
            }
        };
    }

    public static RuleManager getInstance() {
        return RULE_MANAGER;
    }

    public Map<String, Rule> getRuleMap() {
        return ruleMap;
    }

    public void add(Rule rule) {
        ruleMap.put(rule.getKey(), rule);
    }

    public Rule get(String key) {
        return ruleMap.get(key);
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("rm", ruleMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        ruleMap.clear();
        return ruleMap.load(json.getJSONObject("rm"));
    }
}
