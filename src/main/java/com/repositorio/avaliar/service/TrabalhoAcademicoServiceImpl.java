package com.repositorio.avaliar.service;

import com.repositorio.avaliar.model.TrabalhoAcademico;
import com.repositorio.avaliar.repository.TrabalhoAcademicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrabalhoAcademicoServiceImpl implements TrabalhoAcademicoService {

    @Autowired
    public TrabalhoAcademicoServiceImpl(TrabalhoAcademicoRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private TrabalhoAcademicoRepository repository;

    @Override
    public List<TrabalhoAcademico> listarTrabalhos() {
        return repository.findAll();
    }

    @Override
    public TrabalhoAcademico buscarTrabalhoPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Trabalho acadêmico não encontrado!"));
    }

    @Override
    public TrabalhoAcademico criarTrabalho(TrabalhoAcademico trabalho) {
        return repository.save(trabalho);
    }

    @Override
    public TrabalhoAcademico atualizarTrabalho(UUID id, TrabalhoAcademico trabalho) {
        return repository.findById(id)
                .map(trabalhoExistente -> {
                    trabalhoExistente.setTitulo(trabalho.getTitulo());
                    trabalhoExistente.setTipoProducao(trabalho.getTipoProducao());
                    trabalhoExistente.setAno(trabalho.getAno());
                    trabalhoExistente.setAutor(trabalho.getAutor());
                    trabalhoExistente.setOrientador(trabalho.getOrientador());
                    trabalhoExistente.setPalavrasChave(trabalho.getPalavrasChave());
                    trabalhoExistente.setUniversidade(trabalho.getUniversidade());
                    trabalhoExistente.setLink(trabalho.getLink());
                    return repository.save(trabalhoExistente);
                }).orElseThrow(() -> new RuntimeException("Trabalho acadêmico não encontrado!"));
    }

    @Override
    public void deletarTrabalho(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Trabalho acadêmico não encontrado!");
        }
        repository.deleteById(id);
    }

    public List<Map<String, Object>> getTopOrientadores() {
        List<Object[]> resultados = repository.findTopOrientadores(PageRequest.of(0, 4));
        return resultados.stream().map(obj -> Map.of("orientador", obj[0], "quantidade", obj[1])).toList();
    }

    public List<Map<String, Object>> getTopUniversidades() {
        Pageable pageable = PageRequest.of(0, 4);
        List<Object[]> resultados = repository.findTopUniversidades(pageable);
        return resultados.stream()
                .map(obj -> Map.of("universidade", obj[0], "quantidade", obj[1]))
                .toList();
    }

    public List<Map<String, Object>> getTipoProducaoCount() {
        List<Object[]> resultados = repository.countByTipoProducao();
        return resultados.stream().map(obj -> Map.of("tipoProducao", obj[0], "quantidade", obj[1])).toList();
    }

    public Map<String, Long> getPalavrasChaveMaisFrequentes() {
        List<TrabalhoAcademico> trabalhos = repository.findAll();

        return trabalhos.stream()
                .flatMap(t -> Arrays.stream(t.getPalavrasChave().split(";")))
                .map(String::trim)
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }


}
