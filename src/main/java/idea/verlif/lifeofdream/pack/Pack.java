package idea.verlif.lifeofdream.pack;

import idea.verlif.lifeofdream.domain.branch.Branch;
import idea.verlif.lifeofdream.domain.event.Event;
import idea.verlif.lifeofdream.domain.event.Option;
import idea.verlif.lifeofdream.domain.item.Item;
import idea.verlif.lifeofdream.domain.role.extra.Skill;
import idea.verlif.lifeofdream.domain.role.extra.Tag;
import idea.verlif.lifeofdream.domain.rule.Rule;
import idea.verlif.lifeofdream.domain.story.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源包
 *
 * @author Verlif
 */
public class Pack {

    private PackInfo info;

    private Story story;

    private List<Branch> branches;

    private List<Event> events;

    private List<Option> options;

    private List<Item> items;

    private List<Rule> rules;

    private List<Tag> tags;

    private List<Skill> skills;

    public PackInfo getInfo() {
        return info;
    }

    public void setInfo(PackInfo info) {
        this.info = info;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public List<Branch> getBranches() {
        if (branches == null) {
            branches = new ArrayList<>();
        }
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public List<Event> getEvents() {
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Option> getOptions() {
        if (options == null) {
            options = new ArrayList<>();
        }
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Rule> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Tag> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Skill> getSkills() {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
