package idea.verlif.lifeofdream.notice;

import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.notice.entity.ValueNotice;
import idea.verlif.lifeofdream.notice.entity.ValueType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Verlif
 */
public class NoticeRunner {

    private static final NoticeRunner NOTICE_RUNNER = new NoticeRunner();

    private final Set<NoticeHandler> handlers;

    private NoticeRunner() {
        handlers = new HashSet<>();
    }

    public static NoticeRunner getInstance() {
        return NOTICE_RUNNER;
    }

    public void addHandler(NoticeHandler handler) {
        handlers.add(handler);
    }

    public static void notice(Tip tip) {
        for (NoticeHandler handler : NOTICE_RUNNER.handlers) {
            handler.handle(tip);
        }
    }

    public static void notice(String name, int change, ValueType type) {
        ValueNotice notice = new ValueNotice(name, change, type);
        for (NoticeHandler handler : NOTICE_RUNNER.handlers) {
            handler.handle(notice);
        }
    }
}
