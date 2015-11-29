package com.hackathon.congente.callbacks;

import com.hackathon.congente.datatype.User;

public interface LoginCallback {
    void LoginResult(boolean result, User user);
}
