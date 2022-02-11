package com.jsjg73.lmun.model.manytomany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OriginKey implements Serializable {
    ProposalKey proposalId;
    @Column(name = "username")
    String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OriginKey originKey = (OriginKey) o;
        return Objects.equals(proposalId, originKey.proposalId) && Objects.equals(username, originKey.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposalId, username);
    }
}
