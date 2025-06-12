import java.util.PriorityQueue;
// PEC.java - Manages events in priority order
public class PEC {
    private final PriorityQueue<Event> events;
    
    public PEC() {
        this.events = new PriorityQueue<>();
    }
    
    public void addEvent(Event event) {
        events.add(event);
    }
    
    public Event nextEvent() {
        return events.poll();
    }
    
    public boolean isEmpty() {
        return events.isEmpty();
    }
}
