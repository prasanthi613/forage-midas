package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.model.User; // ✅ Use User model, not UserRecord
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    public void populate() {
        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");
        for (String userLine : userLines) {
            String[] userData = userLine.split(", ");
            User user = new User(); // ✅ Create User instance
            user.setId(userData[0]);
            user.setBalance(Double.parseDouble(userData[1])); // Assuming balance is double
            databaseConduit.save(user); // ✅ Save User, not UserRecord
        }
    }
}
