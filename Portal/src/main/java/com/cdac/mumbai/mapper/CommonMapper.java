package com.cdac.mumbai.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.cdac.mumbai.model.BillDetails;
import com.cdac.mumbai.model.Ip;

//import lombok.RequiredArgsConstructor;

@Component
//@RequiredArgsConstructor
public class CommonMapper {
    
    private ModelMapper modelMapper;
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public <T, S> S mapToEntity(T data, Class<S> type) {
        return modelMapper.map(data, type);
    }

    public <T, S> S mapToDto(T data, Class<S> type) {
        return modelMapper.map(data, type);
    }

    public <T, S> List<S> convertToResponseList(Optional<List<Ip>> optional, Class<S> type) {
        return optional.stream()
                .map(list -> mapToDto(list, type))
                .collect(Collectors.toList());
    }
}
