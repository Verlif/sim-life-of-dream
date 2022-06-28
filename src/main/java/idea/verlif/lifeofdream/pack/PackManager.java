package idea.verlif.lifeofdream.pack;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 */
public class PackManager {

    private static final PackManager PACK_MANAGER = new PackManager();

    private final Map<String, Pack> packMap;

    private PackManager() {
        packMap = new HashMap<>();
    }

    public static PackManager getInstance() {
        return PACK_MANAGER;
    }

    public Map<String, Pack> getPackMap() {
        return packMap;
    }

    public Pack get(String key) {
        return packMap.get(key);
    }

    public boolean loadFromFile(File jsonFile) throws IOException {
        String jsonStr = FileUtil.readFromFile(jsonFile);
        JSONObject json = JSONObject.parseObject(jsonStr);
        if (json != null) {
            Pack pack = json.to(Pack.class);
            if (pack.getInfo() != null) {
                packMap.put(pack.getInfo().getKey(), pack);
                return true;
            }
        }
        return false;
    }
}
