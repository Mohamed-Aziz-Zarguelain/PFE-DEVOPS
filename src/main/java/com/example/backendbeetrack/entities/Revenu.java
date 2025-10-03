package com.example.backendbeetrack.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;
@Entity
@Table(name = "revenus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private Double montant;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
