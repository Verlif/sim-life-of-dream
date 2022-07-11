package idea.verlif.lifeofdream.sys.kit;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.sys.kit.impl.DefaultInputKit;
import idea.verlif.lifeofdream.sys.kit.impl.DefaultMessageKit;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 *
 * @author Verlif
 */
public class Kit implements CanSave {

    private MessageKit message;
    private InputKit input;

    private final DataKit data;
    private final List<String> records;

    private final MessagePreHandler handler;

    public Kit(MessagePreHandler handler) {
        this.handler = handler;
        message = new DefaultMessageKit();
        input = new DefaultInputKit();
        data = new DataKit();
        records = new ArrayList<>();
    }

    public MessageKit getMessage() {
        return message;
    }

    public void setMessage(MessageKit message) {
        this.message = message;
    }

    public InputKit getInput() {
        return input;
    }

    public void setInput(InputKit input) {
        this.input = input;
    }

    public void message(String message) {
        if (message != null && message.length() > 0) {
            message = handler.handle(message);
            this.message.show(message);
            records.add(message);
        }
    }

    public DataKit getData() {
        return data;
    }

    public List<String> getRecords() {
        return records;
    }

    public MessagePreHandler getHandler() {
        return handler;
    }

    public void clear() {
        records.clear();
        data.clear();
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("res", records);
        json.put("data", data.save());
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
        data.load(json.getJSONObject("data"));
        return true;
    }

    public interface MessagePreHandler {
        String handle(String message);
    }
}
