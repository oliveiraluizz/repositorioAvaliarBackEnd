package com.repositorio.avaliar;

import com.repositorio.avaliar.model.TrabalhoAcademico;
import com.repositorio.avaliar.repository.TrabalhoAcademicoRepository;
import com.repositorio.avaliar.service.TrabalhoAcademicoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrabalhoAcademicoServiceTest {

    @InjectMocks
    private TrabalhoAcademicoServiceImpl service;

    @Mock
    private TrabalhoAcademicoRepository repository;

    @Test
    public void testGetTopOrientadores() {
        List<Object[]> mockResult = List.of(
                new Object[]{"Maria", 5L},
                new Object[]{"João", 3L}
        );

        when(repository.findTopOrientadores(any())).thenReturn(mockResult);

        var result = service.getTopOrientadores();

        assertEquals(2, result.size());
        assertEquals("Maria", result.get(0).get("orientador"));
        assertEquals(5L, result.get(0).get("quantidade"));
    }

    @Test
    public void testGetTopUniversidades() {
        List<Object[]> mockData = List.of(
                new Object[]{"UFBA", 10L},
                new Object[]{"USP", 8L}
        );

        when(repository.findTopUniversidades(any(Pageable.class))).thenReturn(mockData);

        List<Map<String, Object>> result = service.getTopUniversidades();

        assertEquals(2, result.size());
        assertEquals("UFBA", result.get(0).get("universidade"));
    }

    @Test
    public void testGetTipoProducaoCount() {
        List<Object[]> mockResult = List.of(
                new Object[]{"Tese", 6L},
                new Object[]{"Dissertação", 4L}
        );

        when(repository.countByTipoProducao()).thenReturn(mockResult);

        var result = service.getTipoProducaoCount();

        assertEquals(2, result.size());
        assertEquals("Tese", result.get(0).get("tipoProducao"));
        assertEquals(6L, result.get(0).get("quantidade"));
    }

    @Test
    public void testGetPalavrasChaveMaisFrequentes() {
        TrabalhoAcademico t1 = new TrabalhoAcademico();
        t1.setPalavrasChave("Educação;Avaliação;Ensino");

        TrabalhoAcademico t2 = new TrabalhoAcademico();
        t2.setPalavrasChave("Avaliação;Tecnologia");

        when(repository.findAll()).thenReturn(List.of(t1, t2));

        var result = service.getPalavrasChaveMaisFrequentes();

        assertEquals(4, result.size());
        assertTrue(result.containsKey("Avaliação"));
        assertEquals(2L, result.get("Avaliação"));
    }
}

