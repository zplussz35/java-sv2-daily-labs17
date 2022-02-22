package day01;

public class Actor {
    private int id;
    private String actorName;

    public Actor(String actorName) {
        this.actorName = actorName;
    }

    public int getId() {
        return id;
    }

    public String getActorName() {
        return actorName;
    }

    public Actor(int id, String actorName) {
        this.id = id;
        this.actorName = actorName;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", actorName='" + actorName + '\'' +
                '}';
    }
}
