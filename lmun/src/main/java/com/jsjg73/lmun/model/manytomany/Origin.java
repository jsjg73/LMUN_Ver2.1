package com.jsjg73.lmun.model.manytomany;

import com.jsjg73.lmun.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Origin {
    @EmbeddedId
    private OriginKey id;

    @ManyToOne
    @MapsId("proposalId")
    @JoinColumns({
        @JoinColumn(name="meeting_id"),
        @JoinColumn(name="location_id")
    })
    private Proposal proposal;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name="username")
    private User user;

    private Double lon;
    private Double lat;
    private Boolean agree;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Origin origin = (Origin) o;
        return Objects.equals(id, origin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
