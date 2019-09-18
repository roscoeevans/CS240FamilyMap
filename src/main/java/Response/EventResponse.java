package Response;

import java.util.ArrayList;

import Model.Event;

public class EventResponse {
    private Event event;
    private String message;
    private Event[] data;

    public EventResponse(Event event) {
        this.event = event;
    }
    public EventResponse(ArrayList<Event> events) {
        data = new Event[events.size()];
        for (int i = 0; i < events.size(); i++) {
            data[i] = events.get(i);
        }
    }

    public EventResponse(String message) {
        this.message = message;
    }

    public EventResponse(Event[] data) {
        this.data = data;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}
