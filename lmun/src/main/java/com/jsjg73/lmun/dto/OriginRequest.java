package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.manytomany.Origin;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OriginRequest {
    String username;
    Double lon;
    Double lat;
}
