package com.repositorio.avaliar.controller;

import com.repositorio.avaliar.model.TrabalhoAcademico;
import com.repositorio.avaliar.service.TrabalhoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/trabalhos")
@CrossOrigin(origins = "http://localhost:3000")
public class TrabalhoAcademicoController {

    @Autowired
    private TrabalhoAcademicoService service;

    @GetMapping
    public ResponseEntity<List<TrabalhoAcademico>> listarTrabalhos() {
        return ResponseEntity.ok(service.listarTrabalhos());
    }

    @GetMapping("/top-orientadores")
    public List<Map<String, Object>> getTopOrientadores() {
        return service.getTopOrientadores();
    }

    @GetMapping("/top-universidades")
    public List<Map<String, Object>> getTopUniversidades() {
        return service.getTopUniversidades();
    }

    @GetMapping("/tipo-producao-count")
    public List<Map<String, Object>> getTipoProducaoCount() {
        return service.getTipoProducaoCount();
    }

    @GetMapping("/palavras-chave")
    public Map<String, Long> getPalavrasChaveMaisFrequentes() {
        return service.getPalavrasChaveMaisFrequentes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrabalhoAcademico> buscarTrabalhoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarTrabalhoPorId(id));
    }

    @PostMapping
    public ResponseEntity<TrabalhoAcademico> criarTrabalho(@RequestBody TrabalhoAcademico trabalho) {
        return ResponseEntity.status(201).body(service.criarTrabalho(trabalho));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrabalhoAcademico> atualizarTrabalho(@PathVariable UUID id, @RequestBody TrabalhoAcademico trabalho) {
        return ResponseEntity.ok(service.atualizarTrabalho(id, trabalho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTrabalho(@PathVariable UUID id) {
        service.deletarTrabalho(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo-producao-per-year")
    public List<Map<String, Object>> getTipoProducaoPerYear() {
        return service.getTipoProducaoPerYear();
    }
}
