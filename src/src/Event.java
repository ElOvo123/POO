// Event.java - Abstract base class for simulation events
public abstract class Event implements Comparable<Event> {
    protected final EventType type;
    protected final double time;
    protected final Individual individual;

    public Event(EventType type, double time, Individual individual) {
        this.type = type;
        this.time = time;
        this.individual = individual;
    }

    public EventType getType() {
        return type;
    }

    public double getTime() {
        return time;
    }

    public Individual getIndividual() {
        return individual;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    public abstract void execute(Simulation simulation);
}

enum EventType {
    DEATH,
    MOVE,
    REPRODUCTION
}
