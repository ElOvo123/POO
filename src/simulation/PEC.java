package simulation;

import java.util.PriorityQueue;
import events.Event;

public class PEC {
    private final PriorityQueue<Event> events = new PriorityQueue<>();

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