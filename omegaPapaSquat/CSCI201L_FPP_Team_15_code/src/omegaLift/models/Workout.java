package omegaLift.models;

import java.sql.Timestamp;

public class Workout {
    private int workoutId;
    private String title;
    private String author;
    private String caption;
    private String description;
    private Timestamp posted;
    
    public Workout () {
        
    }
    
    public Workout(int workoutId, String title, String author, String caption, String description, Timestamp posted) {
        this.workoutId = workoutId;
        this.title = title;
        this.author = author;
        this.caption = caption;
        this.description = description;
        this.posted = posted;
    }
}
