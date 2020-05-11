package com.ali.alisimulate.entity;

import java.util.List;

public class LoginSuccess {
    public String token;
    public UserDetail userDetail;

    public class UserDetail {
        public boolean accountNonExpired;
        public boolean accountNonLocked;
        public List<Authorities> authorities;
        public boolean credentialsNonExpired;
        public boolean enabled;
        public String id;
        public String lastPasswordResetDate;
        public int orgType;
        public String organizationId;
        public String password;
        public Role role;
        public String username;
    }

    public class Role {
        public int id;
        public String name;
        public String nameZh;
    }

    public class Authorities {
        public String authority;
    }
}
