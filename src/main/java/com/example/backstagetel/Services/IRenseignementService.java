package com.example.backstagetel.Services;

import com.example.backstagetel.Entities.Renseignement;

import java.util.List;

public interface IRenseignementService {

    public List<Renseignement> findAllRenseignements();

    public Renseignement ajouterRenseignement(Renseignement renseignement, int idUser);

    public Renseignement repondreRenseignement(int idRenseignement, Renseignement newRenseignement);

    public List<Renseignement> getRenseignementByUser(int idUser);
}
