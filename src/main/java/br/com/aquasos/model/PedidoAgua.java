package br.com.aquasos.model;

import br.com.aquasos.model.enums.NivelUrgencia;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoAgua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;

    @NotNull(message = "Quantidade de litros é obrigatória")
    @Min(value = 1, message = "Quantidade de litros deve ser maior que zero")
    private Integer quantidadeLitros;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Nível de urgência é obrigatório")
    private NivelUrgencia nivelUrgencia;

    @NotNull(message = "Endereço é obrigatório")
    private String endereco;
}