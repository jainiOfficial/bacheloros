package com.bacheloros.bacheloros_backend.dto;

public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;

    public UserResponse(Long id, String email, String name,String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

}
