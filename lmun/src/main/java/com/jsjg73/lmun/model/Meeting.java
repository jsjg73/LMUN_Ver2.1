package com.jsjg73.lmun.model;

import com.jsjg73.lmun.model.manytomany.Participant;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "meeting")
    private Set<Participant> participants;

    public Meeting(String name, User host, Integer atLeast) {
        this.name = name;
        this.host = host;
        this.atLeast = atLeast;
    }

    public boolean containsUser(User user) {
        return participants.stream().filter(participant -> participant.getUser().equals(user)).count() == 1;
    }
}
