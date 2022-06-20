package idea.verlif.lifeofdream;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.story.Story;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.game.Game;
import idea.verlif.lifeofdream.pack.Pack;
import idea.verlif.lifeofdream.game.GameRunner;
import idea.verlif.lifeofdream.game.Result;
import idea.verlif.lifeofdream.sys.kit.MessageKit;
import idea.verlif.lifeofdream.sys.manager.EventManager;
import idea.verlif.lifeofdream.sys.manager.ItemManager;
import idea.verlif.lifeofdream.sys.manager.OptionManager;
import idea.verlif.lifeofdream.sys.manager.PackManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Verlif
 */
public class TestForSth {

    private static final GameRunner EXEC_RUNNER = GameRunner.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        TestForSth tfs = new TestForSth();
        tfs.simulation();
    }

    @Test
    public void simulation() {
        PackManager pm = PackManager.getInstance();
        pm.loadFromFile(new File("packs\\default.json"));
        List<Pack> packs = pm.getPacks();
        if (packs.size() > 0) {
            Game game = Game.newGame(packs.get(0));
            System.out.println(game.exportData());
        }
        EXEC_RUNNER.setMessageKit(createMessageKit());
        System.out.println(EXEC_RUNNER.start().getM());

        Item item = ItemManager.getInstance().get("小学作业");
        item.setName("hahahaha");
        System.out.println(item.save());
        System.out.println(ItemManager.getInstance().get("小学作业").save());

        while (true) {
            System.out.println("----------------------------------------------------------------\n" +
                    "1. 显示当前事件\n" +
                    "2. 跳过当前事件\n" +
                    "3. 下一回合\n" +
                    "4. 显示角色属性\n" +
                    "5. 显示世界信息");
            select(SCANNER.nextInt());
        }
    }

    private static void select(int opt) {
        switch (opt) {
            case 1: {
                Event event = EXEC_RUNNER.nextEvent();
                if (event != null) {
                    showEvent(event);
                    List<Option> options = event.getReadyOptions();
                    showOptions(options);
                    int i = SCANNER.nextInt();
                    if (i <= options.size()) {
                        Option option = options.get(i - 1);
                        System.out.println(JSONObject.toJSONString(EXEC_RUNNER.execCmd("execOption " + option.getKey()).getData()));
                        select(1);
                    } else {
                        select(2);
                    }
                } else {
                    System.out.println("没有事件发生");
                }
                break;
            }
            case 2: {
                System.out.println(JSONObject.toJSONString(EXEC_RUNNER.execCmd("skipEvent").getData()));
                Result result = EXEC_RUNNER.execCmd("nextTurn");
                System.out.println(JSONObject.toJSONString(result.getData()));
                if (result.isOk()) {
                    select(1);
                }
                break;
            }
            case 3:
                Result result = EXEC_RUNNER.execCmd("nextTurn");
                System.out.println(JSONObject.toJSONString(result.getData()));
                if (result.isOk()) {
                    select(1);
                }
                break;
            case 5:
                System.out.println(JSONObject.toJSONString(EXEC_RUNNER.getWorld()));
                break;
            default:
                showRole(EXEC_RUNNER.getRole());
        }
    }

    private static void showEvent(Event event) {
        System.out.println(event.getTitle() + "\n\t\t" + event.getDesc());
    }

    private static void showOptions(List<Option> options) {
        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            System.out.println((i + 1) + ". \t\t" + option.getTitle());
            System.out.println("   \t\t" + option.getDesc());
        }
        System.out.println((options.size() + 1) + ". \t\t忽略");
    }

    private static void showRole(Role role) {
        System.out.println("姓名：" + role.getInfo().getName() + "\t\t年龄：" + role.getInfo().getAge().value());
        System.out.println("健康：" + role.getAttr().getHealth().value() + "\t\t体力：" + role.getAttr().getEndurance().value());
        System.out.println("脑力：" + role.getAttr().getBrain().value() + "\t\t心情：" + role.getAttr().getMood().value());
    }

    @Test
    public void test() {
        GameRunner runner = GameRunner.getInstance();
        Game game = new Game(runner);
        runner.init(new Role(), new World());
        initEventManager();
        initOptionManager();
        runner.setStory(new Story());
        runner.execCmd("role.info.name 小明");
        runner.execCmd("addEventToReady 吃饭 1");
        System.out.println(JSONObject.toJSONString(runner.execCmd("nextEvent").getData()));
        String s = game.exportData();
        System.out.println("exportData: " + s);
        Game loadedGame = Game.loadData(s);
        System.out.println("loadedGame: " + loadedGame.exportData());
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

    public Item createItem(String key) {
        Item item = new Item();
        item.setKey(key);
        return item;
    }

    public Event createEvent(String key) {
        Event event = new Event();
        event.setKey(key);
        return event;
    }

    public Option createOption(String key, String... events) {
        Option option = new Option();
        option.setKey(key);
        for (String event : events) {
            option.getAfterEvents().add(event);
        }
        option.setChance("5000");
        return option;
    }
}
