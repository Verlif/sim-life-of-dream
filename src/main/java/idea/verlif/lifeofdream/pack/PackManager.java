package idea.verlif.lifeofdream.pack;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 */
public class PackManager {

    private static final PackManager PACK_MANAGER = new PackManager();

    private final List<Pack> packs;

    private PackManager() {
        packs = new ArrayList<>();
    }

    public static PackManager getInstance() {
        return PACK_MANAGER;
    }

    public List<Pack> getPacks() {
        return packs;
    }

    public boolean loadFromFile(File jsonFile) {
        try {
            String jsonStr = FileUtil.readFromFile(jsonFile);
            JSONObject json = JSONObject.parseObject(jsonStr);
            if (json != null) {
                Pack pack = json.to(Pack.class);
                packs.add(pack);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
