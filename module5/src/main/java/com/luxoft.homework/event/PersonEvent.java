package com.luxoft.homework.event;

public class PersonEvent {

    public enum Level {
        INSERT,
        UPDATE,
        DELETE
    }

    private long id;
    private String name;
    private Level eventType;


    public PersonEvent() {
    }

    public PersonEvent(long id, String name, Level eventType) {
        this.id = id;
        this.name = name;
        this.eventType = eventType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getEventType() {
        return eventType;
    }

    public void setEventType(Level eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "PersonEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }


}