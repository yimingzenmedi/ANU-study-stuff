package models.entities;

import java.util.ArrayList;

public class PreStory {
    private ArrayList<String> stories;

    public PreStory(ArrayList<String> stories) {
        this.stories = stories;
    }

    public PreStory() {
        stories = new ArrayList<>();
    }

    public ArrayList<String> getStories() {
        return stories;
    }

    public void setStories(ArrayList<String> stories) {
        this.stories = stories;
    }

    public void addStory(String story) {
        this.stories.add(story);
    }
}
