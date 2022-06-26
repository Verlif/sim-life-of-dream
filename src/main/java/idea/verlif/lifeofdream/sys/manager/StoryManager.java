package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedList;
import idea.verlif.lifeofdream.domain.story.Story;

import java.util.List;

/**
 * 故事管理器
 *
 * @author Verlif
 */
public class StoryManager implements CanSave {

    private static final StoryManager STORY_MANAGER = new StoryManager();

    private final CanSavedList<Story> stories;

    private StoryManager() {
        stories = new CanSavedList<Story>() {
            @Override
            protected Story getNewElement() {
                return new Story();
            }
        };
    }

    public static StoryManager getInstance() {
        return STORY_MANAGER;
    }

    public List<Story> getStories() {
        return stories;
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("sts", stories.save());
    }

    @Override
    public boolean load(JSONObject json) {
        stories.clear();
        if (json == null) {
            return false;
        }
        return stories.load(json.getJSONObject("sts"));
    }
}
