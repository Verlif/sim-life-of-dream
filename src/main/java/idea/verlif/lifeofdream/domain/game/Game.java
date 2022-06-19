package idea.verlif.lifeofdream.domain.game;

import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.world.World;

/**
 * 当前游戏信息
 *
 * @author Verlif
 */
public class Game {

    private final Role role;

    private final World world;

    public Game() {
        role = new Role();
        world = new World();
    }
}
