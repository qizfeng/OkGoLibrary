package com.project.community.model;

import java.io.Serializable;

/**
 * Created by qizfeng on 17/7/21.
 */

public class UserResponse implements Serializable{
    public UserModel user = new UserModel();

    @Override
    public String toString() {
        return "UserResponse{" +
                "user=" + user +
                '}';
    }
}
