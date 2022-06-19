package idea.verlif.lifeofdream.domain.role;

import idea.verlif.lifeofdream.domain.role.extra.Param;

/**
 * 角色判定属性
 *
 * @author Verlif
 */
public class RoleParam {

    /**
     * 魅力
     */
    private final Param charm;

    /**
     * 幸运
     */
    private final Param luck;

    /**
     * 逻辑
     */
    private final Param logic;

    /**
     * 感性
     */
    private final Param perceptual;

    /**
     * 运动
     */
    private final Param sport;

    /**
     * 精神
     */
    private final Param spirit;

    public RoleParam() {
        charm = new Param("魅力");
        luck = new Param("幸运");
        logic = new Param("逻辑");
        perceptual = new Param("感性");
        sport = new Param("运动");
        spirit = new Param("精神");
    }

    public Param getCharm() {
        return charm;
    }

    public Param getLuck() {
        return luck;
    }

    public Param getLogic() {
        return logic;
    }

    public Param getPerceptual() {
        return perceptual;
    }

    public Param getSport() {
        return sport;
    }

    public Param getSpirit() {
        return spirit;
    }
}
