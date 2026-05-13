package com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto;

import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private String role;
    private Province province;
    private Boolean fuerzaFederal;

    public UserDto(){

    }
    public UserDto(Long id, String username , String nickname, Province province , String role , Boolean fuerzaFederal){
        this.id = id;
        this.username = username;
        this.province = province;
        this.nickname = nickname;
        this.role = role;
        this.fuerzaFederal = fuerzaFederal;
    }
}
