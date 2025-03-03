package com.kira.farm_fresh_store.entity.order;

import com.kira.farm_fresh_store.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="php_payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")  // Tên cột trong DB
    private PaymentMethod method; // Thêm thuộc tính này

    private String bankName;
    private String datePayment;
    private String tradingCode;
    private Integer status;
}

