package idea.verlif.lifeofdream.sys.exec;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.justsimmand.SimmandManager;
import idea.verlif.justsimmand.anno.SimmParam;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedList;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.branch.Branch;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.domain.story.Story;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.sys.kit.Kit;
import idea.verlif.lifeofdream.sys.kit.KitImpl;
import idea.verlif.lifeofdream.sys.manager.BranchManager;
import idea.verlif.lifeofdream.sys.manager.EventManager;
import idea.verlif.lifeofdream.sys.manager.OptionManager;
import idea.verlif.lifeofdream.util.DescUtil;
import idea.verlif.parser.vars.VarsContext;
import idea.verlif.parser.vars.VarsHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 指令执行器
 *
 * @author Verlif
 */
public class ExecRunner implements CanSave {

    private static final ExecRunner EXEC_RUNNER = new ExecRunner();

    private final SimmandManager simmandManager;

    /**
     * 故事数据
     */
    private Story story;

    /**
     * 角色数据
     */
    private Role role;

    /**
     * 世界数据
     */
    private World world;

    /**
     * 工具
     */
    private Kit kit;

    /**
     * 事件管理器
     */
    private final EventManager eventManager;

    /**
     * 事件选项管理器
     */
    private final OptionManager optionManager;

    /**
     * 分支管理器
     */
    private final BranchManager branchManager;

    /**
     * 当前需要触发的事件列表
     */
    private final CanSavedList<Event> readyEvents;

    /**
     * 下回合触发的事件列表
     */
    private final CanSavedList<Event> preEvents;

    /**
     * 进行中的分支线
     */
    private final CanSavedMap<String, Branch> branchMap;

    /**
     * 是否结束
     */
    private boolean finish;

    private final VarsContext varsContext;
    private final AttrHandler attrHandler;

    private ExecRunner() {
        this.eventManager = EventManager.getInstance();
        this.optionManager = OptionManager.getInstance();
        this.branchManager = BranchManager.getInstance();
        this.simmandManager = new SimmandManager();
        this.readyEvents = new CanSavedList<Event>() {
            @Override
            protected Event getNewElement() {
                return new Event();
            }
        };
        this.preEvents = new CanSavedList<Event>() {
            @Override
            protected Event getNewElement() {
                return new Event();
            }
        };
        this.branchMap = new CanSavedMap<String, Branch>() {
            @Override
            protected Branch getNewValue(String s) {
                return new Branch();
            }
        };
        this.kit = new KitImpl();

        this.varsContext = new VarsContextLocal();
        this.attrHandler = new AttrHandler();
    }

    public static ExecRunner getInstance() {
        return EXEC_RUNNER;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    public Role getRole() {
        return role;
    }

    public World getWorld() {
        return world;
    }

    /**
     * 重置执行器
     *
     * @param role  角色数据
     * @param world 世界数据
     */
    public void init(Role role, World world) {
        this.role = role;
        this.world = world;
        readyEvents.clear();
        preEvents.clear();
        branchMap.clear();
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public List<Event> getPreEvents() {
        return preEvents;
    }

    public List<Event> getReadyEvents() {
        return readyEvents;
    }

    public Map<String, Branch> getBranchMap() {
        return branchMap;
    }

    /**
     * 开始游戏
     *
     * @return 开始结果
     */
    public Result start() {
        if (story == null) {
            return Result.fail("No story!");
        }
        kit.message(story.getDesc());
        return execCmd(story.getExec());
    }

    /**
     * 跳过事件
     */
    public void skipEvent() {
        if (readyEvents.size() > 0) {
            Event event = readyEvents.get(0);
            event.getReadyOptions().clear();
            readyEvents.remove(0);
        }
    }

    /**
     * 执行事件选项
     *
     * @param key 选项Key
     * @return 执行结果
     */
    public Result execOption(String key) {
        if (readyEvents.size() == 0) {
            return Result.fail("No event can be executed!");
        }
        Event event = readyEvents.get(0);
        for (Option option : event.getReadyOptions()) {
            if (option.getKey().equals(key)) {
                kit.message(option.getDesc());
                skipEvent();
                return execCmd(option.getExec());
            }
        }
        return Result.fail("No such option!");
    }

    /**
     * 获得下一个需要触发的事件
     *
     * @return 下一个需要触发的事件
     */
    public Event nextEvent() {
        if (readyEvents.size() == 0) {
            return null;
        }
        Event event = readyEvents.get(0);
        if (event.isDone()) {
            return event;
        }
        execCmd(event.getExec());
        event.setRemain(event.getRemain() - 1);
        if (event.getOptions().size() == 0) {
            event.getOptions().addAll(optionManager.getOptionOfEvent(event.getKey()));
        }
        if (event.getReadyOptions().size() == 0) {
            OptionManager optionManager = OptionManager.getInstance();
            // 进行选项判定
            Set<Option> allSet = optionManager.getOptionOfEvent(event.getKey());
            Random random = new Random();
            for (Option option : allSet) {
                if (DescUtil.test(varsContext.build(option.getCondition(), attrHandler))) {
                    int chance = DescUtil.result(varsContext.build(option.getChance(), attrHandler));
                    if (chance > random.nextInt(10000)) {
                        event.getReadyOptions().add(option);
                    }
                }
            }
        }
        return event;
    }

    /**
     * 下一回合
     *
     * @return 结果
     */
    public Result nextTurn() {
        readyEvents.clear();
        if (finish) {
            preEvents.clear();
            return Result.fail("Finish!");
        }
        readyEvents.addAll(preEvents);
        // 每回合规则触发
        for (Rule rule : world.getRuleOfTurn().values()) {
            execCmd(rule.getExec());
        }
        Random random = new Random();
        // 分支触发
        for (Branch branch : branchManager.getBranchMap().values()) {
            if (!branchMap.containsKey(branch.getKey())
                    && DescUtil.test(varsContext.build(branch.getCondition(), attrHandler))
                    && DescUtil.result(varsContext.build(branch.getChance(), attrHandler)) > random.nextInt(10000)) {
                addBranch(branch.getKey());
            }
        }
        Set<String> allKeys = new HashSet<>(branchMap.keySet());
        // 向readyEvents中添加需要触发的事件
        // 添加分支的事件
        for (Branch branch : branchMap.values()) {
            Set<Event> set = eventManager.getEventOfBranch(branch.getKey());
            if (set != null) {
                for (Event event : set) {
                    // 避免事件重复
                    if (event.getRemain() > 0 && !allKeys.contains(event.getKey())
                            && DescUtil.test(varsContext.build(event.getCondition(), attrHandler))
                            && DescUtil.result(varsContext.build(event.getChance(), attrHandler)) > random.nextInt(10000)) {
                        allKeys.add(event.getKey());
                        readyEvents.add(event);
                    }
                }
            }
        }
        // 添加全局的事件
        Set<Event> set = eventManager.getEventOfBranch(null);
        if (set != null) {
            for (Event event : set) {
                // 避免事件重复
                if (event.getRemain() > 0 && !allKeys.contains(event.getKey())
                        && DescUtil.test(varsContext.build(event.getCondition(), attrHandler))
                        && DescUtil.result(varsContext.build(event.getChance(), attrHandler)) > random.nextInt(10000)) {
                    allKeys.add(event.getKey());
                    readyEvents.add(event);
                }
            }
        }
        return Result.ok(null);
    }

    /**
     * 添加分支
     *
     * @param key 分支Key
     */
    public void addBranch(String key) {
        Branch branch = branchManager.getBranch(key);
        if (branch != null && !branchMap.containsKey(key)) {
            execCmd(branch.getExec());
            branchMap.put(key, branch);
        }
    }

    /**
     * 移除分支
     *
     * @param key 分支Key
     */
    public void removeBranch(String key) {
        branchMap.remove(key);
    }

    /**
     * 向当前需要触发的事件列表中添加事件
     *
     * @param key   事件key
     * @param index 添加到的序号
     */
    public void addEventToReady(String key, @SimmParam(defaultVal = "-1") int index) {
        Event event = eventManager.getEvent(key);
        if (event != null) {
            if (index == -1 || index > readyEvents.size()) {
                readyEvents.add(event);
            } else {
                readyEvents.add(index, event);
            }
        }
    }

    /**
     * 向下回合需要触发的事件列表中添加事件
     *
     * @param key   事件key
     * @param index 添加到的序号
     */
    public void addEventToPre(String key, @SimmParam(defaultVal = "-1") int index) {
        Event event = eventManager.getEvent(key);
        if (event != null) {
            if (index == -1 || index > readyEvents.size()) {
                preEvents.add(event);
            } else {
                preEvents.add(index, event);
            }
        }
    }

    /**
     * 使用背包中的物品
     *
     * @param key 物品Key
     */
    public void useItem(String key) {
        if (role != null) {
            Item item = role.getBag().get(key);
            if (item != null) {
                execCmd(item.getOnUse());
            }
        }
    }

    /**
     * 游戏结束
     */
    public void finish() {
        this.finish = true;
    }

    /**
     * 执行指令集
     *
     * @param cmd 指令
     */
    public Result execCmd(String cmd) {
        if (cmd == null || cmd.trim().length() == 0) {
            return Result.ok("No cmd");
        }
        String[] ss = cmd.split(";");
        Object[] data = new Object[ss.length];
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            String line = s.trim().replace("\n", "");
            data[i] = invoke(line);
        }
        return Result.ok(data);
    }

    /**
     * 执行单条指令
     *
     * @param desc 指令描述
     * @return 指令结果
     */
    public Object invoke(String desc) {
        String[] ss = desc.split(" ");
        String[] keys = ss[0].split("\\.");
        if (keys.length == 1) {
            simmandManager.add(this);
            StringBuilder sb = new StringBuilder();
            sb.append(keys[keys.length - 1]).append(" ");
            for (int i = 1; i < ss.length; i++) {
                sb.append(ss[i]).append(" ");
            }
            try {
                return simmandManager.run(sb.toString());
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        try {
            Object o = this;
            Field value = this.getClass().getDeclaredField(keys[0]);
            for (int i = 1; i < keys.length - 1; i++) {
                o = value.get(o);
                String key = keys[i];
                Class<?> cl = o.getClass();
                Field field = cl.getDeclaredField(key);
                field.setAccessible(true);
                value = field;
            }
            o = value.get(o);
            simmandManager.add(o);
            StringBuilder sb = new StringBuilder();
            sb.append(keys[keys.length - 1]).append(" ");
            for (int i = 1; i < ss.length; i++) {
                sb.append(ss[i]).append(" ");
            }
            return simmandManager.run(sb.toString());
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        if (story != null) {
            json.put("sto", story.save());
        }
        if (role != null) {
            json.put("role", role.save());
        }
        if (world != null) {
            json.put("wor", world.save());
        }
        json.put("res", readyEvents.save());
        json.put("pes", preEvents.save());
        json.put("bm", branchMap.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        story = new Story();
        story.load(json.getJSONObject("sto"));
        role = new Role();
        role.load(json.getJSONObject("role"));
        world = new World();
        world.load(json.getJSONObject("wor"));
        readyEvents.clear();
        readyEvents.load(json.getJSONObject("res"));
        preEvents.clear();
        preEvents.load(json.getJSONObject("pes"));
        branchMap.clear();
        branchMap.load(json.getJSONObject("bm"));
        return true;
    }

    private static class VarsContextLocal extends VarsContext {

        @Override
        public String build(String context, VarsHandler handler) {
            if (context == null) {
                return null;
            }
            return super.build(context, handler);
        }
    }

    private class AttrHandler implements VarsHandler {

        @Override
        public String handle(int i, String s, String s1) {
            return invoke(s1).toString();
        }
    }
}
