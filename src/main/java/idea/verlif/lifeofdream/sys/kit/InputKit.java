package idea.verlif.lifeofdream.sys.kit;

/**
 * @author Verlif
 */
public interface InputKit {

    /**
     * 输入文本
     * @return 文本内容
     */
    String getText(String title);

    /**
     * 输入数字
     * @return 数字内容
     */
    int getInt(String title);
}
