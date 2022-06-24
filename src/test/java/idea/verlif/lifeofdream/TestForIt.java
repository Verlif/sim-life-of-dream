package idea.verlif.lifeofdream;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.role.RoleTag;
import idea.verlif.lifeofdream.domain.role.extra.Skill;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.game.GameRunner;
import idea.verlif.lifeofdream.sys.kit.MessageKit;
import idea.verlif.lifeofdream.sys.manager.EventManager;
import idea.verlif.lifeofdream.sys.manager.OptionManager;
import idea.verlif.lifeofdream.sys.manager.SkillManager;
import idea.verlif.lifeofdream.sys.manager.TagManager;

import java.util.ArrayList;
import java.util.List;

public class TestForIt {

    private static final GameRunner GAME_RUNNER = GameRunner.getInstance();

    public static void main(String[] args) {
        TestForIt forIt = new TestForIt();
        forIt.test();
    }

    public void test() {
        SkillManager skillManager = SkillManager.getInstance();
        skillManager.add(createSkill("蛋炒饭"));
        skillManager.add(createSkill("扬州炒饭"));
        JSONObject json = skillManager.save();
        System.out.println(json);
        skillManager.load(json);
        System.out.println(skillManager.save());
        GAME_RUNNER.init(new Role(), new World());
        System.out.println(JSONObject.toJSONString(GAME_RUNNER.execCmd("role.skill.set 蛋炒饭 30")));
        System.out.println(JSONObject.toJSONString(GAME_RUNNER.getRole().getSkill()));
    }

    private String[] split(String str) {
        char[] chars = str.toCharArray();
        boolean in = false;
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (in) {
                if (c == '\"') {
                    in = false;
                    list.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            } else {
                if (c == '\"') {
                    in = true;
                }
            }
        }
        return list.toArray(new String[0]);
    }

    public void initEventManager() {
        EventManager em = EventManager.getInstance();
        em.addEventToAll(createEvent("吃饭"));
        em.addEventToAll(createEvent("走路"));
        System.out.println(JSONObject.toJSONString(em.getEvent("吃饭")));
    }

    public void initOptionManager() {
        OptionManager om = OptionManager.getInstance();
        om.addOptionToAll(createOption("看书", "走路", "吃饭"));
        om.addOptionToAll(createOption("说话", "走路", "吃饭"));
        System.out.println(JSONObject.toJSONString(om.getOptionOfEvent("走路")));
    }

    private MessageKit createMessageKit() {
        return new MessageKit() {
            @Override
            public void show(String message) {
                System.out.println(message);
            }
        };
    }

    private Skill createSkill(String key) {
        Skill skill = new Skill();
        skill.setName(key);
        return skill;
    }

    private Tag createTag(String key) {
        Tag tag = new Tag();
        tag.setName(key);
        tag.setOnAdd("kit.message " + key + "-被添加");
        tag.setOnRemove("kit.message " + key + "-被移除");
        return tag;
    }

    public Item createItem(String key) {
        Item item = new Item();
        item.setName(key);
        return item;
    }

    public Event createEvent(String key) {
        Event event = new Event();
        event.setTitle(key);
        return event;
    }

    public Option createOption(String key, String... events) {
        Option option = new Option();
        option.setTitle(key);
        for (String event : events) {
            option.getFollowEvents().add(event);
        }
        option.setChance("5000");
        return option;
    }
}
