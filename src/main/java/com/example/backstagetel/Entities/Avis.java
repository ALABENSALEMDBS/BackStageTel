package com.example.backstagetel.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAvis;

    private String descriptionAvis;
    private int note;



}
