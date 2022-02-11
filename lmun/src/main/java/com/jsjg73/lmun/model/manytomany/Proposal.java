package com.jsjg73.lmun.model.manytomany;

import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Proposal {

    @EmbeddedId
    private ProposalKey id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("meeting_id")
    @JoinColumn(name="meeting_id")
    private Meeting meeting;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("location_id")
    @JoinColumn(name="location_id")
    private Location location;

    @OneToOne
    @JoinColumn(name="username")
    private User proposer;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL)
    Set<Origin> origins;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposal proposal = (Proposal) o;
        return Objects.equals(id, proposal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
