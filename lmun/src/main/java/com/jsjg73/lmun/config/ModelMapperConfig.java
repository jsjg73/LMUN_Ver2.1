package com.jsjg73.lmun.config;

import com.jsjg73.lmun.dto.*;
import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.manytomany.Participant;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        propertyMappings(modelMapper);
        return modelMapper;
    }

    private void propertyMappings(ModelMapper modelMapper) {
        TypeMap<Meeting, MeetingDto> meetingDtoTypeMap = modelMapper.createTypeMap(Meeting.class, MeetingDto.class);
        TypeMap<Meeting, MeetingParticipantsDto> meetingParticipantsDtoTypeMap = modelMapper.createTypeMap(Meeting.class, MeetingParticipantsDto.class);
        TypeMap<Meeting, ParticipantsResponse> meetingParticipantsResponseTypeMap = modelMapper.createTypeMap(Meeting.class, ParticipantsResponse.class);

        Converter<Set<Participant>, Integer> toParticipantsCount =
                ctx-> ctx.getSource().size();
        Converter<Set<Participant>, Set<String>> toParticipants =
                ctx-> ctx.getSource().stream().map(p->p.getUser().getUsername()).collect(Collectors.toSet());
        Converter<Set<Participant>, List<ParticipantResponse>> toParticipantResponseList =
                ctx -> ctx.getSource().stream().map(p->{
                    ParticipantResponse res = new ParticipantResponse();
                    res.setUsername(p.getUser().getUsername());
                    res.setDeparture(modelMapper.map(p.getDeparture(), LocationDto.class));
                    return res;
                }).collect(Collectors.toList());

        meetingDtoTypeMap.addMappings(mapper ->{
            mapper.map(src->src.getHost().getUsername(), MeetingDto::setHost);
            mapper.using(toParticipantsCount).map(Meeting::getParticipants, MeetingDto::setParticipantsCount);
        });


        meetingParticipantsDtoTypeMap.addMappings(mapper->{
            mapper.map(src->src.getHost().getUsername(), MeetingDto::setHost);
            mapper.using(toParticipantsCount).map(Meeting::getParticipants, MeetingParticipantsDto::setParticipantsCount);
            mapper.using(toParticipants).map(Meeting::getParticipants, MeetingParticipantsDto::setParticipants);
        });

        meetingParticipantsResponseTypeMap.addMappings(mapper->{
           mapper.using(toParticipantResponseList).map(Meeting::getParticipants, ParticipantsResponse::setParticipants);
        });

    }
}
