package com.jsjg73.lmun.model.manytomany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantKey implements Serializable {
    @Column(name = "meeting_id")
    String meetingId;
    @Column(name = "username")
    String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantKey that = (ParticipantKey) o;
        return Objects.equals(meetingId, that.meetingId) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, username);
    }
}
