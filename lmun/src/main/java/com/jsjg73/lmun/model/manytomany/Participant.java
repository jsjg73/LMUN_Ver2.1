package com.jsjg73.lmun.model.manytomany;

import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participant {

    @EmbeddedId
    private ParticipantKey id;

    @ManyToOne
    @MapsId("meeting_id")
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    private User user;

    @OneToOne
    @JoinColumn(name="departure_id")
    private Location departure;

    public Participant(Meeting meeting, User host, Location departure) {
        this.meeting=meeting;
        this.user=host;
        this.departure=departure;
    }
}
