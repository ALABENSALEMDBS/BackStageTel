package com.example.backstagetel.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "Utilisateur", uniqueConstraints = @UniqueConstraint(columnNames = "emailUser"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;

    private String nomUser;
    private String prenomUser;
    private String photoUser;
    private String emailUser;
    private String passwordUser;
    private Date createdAt;
    private int numeroLigne;
    private String documentContrat;

    @Enumerated(EnumType.STRING)
    private EtatCompte etatCompte;

    @ManyToOne
    private Role role;

    @OneToMany (cascade = CascadeType.ALL)
    Set<Avis> avis= new HashSet<>();

    @OneToMany(mappedBy = "utilisateurRens", cascade = CascadeType.ALL)
    Set<Renseignement> renseignements= new HashSet<>();

    @OneToMany(mappedBy = "utilisateurRecl", cascade = CascadeType.ALL)
    Set<Reclamation> reclamations= new HashSet<>();




    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
           return List.of(new SimpleGrantedAuthority(role.getNomRole()));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return passwordUser;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return emailUser;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }
}
