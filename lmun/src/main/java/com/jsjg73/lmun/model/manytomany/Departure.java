package com.jsjg73.lmun.model.manytomany;

import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "userDeparture")
@Getter
@Setter
@NoArgsConstructor
public class Departure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public Departure(User user, Location location) {
        this.user = user;
        this.location = location;
    }

}
