package com.example.sheduleApp.sheduleApp.domain;


import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "usr")
public class  User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;

    private String email;
    private boolean active;
    private String activationCode;


    @OneToMany(mappedBy="userWhoCreated")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private Set<Consultation> createdConsultations;
    @OneToMany(mappedBy="userWhoReserved")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private Set<Consultation> reservedConsultations;

    public User(String name, String password,String email) {
        this.username = name;
        this.password = password;
        this.email = email;
        this.active = false;
    }

    public User() {
    }

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public boolean isMaster(){
        return roles.contains(Role.MASTER);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Consultation> getCreatedConsultations() {
        return createdConsultations;
    }

    public void setCreatedConsultations(Set<Consultation> createdConsultations) {
        this.createdConsultations = createdConsultations;
    }

    public Set<Consultation> getReservedConsultations() {
        return reservedConsultations;
    }

    public void setReservedConsultations(Set<Consultation> reservedConsultations) {
        this.reservedConsultations = reservedConsultations;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public List<String> getStringRoles(){
        List<String> stringRoles = new ArrayList<String>();
        for (Role role : roles){
            stringRoles.add(role.name());
        }
        return stringRoles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(createdConsultations, user.createdConsultations) &&
                Objects.equals(reservedConsultations, user.reservedConsultations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, createdConsultations, reservedConsultations);
    }


}