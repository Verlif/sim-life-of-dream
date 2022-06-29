package idea.verlif.lifeofdream.domain.role;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.lifeofdream.base.CanSave;
import idea.verlif.lifeofdream.notice.NoticeRunner;
import idea.verlif.lifeofdream.notice.entity.Tip;
import idea.verlif.lifeofdream.notice.entity.ValueType;
import idea.verlif.lifeofdream.standard.NumberValue;
import idea.verlif.lifeofdream.standard.TextValue;

/**
 * 角色基础信息
 *
 * @author Verlif
 */
public class RoleInfo implements CanSave {

    /**
     * 姓名
     */
    private final Name name;

    /**
     * 年龄
     */
    private final Age age;

    /**
     * 性别代号；0 - 未知；1 - 男；2 - 女
     */
    private int sex;

    public RoleInfo() {
        name = new Name();
        age = new Age();
    }

    public Name getName() {
        return name;
    }

    public Age getAge() {
        return age;
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
        json.put("name", name.str);
        json.put("age", age.value);
        json.put("sex", sex);
        return json;
    }

    @Override
    public boolean load(JSONObject json) {
        if (json == null) {
            return false;
        }
        name.set(json.getString("name"));
        age.set(json.getIntValue("age"));
        sex = json.getIntValue("sex");
        return true;
    }

    public static class Name implements TextValue {

        private String str;

        @Override
        public String text() {
            return str;
        }

        @Override
        public void set(String text) {
            this.str = text;
            NoticeRunner.notice(Tip.NAME_CHANGED);
        }
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
            if (up != 0) {
                NoticeRunner.notice("年龄", up, ValueType.INFO);
            }
        }

        @Override
        public void set(int value) {
            this.value = value;
        }
    }
}
