package com.jsjg73.lmun.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalRequest {
    LocationDto destination;
    List<OriginRequest> origins;
}
