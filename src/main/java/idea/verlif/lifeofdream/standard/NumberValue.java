package idea.verlif.lifeofdream.standard;

/**
 * @author Verlif
 */
public interface NumberValue {

    /**
     * 获取当前数值
     *
     * @return 当前的数值
     */
    int value();

    /**
     * 增加数值
     *
     * @param up 增加的数值量
     */
    void up(int up);

    /**
     * 设定数值
     *
     * @param value 设定的目标数值
     */
    void set(int value);

    /**
     * 降低数值
     *
     * @param down 降低的数值
     */
    default void down(int down) {
        up(-down);
    }

    /**
     * 是否与目标数值相等
     *
     * @param value 目标数值
     * @return 是否相等
     */
    default boolean eq(int value) {
        return value() == value;
    }

    /**
     * 是否比目标数值大
     *
     * @param value 目标数值
     * @return 是否比目标数值大
     */
    default boolean bt(int value) {
        return value() > value;
    }

    /**
     * 是否比目标数值小
     *
     * @param value 目标数值
     * @return 是否比目标数值小
     */
    default boolean lt(int value) {
        return value() < value;
    }

    /**
     * 是否与目标数不相等
     *
     * @param value 目标数
     * @return 是否不相等
     */
    default boolean ne(int value) {
        return value() != value;
    }
}
