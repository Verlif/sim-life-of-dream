package idea.verlif.lifeofdream.standard;

import idea.verlif.justsimmand.anno.SimmParam;

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
    void levelUp(@SimmParam(defaultVal = "1") int up);
}
