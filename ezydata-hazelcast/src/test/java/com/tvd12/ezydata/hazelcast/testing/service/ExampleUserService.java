package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.testing.entity.ExampleUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExampleUserService {

    void saveUser(ExampleUser user);

    void saveUser(List<ExampleUser> users);

    ExampleUser getUser(String username);

    Map<String, ExampleUser> getUsers(Set<String> usernames);

    void deleteUser(String username);

    void deleteAllUser();
}
