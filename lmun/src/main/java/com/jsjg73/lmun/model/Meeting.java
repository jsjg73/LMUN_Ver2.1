package com.jsjg73.lmun.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Meeting {
    @Id
    private String id;
    private String name;
    @OneToOne
    @JoinColumn(name = "host")
    private User host;
    private Integer atLeast;
}
