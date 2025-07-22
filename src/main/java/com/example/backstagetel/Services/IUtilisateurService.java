package com.example.backstagetel.Services;

import com.example.backstagetel.Entities.Utilisateur;

import java.util.List;

public interface IUtilisateurService {

    public List<Utilisateur> getAllUsers();

    public String changePasswordFromToken(String username, String oldPassword, String newPassword);

    public void resetPassword(String email, String code, String newPassword);

    public List<Utilisateur> getAllClients();

}
