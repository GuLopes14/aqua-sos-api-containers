package br.com.aquasos.model;

import java.time.LocalDate;

import br.com.aquasos.model.enums.StatusPedido;
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
import jakarta.validation.constraints.PastOrPresent;
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

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne@JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne@JoinColumn(name = "ponto_distribuicao_id")
    private PontoDistribuicao ponto;

    @NotNull@Min(1)
    private Integer quantidadeLitros;

    @PastOrPresent
    private LocalDate dataSolicitacao;

    @Enumerated(EnumType.STRING)private StatusPedido status;
}