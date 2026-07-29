package com.example.hikermanagement;

public class Observation {

    private int id;
    private int hikeId;
    private String observation;
    private String observationTime;
    private String comment;

    public Observation(
            int id,
            int hikeId,
            String observation,
            String observationTime,
            String comment
    ) {

        this.id = id;
        this.hikeId = hikeId;
        this.observation = observation;
        this.observationTime = observationTime;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public int getHikeId() {
        return hikeId;
    }

    public String getObservation() {
        return observation;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public String getComment() {
        return comment;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setObservationTime(
            String observationTime
    ) {
        this.observationTime = observationTime;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}