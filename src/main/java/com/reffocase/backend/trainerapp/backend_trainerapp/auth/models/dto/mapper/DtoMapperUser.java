package com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto.mapper;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto.UserDto;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;

public class DtoMapperUser {

    private User user;

    private DtoMapperUser() {

    }

    public static DtoMapperUser builder(){
        return new DtoMapperUser();
    }

    public DtoMapperUser setUser(User user){
        this.user = user;
        return this;
    }

    public UserDto build(){
        if(user==null){
            throw new RuntimeException("Debe pasar el entity user!");
        }
        return new UserDto(user.getId(),user.getUsername(), user.getNickname(),user.getProvince(),user.getRole().getName());
    }

}
