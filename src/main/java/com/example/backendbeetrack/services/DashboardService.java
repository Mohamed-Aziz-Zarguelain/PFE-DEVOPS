package com.example.backendbeetrack.services;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardData(Long userId);
    Map<String, Object> getEtatSanteRuchesByEmail(String email);

}
