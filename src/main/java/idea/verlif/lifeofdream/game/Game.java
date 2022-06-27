package idea.verlif.lifeofdream.game;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.pack.Pack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 游戏数据
 *
 * @author Verlif
 */
public class Game implements CanSave {

    private final Role role;
    private final World world;
    private final List<String> packs;

    /**
     * 是否游戏进程状态；0 - 未开始；1 - 进行中；2 - 结束
     */
    private int process;

    public Game() {
        this.role = new Role();
        this.world = new World();
        this.packs = new ArrayList<>();
        this.process = 0;
    }

    public Role getRole() {
        return role;
    }

    public World getWorld() {
        return world;
    }

    public List<String> getPacks() {
        return packs;
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

    /**
     * 从资源包中创建新游戏
     *
     * @param packs 资源包
     * @return 新游戏
     */
    public static Game newGame(Pack... packs) {
        Game game = new Game();
        for (Pack pack : packs) {
            if (pack.getStory() != null) {
                game.getPacks().add(pack.getInfo().getKey());
            }
        }
        return game;
    }

    public static Game newGame(Collection<Pack> packs) {
        Game game = new Game();
        for (Pack pack : packs) {
            if (pack.getStory() != null) {
                game.getPacks().add(pack.getInfo().getKey());
            }
        }
        return game;
    }

    public void clear() {
        packs.clear();
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("role", role.save());
        json.put("world", world.save());
        json.put("stos", packs);
        json.put("pro", process);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        role.load(json.getJSONObject("role"));
        world.load(json.getJSONObject("world"));
        packs.addAll(json.getList("stos", String.class));
        process = json.getIntValue("pro");
        return true;
    }
}
