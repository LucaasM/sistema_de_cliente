package io.github.lucaasm.clientes.domain.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Servico servico = (Servico) o;
        return id != null && Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
