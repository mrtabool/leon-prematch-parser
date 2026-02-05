package com.framework.client;

import com.framework.client.dto.EventDTO;

import java.util.List;

public class EventResponseWrapper {
    private List<EventDTO> events;

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }
}
