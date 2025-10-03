package com.example.backendbeetrack.services.impl;

import com.example.backendbeetrack.entities.Recolte;
import com.example.backendbeetrack.entities.User;
import com.example.backendbeetrack.repositories.*;
import com.example.backendbeetrack.services.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final RucherRepository rucherRepository;
    private final RucheRepository rucheRepository;
    private final RecolteRepository recolteRepository;
    private final RevenuRepository revenuRepository;
    private final DepenseRepository depenseRepository;
    private final UserRepository userRepository;

    public DashboardServiceImpl(RucherRepository rucherRepository,
                                RucheRepository rucheRepository,
                                RecolteRepository recolteRepository,
                                RevenuRepository revenuRepository,
                                DepenseRepository depenseRepository,
                                UserRepository userRepository) {
        this.rucherRepository = rucherRepository;
        this.rucheRepository = rucheRepository;
        this.recolteRepository = recolteRepository;
        this.revenuRepository = revenuRepository;
        this.depenseRepository = depenseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getDashboardData(Long userId) {
        Map<String, Object> data = new HashMap<>();

        // 1. Ruchers actifs
        long ruchersActifs = rucherRepository.findByUserId(userId).size();

        // 2. Total ruches
        long totalRuches = rucheRepository.countByRucherUserId(userId);

        // 3. Total récoltes
        long totalRecoltes = recolteRepository.countByRucherUserId(userId);

        // 4. Bénéfice net
        double revenus = revenuRepository.sumMontantByUserId(userId);
        double depenses = depenseRepository.sumMontantByUserId(userId);
        double beneficeNet = revenus - depenses;

        // 5. Production mensuelle de miel
        List<Recolte> recoltesMiel = recolteRepository.findMielRecoltesByUserId(userId);
        double[] productionParMois = new double[12]; // index 0 = janvier

        for (Recolte r : recoltesMiel) {
            int mois = LocalDate.ofInstant(r.getDate().toInstant(), java.time.ZoneId.systemDefault()).getMonthValue();
            productionParMois[mois - 1] += r.getQuantite(); // mois 1 → index 0
        }

        // Convertir en liste de { mois: 1, quantite: 12.5 } pour le frontend
        List<Map<String, Object>> productionMensuelle = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Map<String, Object> moisData = new HashMap<>();
            moisData.put("mois", i + 1);
            moisData.put("quantiteTotale", productionParMois[i]);
            productionMensuelle.add(moisData);
        }

        // Remplir la réponse
        data.put("ruchersActifs", ruchersActifs);
        data.put("totalRuches", totalRuches);
        data.put("totalRecoltes", totalRecoltes);
        data.put("beneficeNet", beneficeNet);
        data.put("productionMensuelle", productionMensuelle);

        return data;
    }

    @Override
    public Map<String, Object> getEtatSanteRuchesByEmail(String email) {
        User user = userRepository.findByEmail(email);
        List<Object[]> results = rucheRepository.countByEtatAndUserId(user.getId());
        long totalRuches = rucheRepository.countByRucherUserId(user.getId());

        // 🔑 Utilise les valeurs TELLES QUELLES en base
        Map<String, String> labels = new HashMap<>();
        labels.put("Active", "Active");
        labels.put("Malade", "Malade");
        labels.put("En Pause", "En Pause");

        // Initialiser avec les clés exactes
        Map<String, Long> counts = new HashMap<>();
        counts.put("Active", 0L);
        counts.put("Malade", 0L);
        counts.put("En Pause", 0L);

        // Remplir avec les résultats
        for (Object[] row : results) {
            String etat = (String) row[0]; // ← pas de toLowerCase()
            Long count = (Long) row[1];

            if (counts.containsKey(etat)) {
                counts.put(etat, count);
            }
            // Optionnel : log les états inconnus
            else {
                System.out.println("État inconnu trouvé en base : " + etat);
            }
        }

        // Ordre d'affichage souhaité
        List<String> ordre = Arrays.asList("Active", "Malade", "En Pause");

        List<Map<String, Object>> etats = new ArrayList<>();
        for (String key : ordre) {
            long count = counts.get(key);
            double pct = totalRuches > 0 ? Math.round(count * 100.0 / totalRuches * 100.0) / 100.0 : 0.0;
            etats.add(Map.of(
                    "etat", key.toLowerCase().replace(" ", ""), // optionnel : pour le frontend (ex: "enpause")
                    "libelle", labels.get(key),
                    "nombre", count,
                    "pourcentage", pct
            ));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalRuches", totalRuches);
        response.put("repartition", etats);
        return response;
    }}