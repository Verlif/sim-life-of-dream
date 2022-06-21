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
        messageKit.show(message);
        records.add(message);
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("res", records);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        records.clear();
        records.addAll(json.getList("res", String.class));
        return true;
    }
}
