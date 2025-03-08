package com.kira.farm_fresh_store.entity.order;

import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.utils.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "php_transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String billNo;
    @Column(name = "trans_no", nullable = false, unique = true)
    private String transNo;
    private String bankCode;
    private String cardType;
    private Long amount;
    private String currency;
    private String refundBankCode;
    @Lob
    private String reason;
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order tblOrder;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
