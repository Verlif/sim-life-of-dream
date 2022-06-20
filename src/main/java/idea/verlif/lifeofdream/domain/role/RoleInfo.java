package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.standard.NumberValue;

/**
 * 角色基础信息
 *
 * @author Verlif
 */
public class RoleInfo implements CanSave {

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Age age;

    /**
     * 性别代号；0 - 未知；1 - 男；2 - 女
     */
    private int sex;

    public RoleInfo() {
        age = new Age();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public JSONObject save() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("age", age.value);
        json.put("sex", sex);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        name = json.getString("name");
        age = new Age();
        age.setValue(json.getIntValue("age"));
        sex = json.getIntValue("sex");
        return true;
    }

    public static class Age implements NumberValue {

        private int value;

        @Override
        public int value() {
            return value;
        }

        @Override
        public void up(int up) {
            this.value += up;
            if (value < 0) {
                value = 0;
            }
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
