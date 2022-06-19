package idea.verlif.lifeofdream.domain.role;

/**
 * 角色
 *
 * @author Verlif
 */
public class Role {

    private final RoleInfo info;

    private final RoleAttr attr;

    private final RoleSkill skill;

    private final RoleParam param;

    public Role() {
        info = new RoleInfo();
        attr = new RoleAttr();
        skill = new RoleSkill();
        param = new RoleParam();
    }

    public RoleInfo getInfo() {
        return info;
    }

    public RoleAttr getAttr() {
        return attr;
    }

    public RoleSkill getSkill() {
        return skill;
    }

    public RoleParam getParam() {
        return param;
    }
}
