package com.example.backendbeetrack.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "recoltes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recolte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String typeMiel;
    private Double quantite;
    private String observation;

    @ManyToOne
    @JoinColumn(name = "rucher_id", nullable = false)
    private Rucher rucher;
}
