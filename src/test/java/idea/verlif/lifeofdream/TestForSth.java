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
import idea.verlif.lifeofdream.sys.exec.ExecRunner;
import idea.verlif.lifeofdream.sys.exec.Result;
import idea.verlif.lifeofdream.sys.kit.Kit;
import idea.verlif.lifeofdream.sys.manager.EventManager;
import idea.verlif.lifeofdream.sys.manager.OptionManager;
import idea.verlif.lifeofdream.sys.manager.PackManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * @author Verlif
 */
public class TestForSth {

    private static final ExecRunner EXEC_RUNNER = ExecRunner.getInstance();
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
        EXEC_RUNNER.setKit(createKit());
        System.out.println(EXEC_RUNNER.start().getM());
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
        ExecRunner runner = ExecRunner.getInstance();
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

    private Kit createKit() {
        return new Kit() {
            @Override
            public void message(String message) {
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
