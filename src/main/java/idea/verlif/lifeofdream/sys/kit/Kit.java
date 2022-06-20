package idea.verlif.lifeofdream.sys.kit;

/**
 * 工具类
 *
 * @author Verlif
 */
public class Kit {

    private MessageKit messageKit;
    
    public Kit() {
        messageKit = message -> {
        };
    }

    public MessageKit getMessageKit() {
        return messageKit;
    }

    public void setMessageKit(MessageKit messageKit) {
        this.messageKit = messageKit;
    }

    public void message(String message) {
        messageKit.show(message);
    }
}
