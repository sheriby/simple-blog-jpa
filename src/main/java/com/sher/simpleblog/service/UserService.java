package com.sher.simpleblog.service;

import com.sher.simpleblog.entity.User;

public interface UserService {

    User checkUser(String username, String password);
}
