package br.edu.controlplus.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Entity: representa a tabela usuarios no PostgreSQL.
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer metaDias;

    @Column(nullable = false)
    private LocalDate dataInicio;

    public Usuario() { }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Integer getMetaDias() { return metaDias; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setMetaDias(Integer metaDias) { this.metaDias = metaDias; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
}
