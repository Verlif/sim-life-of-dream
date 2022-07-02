package idea.verlif.lifeofdream.game;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.justsimmand.SimmandManager;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedList;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.branch.Branch;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.option.Option;
import idea.verlif.lifeofdream.domain.option.OptionResult;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.role.extra.Skill;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.domain.story.Story;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.pack.Pack;
import idea.verlif.lifeofdream.pack.PackManager;
import idea.verlif.lifeofdream.standard.Chancable;
import idea.verlif.lifeofdream.standard.Conditionable;
import idea.verlif.lifeofdream.sys.kit.Kit;
import idea.verlif.lifeofdream.sys.kit.MessageKit;
import idea.verlif.lifeofdream.sys.manager.*;
import idea.verlif.lifeofdream.tool.ChanceRandom;
import idea.verlif.lifeofdream.util.DescUtil;
import idea.verlif.parser.vars.VarsContext;
import idea.verlif.parser.vars.VarsHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 指令执行器
 *
 * @author Verlif
 */
public class GameRunner implements CanSave {

    private static final GameRunner EXEC_RUNNER = new GameRunner();

    private final SimmandManager simmandManager;

    /**
     * 故事数据
     */
    private final List<Story> stories;

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
    private final Kit kit;

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

    private Game game;

    private final VarsContext varsContext;
    private final AttrHandler attrHandler;
    private final Random random;

    private GameRunner() {
        this.eventManager = EventManager.getInstance();
        this.optionManager = OptionManager.getInstance();
        this.branchManager = BranchManager.getInstance();
        this.simmandManager = new SimmandManager();
        this.stories = new ArrayList<>();
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
        this.kit = new Kit() {
            @Override
            public void message(String message) {
                super.message(tran(message));
            }
        };

        this.varsContext = new VarsContextLocal();
        this.attrHandler = new AttrHandler();
        this.random = new Random();
    }

    public static GameRunner getInstance() {
        return EXEC_RUNNER;
    }

    public void addStory(Story... stories) {
        Collections.addAll(this.stories, stories);
    }

    public List<Story> getStories() {
        return stories;
    }

    public Role getRole() {
        return role;
    }

    public World getWorld() {
        return world;
    }

    public Kit getKit() {
        return kit;
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

    public void setMessageKit(MessageKit kit) {
        this.kit.setMessageKit(kit);
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
    public Result start(Game game) {
        this.game = game;
        // 加载游戏
        if (!load(game)) {
            return Result.fail("No story!");
        }
        if (game.getProcess() == 1) {
            return Result.ok(null);
        } else if (game.getProcess() == 2) {
            return Result.fail("It's finished!");
        }
        init(game.getRole(), game.getWorld());
        for (Story story : stories) {
            kit.message(story.getDesc());
            execCmd(story.getExec());
        }
        game.setProcess(1);
        return Result.ok("Start!");
    }

    private boolean load(Game game) {
        Set<Branch> branches = new HashSet<>();
        Set<Event> events = new HashSet<>();
        Set<Item> items = new HashSet<>();
        Set<Option> options = new HashSet<>();
        Set<Rule> rules = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        Set<Skill> skills = new HashSet<>();
        PackManager pm = PackManager.getInstance();
        stories.clear();
        for (String key : game.getPacks()) {
            Pack pack = pm.get(key);
            if (pack != null) {
                if (pack.getStory() != null) {
                    stories.add(pack.getStory());
                }
                branches.addAll(pack.getBranches());
                events.addAll(pack.getEvents());
                items.addAll(pack.getItems());
                options.addAll(pack.getOptions());
                rules.addAll(pack.getRules());
                tags.addAll(pack.getTags());
                skills.addAll(pack.getSkills());
            }
        }
        if (stories.size() == 0) {
            return false;
        }
        BranchManager bm = BranchManager.getInstance();
        bm.getBranchMap().clear();
        for (Branch branch : branches) {
            bm.addBranch(branch);
        }
        EventManager em = EventManager.getInstance();
        em.getAllEventMap().clear();
        for (Event event : events) {
            em.addEventToAll(event);
        }
        OptionManager om = OptionManager.getInstance();
        om.getAllOptionMap().clear();
        for (Option option : options) {
            om.addOptionToAll(option);
        }
        ItemManager im = ItemManager.getInstance();
        im.getItemMap().clear();
        for (Item item : items) {
            im.add(item);
        }
        RuleManager rm = RuleManager.getInstance();
        rm.getRuleMap().clear();
        for (Rule rule : rules) {
            rm.add(rule);
        }
        TagManager tm = TagManager.getInstance();
        tm.getTagMap().clear();
        for (Tag tag : tags) {
            tm.addTag(tag);
        }
        SkillManager sm = SkillManager.getInstance();
        sm.getSkillMap().clear();
        for (Skill skill : skills) {
            sm.add(skill);
        }
        return true;
    }

    /**
     * 跳过事件
     */
    public void skipEvent() {
        if (readyEvents.size() > 0) {
            Event event = readyEvents.get(0);
            event.getReadyOptions().clear();
            readyEvents.remove(0);
            // 添加跟随事件
            Set<Event> events = eventManager.getEventOfEvent(event.getKey());
            if (events != null) {
                for (Event e : events) {
                    if (testCondition(event) && randomChance(event)) {
                        readyEvents.add(e);
                    }
                }
            }
        }
    }

    /**
     * 是否可以执行选项
     *
     * @param key 选项key
     * @return 是否条件满足
     */
    public boolean canExecOption(String key) {
        Option option = optionManager.getOption(key);
        if (option == null) {
            return false;
        }
        return testCondition(option);
    }

    /**
     * 执行世界选项
     *
     * @param key 选项key
     * @return 执行结果
     */
    public Result execOptionOfWorld(String key) {
        Option option = world.getOptionMap().get(key);
        if (option != null) {
            if (testCondition(option)) {
                return invokeOption(option);
            } else {
                return Result.fail("Can't execute the option");
            }
        } else {
            return Result.fail("No such option!");
        }
    }

    /**
     * 执行当前事件选项。执行后，事件会自动移除。
     *
     * @param key 选项Key
     * @return 执行结果
     */
    public Result execOptionOfEvent(String key) {
        if (readyEvents.size() == 0) {
            return Result.fail("No event can be executed!");
        }
        Event event = readyEvents.get(0);
        for (Option option : event.getReadyOptions()) {
            if (option.getKey().equals(key)) {
                // 跳过此事件
                skipEvent();
                return invokeOption(option);
            }
        }
        return Result.fail("No such option!");
    }

    /**
     * 执行背包中道具的选项。
     *
     * @param itemKey 道具Key
     * @param optKey  选项Key
     * @return 执行结果
     */
    public Result execOptionOfItem(String itemKey, String optKey) {
        Item item = role.getBag().get(itemKey);
        if (item == null) {
            return Result.fail("No such item in bag!");
        }
        for (Option option : item.getOptions()) {
            if (option.getKey().equals(optKey) && testCondition(option)) {
                return invokeOption(option);
            }
        }
        return Result.fail("No such option!");
    }

    private Result invokeOption(Option option) {
        kit.message(option.getPrint());
        // 效果选择
        List<OptionResult> results = option.getResultList();
        List<OptionResult> readyResults = new ArrayList<>();
        for (OptionResult result : results) {
            if (testCondition(result)) {
                readyResults.add(result);
            }
        }
        ChanceRandom<OptionResult> random = new ChanceRandom<>();
        for (OptionResult readyResult : readyResults) {
            random.add(DescUtil.result(tran(readyResult.getChance())), readyResult);
        }
        OptionResult result = random.random();
        if (result != null) {
            kit.message(result.getDesc());
            // 执行结果
            return execCmd(result.getExec());
        } else {
            return Result.ok("ok");
        }
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
        kit.message(event.getPrint());
        execCmd(event.getExec());
        event.setDone(true);
        // 降低事件剩余次数
        Event rawEvent = eventManager.getRawEvent(event.getKey());
        if (rawEvent != null && rawEvent.getRemain() > 0) {
            rawEvent.setRemain(event.getRemain() - 1);
        }
        if (event.getOptions().size() == 0) {
            event.getOptions().addAll(optionManager.getOptionOfEvent(event.getKey()));
        }
        if (event.getReadyOptions().size() == 0) {
            OptionManager optionManager = OptionManager.getInstance();
            // 进行选项判定
            Set<Option> allSet = optionManager.getOptionOfEvent(event.getKey());
            for (Option option : allSet) {
                if (testCondition(option) && randomChance(option)) {
                    event.getReadyOptions().add(option);
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
        if (game.getProcess() == 0) {
            return Result.fail("Not start!");
        } else if (game.getProcess() == 2) {
            return Result.fail("Finish!");
        }
        if (readyEvents.size() > 0) {
            return Result.fail("Can't turn to next with events!");
        }
        readyEvents.addAll(preEvents);
        // 每回合规则触发
        for (Rule rule : world.getRuleMap().values()) {
            if (testCondition(rule) && randomChance(rule)) {
                execCmd(rule.getExec());
            }
        }
        for (Tag tag : role.getTag().getTagMap().values()) {
            execCmd(tag.getOnTurn());
        }
        // 分支触发
        for (Branch branch : branchManager.getBranchMap().values()) {
            if (!branchMap.containsKey(branch.getKey())
                    && testCondition(branch)
                    && randomChance(branch)) {
                addBranch(branch.getKey());
            }
        }
        Set<String> allKeys = readyEvents.stream().map(Event::getKey).collect(Collectors.toSet());
        // 向readyEvents中添加需要触发的事件
        // 添加分支的事件
        List<Event> list = new ArrayList<>();
        for (Branch branch : branchMap.values()) {
            Set<Event> set = eventManager.getEventOfBranch(branch.getKey());
            if (set != null) {
                for (Event event : set) {
                    // 避免事件重复
                    if (event.enabled() && !allKeys.contains(event.getKey())
                            && testCondition(event)
                            && randomChance(event)) {
                        allKeys.add(event.getKey());
                        list.add(event);
                    }
                }
                if (list.size() > 1) {
                    addReadyEvent(list.get(random.nextInt(list.size())));
                } else if (list.size() == 1) {
                    addReadyEvent(list.get(0));
                }
                list.clear();
            }
        }
        // 添加全局的事件
        Set<Event> set = eventManager.getEventOfBranch(null);
        if (set != null) {
            for (Event event : set) {
                // 避免事件重复
                if (event.enabled() && !allKeys.contains(event.getKey())
                        && testCondition(event)
                        && randomChance(event)) {
                    allKeys.add(event.getKey());
                    list.add(event);
                }
            }
            if (list.size() > 1) {
                addReadyEvent(list.get(random.nextInt(list.size())));
            } else if (list.size() == 1) {
                addReadyEvent(list.get(0));
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

    public boolean hasBranch(String key) {
        return branchMap.containsKey(key);
    }

    /**
     * 向当前需要触发的事件列表中添加事件
     *
     * @param key   事件key
     * @param index 添加到的序号
     */
    public boolean addEventToReady(String key, int index) {
        Event event = eventManager.getEvent(key);
        if (event != null) {
            if (index == -1 || index > readyEvents.size()) {
                readyEvents.add(event);
            } else {
                readyEvents.add(index, event);
                if (index == 0) {
                    NoticeRunner.notice(Tip.EVENT_NOW);
                }
            }
            return true;
        }
        return false;
    }

    public void addReadyEvent(Event event) {
        readyEvents.add(event);
    }

    /**
     * 向下回合需要触发的事件列表中添加事件
     *
     * @param key   事件key
     * @param index 添加到的序号
     */
    public boolean addEventToPre(String key, int index) {
        Event event = eventManager.getEvent(key);
        if (event != null) {
            if (index == -1 || index > readyEvents.size()) {
                preEvents.add(event);
            } else {
                preEvents.add(index, event);
            }
            return true;
        }
        return false;
    }

    public boolean canUseItem(String key) {
        if (role != null) {
            Item item = role.getBag().get(key);
            if (item != null) {
                return testCondition(item);
            }
        }
        return false;
    }

    /**
     * 使用背包中的道具
     *
     * @param key 道具Key
     */
    public Result useItem(String key) {
        if (role != null) {
            Item item = role.getBag().get(key);
            if (item != null) {
                if (testCondition(item)) {
                    return Result.ok(execCmd(item.getOnUse()));
                }
            } else {
                return Result.fail("No such item!");
            }
        }
        return Result.fail("Fail");
    }

    public int random(int min, int max) {
        int bound = max - min;
        if (bound < 1) {
            return 0;
        }
        return random.nextInt(max) + min;
    }

    /**
     * 游戏结束
     */
    public void finish() {
        game.setProcess(2);
        readyEvents.clear();
        preEvents.clear();
    }

    public void clear() {
        kit.clear();
        stories.clear();
        game.clear();

        BranchManager.getInstance().clear();
        EventManager.getInstance().clear();
        ItemManager.getInstance().clear();
        OptionManager.getInstance().clear();
        RuleManager.getInstance().clear();
        SkillManager.getInstance().clear();
        StoryManager.getInstance().clear();
        TagManager.getInstance().clear();
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
        cmd = tran(cmd);
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

    private String tran(String desc) {
        return varsContext.build(desc, attrHandler);
    }

    private boolean testCondition(Conditionable conditionable) {
        return DescUtil.test(tran(conditionable.getCondition()));
    }

    private boolean randomChance(Chancable chancable) {
        return DescUtil.result(tran(chancable.getChance())) > random.nextInt(10000);
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        if (stories != null) {
            json.put("sto", stories);
        }
        if (role != null) {
            json.put("role", role.save());
        }
        if (world != null) {
            json.put("wor", world.save());
        }
        json.put("kit", kit.save());
        json.put("res", readyEvents.save());
        json.put("pes", preEvents.save());
        json.put("bm", branchMap.save());
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        readyEvents.clear();
        preEvents.clear();
        branchMap.clear();
        stories.clear();
        if (json == null) {
            return false;
        }
        if (json.containsKey("sto")) {
            stories.addAll(json.getList("sto", Story.class));
        }
        role = new Role();
        role.load(json.getJSONObject("role"));
        world = new World();
        world.load(json.getJSONObject("wor"));
        kit.load(json.getJSONObject("kit"));
        readyEvents.load(json.getJSONObject("res"));
        preEvents.load(json.getJSONObject("pes"));
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
