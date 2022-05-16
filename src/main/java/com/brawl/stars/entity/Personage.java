package com.brawl.stars.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "PERSONAGE")
public class Personage extends EntityIdUUID {

    @Column(name = "NAME")
    private String name;

    @Column(name = "RARE")
    @Enumerated(value = EnumType.STRING)
    private Rare rare;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ID", referencedColumnName = "ID")
    private Specification specification;
}
