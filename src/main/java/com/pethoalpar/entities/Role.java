package com.pethoalpar.entities;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Role{

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, precision = 11)
    private Integer id;

    @NotBlank
    @Column(name = "role_name", nullable = false, length = 255)
    private String roleName;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = User.class, mappedBy = "role", cascade = {CascadeType.DETACH})
    private Collection<User> users;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Collection<User> getUsers() {
        return this.users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    /**
     * @param e
     */
    public void addToUsers(User e) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        e.setRole(this);
        this.users.add(e);
    }


}

