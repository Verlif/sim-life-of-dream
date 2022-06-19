package idea.verlif.lifeofdream.sys.exec;

/**
 * 执行结果
 *
 * @author Verlif
 */
public class Result {

    private boolean ok;

    private String m;

    private Object data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result ok(Object o) {
        Result result = new Result();
        result.setOk(true);
        result.setData(o);
        return result;
    }

    public static Result fail(String m) {
        Result result = new Result();
        result.setOk(false);
        result.setM(m);
        return result;
    }
}
