package br.edu.controlplus.repository;

import br.edu.controlplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository: o Spring Data JPA ja fornece save, findAll, findById e delete.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
