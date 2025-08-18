package com.backend.platformDuoc.dto;

import com.backend.platformDuoc.models.Activity;

public class ActivityDTO {

    private Activity activity_dto;
    private Integer id_user;
    private String user_name;
    private String user_lastname;

    public ActivityDTO(Activity activity){
        this.activity_dto = activity;
        this.id_user = activity.getUser().getId();
        this.user_name = activity.getUser().getName();
        this.user_lastname = activity.getUser().getLastname();
    }

    public Activity getActivity() {return activity_dto;}
    public Integer getId_User(){return id_user;}
    public String getUser_Name(){return user_name;}
    public String getUser_LastName() { return user_lastname; }

}