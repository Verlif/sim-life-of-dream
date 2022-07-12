package idea.verlif.lifeofdream.notice.entity;

/**
 * @author Verlif
 */
public enum Tip {

    /**
     * 即时事件
     */
    EVENT_NOW,

    /**
     * 角色名称改变
     */
    NAME_CHANGED,

    /**
     * 新技能学习
     */
    SKILL_ADDED,

    /**
     * 技能升级
     */
    SKILL_LEVEL_UP,

    /**
     * 技能升级
     */
    SKILL_LEVEL_DOWN,

    /**
     * 技能遗忘
     */
    SKILL_REMOVED,

    /**
     * 添加了标签
     */
    TAG_ADDED,

    /**
     * 移除了标签
     */
    TAG_REMOVED,

    /**
     * 添加了道具
     */
    ITEM_ADDED,

    /**
     * 移除了道具
     */
    ITEM_REMOVED;
}
