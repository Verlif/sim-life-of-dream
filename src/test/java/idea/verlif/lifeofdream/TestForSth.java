package idea.verlif.lifeofdream;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.game.Game;
import idea.verlif.lifeofdream.game.GameRunner;
import idea.verlif.lifeofdream.game.Result;
import idea.verlif.lifeofdream.notice.NoticeHandler;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.notice.entity.ValueNotice;
import idea.verlif.lifeofdream.pack.Pack;
import idea.verlif.lifeofdream.pack.PackManager;

import java.io.File;
import java.util.*;

/**
 * @author Verlif
 */
public class TestForSth {

    private static final GameRunner GAME_RUNNER = GameRunner.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        TestForSth tfs = new TestForSth();
        tfs.simulation();
    }

    public void simulation() {
        PackManager pm = PackManager.getInstance();
        pm.loadFromFile(new File("packs\\Demo-RockPaperScissors.json"));
        List<Pack> packs = pm.getPacks();
        NoticeHandler handler = new NoticeHandler() {
            @Override
            public void handle(Tip tip) {
                if (tip == Tip.EVENT_NOW) {
                    select(1);
                }
            }

            @Override
            public void handle(ValueNotice notice) {
                System.out.println(notice.getName() + " " + notice.getChange());
            }
        };
        NoticeRunner.getInstance().addHandler(handler);
        if (packs.size() > 0) {
            Game game = Game.newGame(packs.toArray(new Pack[0]));
            game.load(game.save());
            GAME_RUNNER.setMessageKit(System.out::println);
            GAME_RUNNER.start(game);
            System.out.println();
            int i = 1;
            while (true) {
                if (!GAME_RUNNER.isFinish()) {
                    System.out.print("----------------------------------------- " + i++ + " -----------------------------------------\n" +
                            "1. 显示当前事件\n" +
                            "2. 跳过当前事件\n" +
                            "3. 下一回合\n" +
                            "4. 显示角色属性\n" +
                            "5. 显示背包\n" +
                            "6. 显示世界信息\n" +
                            "你的选择是：");
                    int opt = SCANNER.nextInt();
                    System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓\n");
                    select(opt);
                    System.out.println("\n↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                } else {
                    break;
                }
            }
        }
    }

    private static void select(int opt) {
        switch (opt) {
            case 1: {
                Event event = GAME_RUNNER.nextEvent();
                if (event != null) {
                    showEvent(event);
                    List<Option> options = event.getReadyOptions();
                    // 当没有选项时，表示叙事事件，可直接跳过
                    if (options.size() > 0) {
                        showOptions(options);
                        int i = SCANNER.nextInt();
                        if (i <= options.size()) {
                            Option option = options.get(i - 1);
                            Result result = GAME_RUNNER.execCmd("execOptionOfEvent " + option.getKey());
                            if (result.isOk()) {
                                select(1);
                            } else {
                                System.out.println("出错-" + JSONObject.toJSONString(result.getM()));
                            }
                        } else {
                            System.out.println("没有此选项");
                            select(1);
                        }
                    } else {
                        System.out.println();
                        select(2);
                    }
                } else {
                    System.out.println("没有事件发生");
                }
                break;
            }
            case 2: {
                Result result = GAME_RUNNER.execCmd("skipEvent");
                if (result.isOk()) {
                    select(1);
                }
                break;
            }
            case 3:
                Result result = GAME_RUNNER.execCmd("nextTurn");
                if (result.isOk()) {
                    select(1);
                }
                break;
            case 5: {
                Map<String, Item> itemMap = GAME_RUNNER.getRole().getBag().getItemMap();
                Collection<Item> items = itemMap.values();
                int i = 1;
                for (Item item : items) {
                    System.out.println((i++ + ". " + item.getName() + (GAME_RUNNER.canUseItem(item.getKey()) ? "" : "(无法使用)")));
                }
                break;
            }
            case 6: {
                Map<String, Rule> ruleMap = GAME_RUNNER.getWorld().getRuleMap();
                for (Rule rule : ruleMap.values()) {
                    System.out.println(rule.getName() + " ---- " + rule.getDesc());
                }
                break;
            }
            default:
                showRole(GAME_RUNNER.getRole());
        }
    }

    private static void showEvent(Event event) {
        System.out.println(">>> " + event.getTitle() + "\n-->\t" + event.getDesc());
    }

    private static void showOptions(List<Option> options) {
        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            System.out.println((i + 1) + ".\t" + option.getTitle() + " ---- " + option.getDesc());
        }
    }

    private static void showRole(Role role) {
        System.out.println("姓名：" + role.getInfo().getName().text() + "\t\t年龄：" + role.getInfo().getAge().value());
        System.out.println("健康：" + role.getAttr().getHealth().value() + "\t\t体力：" + role.getAttr().getEndurance().value());
        System.out.println("脑力：" + role.getAttr().getBrain().value() + "\t\t心情：" + role.getAttr().getMood().value());
    }

}
