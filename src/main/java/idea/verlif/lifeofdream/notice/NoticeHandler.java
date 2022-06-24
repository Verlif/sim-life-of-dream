package idea.verlif.lifeofdream.notice;

import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.notice.entity.ValueNotice;

/**
 * 消息处理器
 *
 * @author Verlif
 */
public interface NoticeHandler {

    /**
     * 提示消息
     *
     * @param tip 提示
     */
    void handle(Tip tip);

    /**
     * 数值提醒
     *
     * @param notice 数值数据
     */
    void handle(ValueNotice notice);
}
