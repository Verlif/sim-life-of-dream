package idea.verlif.lifeofdream.pack;

/**
 * @author Verlif
 */
public class PackInfo {

    /**
     * 资源包名称
     */
    private String name;

    /**
     * 资源包描述
     */
    private String desc;

    /**
     * 资源包版本
     */
    private String version;

    /**
     * 资源包作者
     */
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
