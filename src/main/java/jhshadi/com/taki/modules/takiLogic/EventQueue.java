package jhshadi.com.taki.modules.takiLogic;

import java.util.ArrayList;
import java.util.List;

public class EventQueue {

    private static final int TOP_OF_QUEUE = 0;
    private final List<Event> queue;

    public EventQueue() {
        this.queue = new ArrayList<>();
    }

    public Event pop() {
        return queue.remove(TOP_OF_QUEUE);
    }

    public void push(Event event) {
        event.setEventId(getCurrentEventId());
        queue.add(event);
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public List<Event> getEvents(int eventId) {
        return queue.subList(eventId, getCurrentEventId());
    }

    public int getCurrentEventId() {
        return queue.size();
    }
}
