package br.edu.controlplus.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// A chave única também protege contra duas requisições simultâneas para o mesmo dia.
@Entity
@Table(name = "progressos_diarios", uniqueConstraints =
    @UniqueConstraint(name = "uk_progresso_usuario_data", columnNames = {"usuario_id", "data"}))
public class ProgressoDiario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private Boolean metaCumprida;
    @Column(length = 1000)
    private String observacoes;

    public ProgressoDiario() { }
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getData() { return data; }
    public Boolean getMetaCumprida() { return metaCumprida; }
    public String getObservacoes() { return observacoes; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setData(LocalDate data) { this.data = data; }
    public void setMetaCumprida(Boolean metaCumprida) { this.metaCumprida = metaCumprida; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
