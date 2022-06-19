package idea.verlif.lifeofdream;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.domain.role.Role;
import idea.verlif.lifeofdream.domain.world.World;
import idea.verlif.lifeofdream.sys.exec.ExecRunner;
import idea.verlif.lifeofdream.sys.launch.Result;

/**
 * @author Verlif
 */
public class Test {

    public static void main(String[] args) {
        ExecRunner runner = ExecRunner.getInstance();
        Role role = new Role();
        runner.init(role, new World());
        Result result = runner.exec("role.info.setName 小明");
        System.out.println(JSONObject.toJSONString(result));
        System.out.println(JSONObject.toJSONString(role));
    }

}
