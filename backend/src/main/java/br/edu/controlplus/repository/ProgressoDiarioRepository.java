package br.edu.controlplus.repository;

import br.edu.controlplus.model.ProgressoDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// O Spring Data transforma os nomes dos métodos em consultas ao banco.
public interface ProgressoDiarioRepository extends JpaRepository<ProgressoDiario, Long> {
    List<ProgressoDiario> findByUsuarioIdOrderByDataDesc(Long usuarioId);
    Optional<ProgressoDiario> findByIdAndUsuarioId(Long id, Long usuarioId);
    boolean existsByUsuarioIdAndData(Long usuarioId, LocalDate data);
    boolean existsByUsuarioIdAndDataAndIdNot(Long usuarioId, LocalDate data, Long id);
}
