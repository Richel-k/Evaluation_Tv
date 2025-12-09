package com.Application.EvaluationTV.security;


import com.Application.EvaluationTV.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Getter
public class CustomUserDetails implements UserDetails {
    
    private final User person;
    private final String userType; // "CANDIDAT" ou "EXAMINATEUR"
    
    public CustomUserDetails(User person, String userType) {
        this.person = person;
        this.userType = userType;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retourne le rôle basé sur le type d'utilisateur
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + userType)
        );
    }
    
    @Override
    public String getPassword() {
        return person.getMotDePasse();
    }
    
    @Override
    public String getUsername() {
        return person.getEmail();
    }

    //@Override
    public String getMyEmail(){
        return person.getEmail();
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
    
    // Méthodes utilitaires
    public Long getId() {
        return person.getId();
    }
    
    public String getNomComplet() {
        
        return person.getPrenom() + " " + person.getUsername();
    }
    
    public boolean isCandidat() {
        return "CANDIDAT".equals(userType);
    }
    
    public boolean isExaminateur() {
        return "EXAMINATEUR".equals(userType);
    }
}
