package com.jsjg73.lmun.model.manytomany;

import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participant {

    @EmbeddedId
    private ParticipantKey id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("meeting_id")
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("username")
    @JoinColumn(name = "username")
    private User user;

    @OneToOne
    @JoinColumn(name="departure_id")
    private Location departure;

    public Participant(Meeting meeting, User host, Location departure) {
        this.id = new ParticipantKey(meeting.getId(), host.getUsername());
        this.meeting=meeting;
        this.user=host;
        this.departure=departure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
