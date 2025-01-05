//package com.kira.farm_fresh_store.entity.user;
//
//import com.kira.farm_fresh_store.entity.BaseEntity;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "address")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//public class Address extends BaseEntity {
//
//    @Id
//    private String addressId;
//
//    @Column(name = "city")
//    private String city;
//
//    @Column(name = "city_id")
//    private Integer city_id;
//
//    @Column(name = "district")
//    private String district;
//
//    @Column(name = "district_id")
//    private Integer district_id;
//
//    @Column(name = "ward")
//    private String ward;
//
//    @Column(name = "ward_id")
//    private String ward_id;
//    @Column(name = "house")
//    private String house;
//
//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "user_id")
//    private User user;
//}