// Event.java - Abstract base class for simulation events
public abstract class Event implements Comparable<Event> {
    protected final double time;
    protected final Individual individual;
    protected final EventType type;

    public Event(double time, Individual individual, EventType type) {
        this.time = time;
        this.individual = individual;
        this.type = type;
    }

    public double getTime() {
        return time;
    }

    public Individual getIndividual() {
        return individual;
    }

    public EventType getType() {
        return type;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    public abstract void execute(Simulation simulation);
}
