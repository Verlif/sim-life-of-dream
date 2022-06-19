package idea.verlif.lifeofdream.standard;

/**
 * @author Verlif
 */
public interface LevelValue {

    /**
     * 获得当前等级
     *
     * @return 当前等级
     */
    int level();

    /**
     * 强制升级当前等级
     *
     * @param up 升级的等级
     */
    void levelUp(int up);
}
