package com.example.backendbeetrack;

import com.example.backendbeetrack.entities.Rucher;
import com.example.backendbeetrack.repositories.RucherRepository;
import com.example.backendbeetrack.services.impl.RucherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RucherServiceTest {

    @Mock
    private RucherRepository rucherRepository;

    @InjectMocks
    private RucherServiceImpl rucherService;

    private Rucher rucher;

    @BeforeEach
    void setUp() {
        rucher = new Rucher();
        rucher.setId(1L);
        rucher.setNom("Test Rucher");
    }

    @Test
    void testGetAllRuchers() {
        // Given
        List<Rucher> ruchers = Arrays.asList(rucher);
        when(rucherRepository.findAll()).thenReturn(ruchers);

        // When
        List<Rucher> result = rucherService.getAllRuchers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rucherRepository, times(1)).findAll();
    }

    @Test
    void testGetRucherById() {
        // Given
        when(rucherRepository.findById(1L)).thenReturn(Optional.of(rucher));

        // When
        Optional<Rucher> result = rucherService.getRucherById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Test Rucher", result.get().getNom());
    }

    @Test
    void testCreateRucher() {
        // Given
        when(rucherRepository.save(any(Rucher.class))).thenReturn(rucher);

        // When
        Rucher result = rucherService.createRucher(rucher);

        // Then
        assertNotNull(result);
        assertEquals(rucher.getId(), result.getId());
        verify(rucherRepository, times(1)).save(any(Rucher.class));
    }



}