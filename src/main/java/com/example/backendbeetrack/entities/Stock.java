package com.example.backendbeetrack.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String produit; // Miel, Pollen, etc.
    private Double quantite;
    private String unite; // Par exemple : kg, litre, etc.

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
