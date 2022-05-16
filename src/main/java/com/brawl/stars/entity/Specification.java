package com.brawl.stars.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SPECIFICATIONS")
public class Specification extends EntityIdUUID{

    @Column(name = "HEALTH")
    private int health;

    @Column(name = "SPEED")
    private String speed;

    @Column(name = "DAMAGE")
    private String damage;

    @Column(name ="RANGE")
    private String rang;

    @Column(name ="RECHARGE_RATE")
    private String rechargeRate;
}
