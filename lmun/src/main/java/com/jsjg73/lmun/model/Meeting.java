package com.jsjg73.lmun.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    @OneToOne
    @JoinColumn(name = "host")
    private User host;

    private Integer atLeast;

    @OneToMany(mappedBy = "meeting")
    private List<Participant> participants = new ArrayList<>();

    public Meeting(String name, User host, Integer atLeast) {
        this.name = name;
        this.host = host;
        this.atLeast = atLeast;
    }

}
