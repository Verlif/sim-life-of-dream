package idea.verlif.lifeofdream.domain.role;

import idea.verlif.lifeofdream.role.extra.Attr;

/**
 * 角色基础属性
 *
 * @author Verlif
 */
public class RoleAttr {

    /**
     * 健康值
     */
    private final Attr health;

    /**
     * 体力值
     */
    private final Attr endurance;

    /**
     * 脑力值
     */
    private final Attr brain;

    /**
     * 心情值
     */
    private final Attr mood;

    public RoleAttr() {
        health = new Attr("健康值", 100);
        endurance = new Attr("体力值", 100);
        brain = new Attr("脑力值", 100);
        mood = new Attr("心情值", 100);
    }

    public Attr getHealth() {
        return health;
    }

    public Attr getEndurance() {
        return endurance;
    }

    public Attr getBrain() {
        return brain;
    }

    public Attr getMood() {
        return mood;
    }
}
