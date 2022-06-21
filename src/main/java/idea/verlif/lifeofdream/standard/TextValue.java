package idea.verlif.lifeofdream.standard;

/**
 * 字符串类型
 *
 * @author Verlif
 */
public interface TextValue {

    /**
     * 获取字符串
     *
     * @return 字符串
     */
    String text();

    /**
     * 与目标字符串是否相同
     *
     * @param text 目标字符串
     * @return 是否相同
     */
    default boolean eq(String text) {
        return text.equals(text());
    }

    /**
     * 设置字符串
     *
     * @param text 设定的字符串
     */
    void set(String text);

}
