package ru.dininslam.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BILL",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_bill_id",
                columnNames = "id"
        )
)
public class Bill {

    @Id
    @Column(name = "id",
            unique = true,
            nullable = false,
            columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "value",
            nullable = false,
            columnDefinition = "BIGINT")
    private Long value;

    @Column(name = "closed",
            nullable = false,
            columnDefinition = "BOOLEAN")
    private Boolean closed;

    @PrePersist
    void open() {
        value = 0L;
        closed = false;
    }
}
