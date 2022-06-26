package idea.verlif.lifeofdream.sys.kit;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 *
 * @author Verlif
 */
public class Kit implements CanSave {

    private MessageKit messageKit;

    private final List<String> records;

    public Kit() {
        messageKit = message -> {
        };
        records = new ArrayList<>();
    }

    public MessageKit getMessageKit() {
        return messageKit;
    }

    public void setMessageKit(MessageKit messageKit) {
        this.messageKit = messageKit;
    }

    public void message(String message) {
        if (message != null && message.length() > 0) {
            messageKit.show(message);
            records.add(message);
        }
    }

    public List<String> getRecords() {
        return records;
    }

    public void clear() {
        records.clear();
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("res", records);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        records.clear();
        if (json == null) {
            return false;
        }
        if (json.containsKey("res")) {
            records.addAll(json.getList("res", String.class));
        }
        return true;
    }
}
