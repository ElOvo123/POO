// Event.java - Base event class
public abstract class Event implements Comparable<Event> {
    protected final double time;
    protected final Individual individual;
    protected final EventType type;
    
    public Event(double time, Individual individual, EventType type) { this.time = time;
    this.individual = individual;
    this.type = type; }
    
    public abstract void execute(Simulation simulation);
    
    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}