package day02;
public class Actor {
    private Long id;
    private String actorName;

    public Actor(String actorName) {
        this.actorName = actorName;
    }

    public Long getId() {
        return id;
    }

    public String getActorName() {
        return actorName;
    }

    public Actor(Long id, String actorName) {
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
