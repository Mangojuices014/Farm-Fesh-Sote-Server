package com.kira.farm_fresh_store.entity.order;

import com.kira.farm_fresh_store.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="php_payment_method")
public class PaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "method", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "type")
    private Integer type;

    @Column(name = "display")
    private Integer display; // 1 : show, 0: hidden
}
