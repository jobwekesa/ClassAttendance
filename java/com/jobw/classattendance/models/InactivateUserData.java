package com.adivid.zpattendanceadmin.models;

import java.util.List;

public class InactivateUserData {
    private List<InactivateUserListModel> users = null;
    private Boolean result;

    public List<InactivateUserListModel> getUsers() {
        return users;
    }

    public void setUsers(List<InactivateUserListModel> users) {
        this.users = users;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
