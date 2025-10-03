package com.example.backendbeetrack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "ruches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String type;
    private String etat;

    @ManyToOne
    @JoinColumn(name = "rucher_id", nullable = false)
    private Rucher rucher;

    @OneToMany(mappedBy = "ruche", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Intervention> interventions = new ArrayList<>();
}
