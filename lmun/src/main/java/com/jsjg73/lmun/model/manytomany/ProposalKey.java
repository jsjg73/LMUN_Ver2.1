package com.jsjg73.lmun.model.manytomany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalKey implements Serializable {
    @Column(name="meeting_id")
    String meetingId;

    @Column(name="location_id")
    Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposalKey that = (ProposalKey) o;
        return Objects.equals(meetingId, that.meetingId) && Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, locationId);
    }
}
