package idea.verlif.lifeofdream.game;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.branch.Branch;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.role.extra.Skill;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.domain.story.Story;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.pack.Pack;
import idea.verlif.lifeofdream.sys.manager.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 游戏数据
 *
 * @author Verlif
 */
public class Game implements CanSave {

    private final Role role;
    private final World world;
    private final List<Story> stories;

    /**
     * 是否游戏进程状态；0 - 未开始；1 - 进行中；2 - 结束
     */
    private int process;

    public Game() {
        this.role = new Role();
        this.world = new World();
        this.stories = new ArrayList<>();
        this.process = 0;
    }

    public Role getRole() {
        return role;
    }

    public World getWorld() {
        return world;
    }

    public List<Story> getStories() {
        return stories;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public boolean isFinish() {
        return process == 2;
    }

    public static Game loadData(JSONObject json) {
        GameRunner er = GameRunner.getInstance();
        er.load(json.getJSONObject("er"));

        Game game = new Game();
        game.load(json.getJSONObject("game"));

        BranchManager bm = BranchManager.getInstance();
        bm.load(json.getJSONObject("bm"));
        EventManager em = EventManager.getInstance();
        em.load(json.getJSONObject("em"));
        ItemManager im = ItemManager.getInstance();
        im.load(json.getJSONObject("im"));
        OptionManager om = OptionManager.getInstance();
        om.load(json.getJSONObject("om"));
        RuleManager rm = RuleManager.getInstance();
        rm.load(json.getJSONObject("rm"));
        TagManager tm = TagManager.getInstance();
        tm.load(json.getJSONObject("tm"));
        SkillManager sm = SkillManager.getInstance();
        sm.load(json.getJSONObject("sm"));

        return game;
    }

    /**
     * 从资源包中创建新游戏
     *
     * @param packs 资源包
     * @return 新游戏
     */
    public static Game newGame(Pack... packs) {
        Game game = new Game();
        Set<Branch> branches = new HashSet<>();
        Set<Event> events = new HashSet<>();
        Set<Item> items = new HashSet<>();
        Set<Option> options = new HashSet<>();
        Set<Rule> rules = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        Set<Skill> skills = new HashSet<>();
        for (Pack pack : packs) {
            if (pack.getStory() != null) {
                game.getStories().add(pack.getStory());
            }
        }
        for (int i = packs.length - 1; i > -1; i--) {
            Pack pack = packs[i];
            branches.addAll(pack.getBranches());
            events.addAll(pack.getEvents());
            items.addAll(pack.getItems());
            options.addAll(pack.getOptions());
            rules.addAll(pack.getRules());
            tags.addAll(pack.getTags());
            skills.addAll(pack.getSkills());
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

        return game;
    }

    public JSONObject exportData() {
        JSONObject json = new JSONObject();
        json.put("er", GameRunner.getInstance().save());
        json.put("game", save());

        BranchManager bm = BranchManager.getInstance();
        json.put("bm", bm.save());
        EventManager em = EventManager.getInstance();
        json.put("em", em.save());
        ItemManager im = ItemManager.getInstance();
        json.put("im", im.save());
        OptionManager om = OptionManager.getInstance();
        json.put("om", om.save());
        RuleManager rm = RuleManager.getInstance();
        json.put("rm", rm.save());
        TagManager tm = TagManager.getInstance();
        json.put("tm", tm.save());
        SkillManager sm = SkillManager.getInstance();
        json.put("sm", sm.save());

        return json;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("role", role.save());
        json.put("world", world.save());
        json.put("stos", stories);
        json.put("pro", process);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        role.load(json.getJSONObject("role"));
        world.load(json.getJSONObject("world"));
        stories.addAll(json.getList("stos", Story.class));
        process = json.getIntValue("pro");
        return true;
    }
}
