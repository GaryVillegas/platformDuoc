package com.backend.platformDuoc.dto;

public class UserDTO {
    private Integer id;
    private String name;
    private String lastname;

    // Constructor: con esto armamos la cajita
    public UserDTO(Integer id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
    }

    public UserDTO() {}

    // Getters: para sacar lo que hay en la cajita
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
}
