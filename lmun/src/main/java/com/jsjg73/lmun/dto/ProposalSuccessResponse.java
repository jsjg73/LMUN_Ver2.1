package com.jsjg73.lmun.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalSuccessResponse {
    String meetingId;
    String locationId;
    Date timestamp;
    Date update;
}
