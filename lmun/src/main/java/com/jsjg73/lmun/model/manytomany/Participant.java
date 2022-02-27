package com.jsjg73.lmun.model.manytomany;

import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participant {

    @EmbeddedId
    private ParticipantKey id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @MapsId("meeting_id")
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @MapsId("username")
    @JoinColumn(name = "username")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="departure_id")
    private Location departure;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean attending;

    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

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
