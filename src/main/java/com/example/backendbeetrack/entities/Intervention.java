package com.example.backendbeetrack.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "interventions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String remarque;

    @ManyToOne
    @JoinColumn(name = "ruche_id", nullable = false)
    private Ruche ruche;
}
