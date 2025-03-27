package com.repositorio.avaliar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repositorio.avaliar.controller.TrabalhoAcademicoController;
import com.repositorio.avaliar.model.TrabalhoAcademico;
import com.repositorio.avaliar.repository.TrabalhoAcademicoRepository;
import com.repositorio.avaliar.service.TrabalhoAcademicoService;
import com.repositorio.avaliar.service.TrabalhoAcademicoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrabalhoAcademicoController.class)

public class TrabalhoAcademicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrabalhoAcademicoService service; // Só mocka a interface

    @Autowired
    private ObjectMapper objectMapper;


    private TrabalhoAcademico criarTrabalhoAcademico(UUID id) {
        return new TrabalhoAcademico(
                id,
                "Trabalho 1",
                "Artigo",
                2022,
                "Autor 1",
                "Orientador 1",
                "Palavras-chave 1",
                "Universidade 1",
                "https://example.com"
        );
    }

    @Test
    public void testListarTrabalhos() throws Exception {
        List<TrabalhoAcademico> trabalhos = Arrays.asList(
                criarTrabalhoAcademico(UUID.randomUUID()),
                criarTrabalhoAcademico(UUID.randomUUID())
        );

        when(service.listarTrabalhos()).thenReturn(trabalhos);

        mockMvc.perform(MockMvcRequestBuilders.get("/trabalhos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo", is("Trabalho 1")))
                .andExpect(jsonPath("$[1].titulo", is("Trabalho 1")));
    }

    @Test
    public void testBuscarTrabalhoPorId() throws Exception {
        UUID id = UUID.randomUUID();
        TrabalhoAcademico trabalho = criarTrabalhoAcademico(id);

        when(service.buscarTrabalhoPorId(id)).thenReturn(trabalho);

        mockMvc.perform(MockMvcRequestBuilders.get("/trabalhos/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.titulo", is("Trabalho 1")));
    }

    @Test
    public void testCriarTrabalho() throws Exception {
        TrabalhoAcademico trabalho = criarTrabalhoAcademico(UUID.randomUUID());

        when(service.criarTrabalho(any(TrabalhoAcademico.class))).thenReturn(trabalho);

        mockMvc.perform(MockMvcRequestBuilders.post("/trabalhos")
                        .content(objectMapper.writeValueAsString(trabalho))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is("Trabalho 1")));
    }

    @Test
    public void testAtualizarTrabalho() throws Exception {
        UUID id = UUID.randomUUID();
        TrabalhoAcademico trabalho = criarTrabalhoAcademico(id);

        when(service.atualizarTrabalho(eq(id), any(TrabalhoAcademico.class))).thenReturn(trabalho);

        mockMvc.perform(MockMvcRequestBuilders.put("/trabalhos/" + id)
                        .content(objectMapper.writeValueAsString(trabalho))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Trabalho 1")));
    }

    @Test
    public void testDeletarTrabalho() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(service).deletarTrabalho(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/trabalhos/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }



}
