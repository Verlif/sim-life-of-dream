package idea.verlif.lifeofdream.domain.event;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedList;
import idea.verlif.lifeofdream.domain.option.Option;
import idea.verlif.lifeofdream.standard.Chancable;
import idea.verlif.lifeofdream.standard.Conditionable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 事件
 *
 * @author Verlif
 */
public class Event implements CanSave, Chancable, Conditionable {

    /**
     * 事件所属支线
     */
    private final List<String> followBranches;

    /**
     * 事件链接的事件
     */
    private final List<String> followEvents;

    /**
     * 事件key
     */
    private String key;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件描述
     */
    private String desc;

    /**
     * 事件触发打印消息
     */
    private String print;

    /**
     * 概率描述
     */
    private String chance;

    /**
     * 事件触发条件
     */
    private String condition;

    /**
     * 事件执行指令
     */
    private String exec;

    /**
     * 事件选项
     */
    private final CanSavedList<Option> options;

    /**
     * 可以触发的事件选项
     */
    private final CanSavedList<Option> readyOptions;

    /**
     * 剩余触发次数
     */
    private int remain = 1;

    /**
     * 是否已触发过
     */
    private boolean done;

    public Event() {
        followBranches = new ArrayList<>();
        followEvents = new ArrayList<>();
        options = new CanSavedList<Option>() {
            @Override
            protected Option getNewElement() {
                return new Option();
            }
        };
        readyOptions = new CanSavedList<Option>() {
            @Override
            protected Option getNewElement() {
                return new Option();
            }
        };
        done = false;
    }

    public List<String> getFollowBranches() {
        return followBranches;
    }

    public List<String> getFollowEvents() {
        return followEvents;
    }

    public String getKey() {
        if (key == null) {
            key = name;
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        if (desc == null && print != null) {
            desc = print;
        }
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrint() {
        if (print == null && desc != null) {
            print = desc;
        }
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    @Override
    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getExec() {
        return exec;
    }

    public void setExec(String exec) {
        this.exec = exec;
    }

    public List<Option> getOptions() {
        return options;
    }

    public List<Option> getReadyOptions() {
        return readyOptions;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        if (remain < -1) {
            remain = -1;
        }
        this.remain = remain;
    }

    public boolean enabled() {
        return remain != 0;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(getKey(), event.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("fb", followBranches);
        json.put("fe", followEvents);
        json.put("key", key);
        json.put("name", name);
        json.put("desc", desc);
        json.put("pri", print);
        json.put("cha", chance);
        json.put("con", condition);
        json.put("exec", exec);
        json.put("opts", options.save());
        json.put("rps", readyOptions.save());
        json.put("rem", remain);
        json.put("done", done);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        followBranches.clear();
        followEvents.clear();
        if (json == null) {
            return false;
        }
        if (json.containsKey("fb")) {
            followBranches.addAll(json.getList("fb", String.class));
        }
        if (json.containsKey("fe")) {
            followEvents.addAll(json.getList("fe", String.class));
        }
        key = json.getString("key");
        name = json.getString("name");
        desc = json.getString("desc");
        print = json.getString("pri");
        chance = json.getString("cha");
        condition = json.getString("con");
        exec = json.getString("exec");
        options.load(json.getJSONObject("opts"));
        readyOptions.load(json.getJSONObject("rps"));
        remain = json.getIntValue("rem");
        done = json.getBooleanValue("done");
        return true;
    }

    public Event copy() {
        Event event = new Event();
        event.load(save());
        return event;
    }
}
