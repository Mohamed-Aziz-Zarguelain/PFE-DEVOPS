package com.example.backendbeetrack.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "taches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private Date date;
    private boolean accomplie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
