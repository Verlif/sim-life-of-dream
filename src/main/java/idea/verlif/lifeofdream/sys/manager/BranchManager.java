package idea.verlif.lifeofdream.sys.manager;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.base.CanSavedMap;
import idea.verlif.lifeofdream.domain.branch.Branch;

import java.util.Map;

/**
 * 分支管理器
 *
 * @author Verlif
 */
public class BranchManager implements CanSave {

    private static final BranchManager BRANCH_MANAGER = new BranchManager();

    private final CanSavedMap<String, Branch> branchMap;

    private BranchManager() {
        branchMap = new CanSavedMap<String, Branch>() {
            @Override
            protected Branch getNewValue(String s) {
                return new Branch();
            }
        };
    }

    public static BranchManager getInstance() {
        return BRANCH_MANAGER;
    }

    public Map<String, Branch> getBranchMap() {
        return branchMap;
    }

    public Branch getBranch(String key) {
        Branch branch = branchMap.get(key);
        if (branch != null) {
            return branch.copy();
        }
        return null;
    }

    public void addBranch(Branch branch) {
        branchMap.put(branch.getKey(), branch);
    }

    public void clear() {
        branchMap.clear();
    }

    @Override
    public JSONObject save() {
        return JSONObject.of("bm", branchMap.save());
    }

    @Override
    public boolean load(JSONObject json) {
        branchMap.clear();
        if (json == null) {
            return false;
        }
        return branchMap.load(json.getJSONObject("bm"));
    }
}
