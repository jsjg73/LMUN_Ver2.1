package com.jsjg73.lmun.model;

import com.jsjg73.lmun.model.manytomany.Participant;
import com.jsjg73.lmun.model.manytomany.Proposal;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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

    @Column(columnDefinition="tinyint(1) default 0")
    private boolean closed;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private Set<Participant> participants=new HashSet<>();
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private Set<Proposal> proposals = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

    public Meeting(String name, User host, Integer atLeast) {
        this.name = name;
        this.host = host;
        this.atLeast = atLeast;
    }

    public boolean containsUser(User user) {
        return participants.stream().filter(participant -> participant.getUser().equals(user)).count() == 1;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
    public void addProposal(Proposal proposal){
        proposals.add(proposal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
