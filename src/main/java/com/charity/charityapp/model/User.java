// src/main/java/com/charity/charityapp/model/User.java
package com.charity.charityapp.model;

import com.charity.charityapp.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Entity
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ← NEW: track which actions this user follows
    @ManyToMany
    @JoinTable(
            name = "user_action_follows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Set<CharityAction> followedActions = new HashSet<>();
}
