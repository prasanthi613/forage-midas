package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {

    @Autowired
    private UserRepository userRepository;

    public void save(UserRecord user) {
        userRepository.save(user);
    }

    public UserRecord findUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
