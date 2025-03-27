package com.repositorio.avaliar.repository;

import com.repositorio.avaliar.model.TrabalhoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrabalhoAcademicoRepository extends JpaRepository<TrabalhoAcademico, UUID> {

    @Query("SELECT t.orientador, COUNT(t) as total FROM TrabalhoAcademico t GROUP BY t.orientador ORDER BY total DESC")
    List<Object[]> findTopOrientadores(Pageable pageable);

    @Query("SELECT t.universidade, COUNT(t) as total FROM TrabalhoAcademico t GROUP BY t.universidade ORDER BY total DESC")
    List<Object[]> findTopUniversidades(Pageable pageable);

    @Query("SELECT t.tipoProducao, COUNT(t) FROM TrabalhoAcademico t GROUP BY t.tipoProducao")
    List<Object[]> countByTipoProducao();

}
