package idea.verlif.lifeofdream.game;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.domain.branch.Branch;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.pack.Pack;
import idea.verlif.lifeofdream.sys.manager.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 游戏数据
 *
 * @author Verlif
 */
public class Game {

    private final GameRunner gameRunner;

    public Game(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    public Result start() {
        gameRunner.init(new Role(), new World());
        return gameRunner.start();
    }

    public void finish() {
        gameRunner.finish();
    }

    public boolean isFinish() {
        return gameRunner.isFinish();
    }

    public static Game loadData(String data) {
        JSONObject json = JSONObject.parseObject(data);
        GameRunner er = GameRunner.getInstance();
        er.load(json.getJSONObject("er"));

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

        return new Game(er);
    }

    /**
     * 从资源包中创建新游戏
     *
     * @param packs 资源包
     * @return 新游戏
     */
    public static Game newGame(Pack... packs) {
        GameRunner er = GameRunner.getInstance();
        er.init(new Role(), new World());

        Set<Branch> branches = new HashSet<>();
        Set<Event> events = new HashSet<>();
        Set<Item> items = new HashSet<>();
        Set<Option> options = new HashSet<>();
        Set<Rule> rules = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        for (Pack pack : packs) {
            er.addStory(pack.getStory());
        }
        for (int i = packs.length - 1; i > -1; i--) {
            Pack pack = packs[i];
            branches.addAll(pack.getBranches());
            events.addAll(pack.getEvents());
            items.addAll(pack.getItems());
            options.addAll(pack.getOptions());
            rules.addAll(pack.getRules());
            tags.addAll(pack.getTags());
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

        return new Game(er);
    }

    public String exportData() {
        JSONObject json = new JSONObject();
        json.put("er", gameRunner.save());

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

        return json.toJSONString();
    }
}
