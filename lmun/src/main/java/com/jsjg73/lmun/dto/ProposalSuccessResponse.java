package com.jsjg73.lmun.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalSuccessResponse {
    String meetingId;
    String locationId;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
