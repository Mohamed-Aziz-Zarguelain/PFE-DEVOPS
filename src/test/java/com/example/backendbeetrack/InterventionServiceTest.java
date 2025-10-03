package com.example.backendbeetrack;

import com.example.backendbeetrack.entities.Intervention;
import com.example.backendbeetrack.repositories.InterventionRepository;
import com.example.backendbeetrack.services.impl.InterventionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InterventionServiceTest {

    @Mock
    private InterventionRepository interventionRepository;

    @InjectMocks
    private InterventionServiceImpl interventionService;

    private Intervention intervention;

    @BeforeEach
    void setUp() {
        intervention = new Intervention();
        intervention.setId(1L);
        intervention.setDate(new Date());
        // Remove type and description if they don't exist in your entity
    }

    @Test
    void testGetAllInterventions() {
        // Given
        List<Intervention> interventions = Arrays.asList(intervention);
        when(interventionRepository.findAll()).thenReturn(interventions);

        // When
        List<Intervention> result = interventionService.getAllInterventions();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(interventionRepository, times(1)).findAll();
    }

    @Test
    void testGetInterventionById() {
        // Given
        when(interventionRepository.findById(1L)).thenReturn(Optional.of(intervention));

        // When
        Optional<Intervention> result = interventionService.getInterventionById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertNotNull(result.get().getDate());
    }

    @Test
    void testCreateIntervention() {
        // Given
        when(interventionRepository.save(any(Intervention.class))).thenReturn(intervention);

        // When
        Intervention result = interventionService.createIntervention(intervention);

        // Then
        assertNotNull(result);
        assertEquals(intervention.getId(), result.getId());
        assertNotNull(result.getDate());
        verify(interventionRepository, times(1)).save(any(Intervention.class));
    }
}