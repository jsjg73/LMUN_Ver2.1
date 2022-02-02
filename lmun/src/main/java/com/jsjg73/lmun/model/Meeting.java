package com.jsjg73.lmun.model;

import com.jsjg73.lmun.model.manytomany.Participant;
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

    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "host")
    private User host;

    private Integer atLeast;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

    public Meeting(String name, User host, Integer atLeast) {
        this.name = name;
        this.host = host;
        this.atLeast = atLeast;
    }

}
