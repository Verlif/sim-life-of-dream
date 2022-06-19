package idea.verlif.lifeofdream.domain.world;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.rule.Rule;
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
    private final CanSavedMap<String, Rule> ruleOfTurn;

    public World() {
        ruleOfTurn = new CanSavedMap<String, Rule>() {
            @Override
            protected Rule getNewValue(String s) {
                return new Rule();
            }
        };
    }

    public Map<String, Rule> getRuleOfTurn() {
        return ruleOfTurn;
    }

    public void addRuleOfTurn(String key) {
        RuleManager ruleManager = RuleManager.getInstance();
        Rule rule = ruleManager.get(key);
        if (rule != null) {
            ruleOfTurn.put(key, rule);
        }
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("rot", ruleOfTurn.save());
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        return ruleOfTurn.load(json.getJSONObject("rot"));
    }
}
