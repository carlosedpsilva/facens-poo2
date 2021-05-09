package br.facens.poo2.ac2project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_attendee")
@PrimaryKeyJoinColumn(name="BASE_USER_ID")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Attendee extends BaseUser{
    
    @OneToMany
    @JoinColumn(name="ATTENDEE_BASE_USER_ID")
    private final List<Ticket> tickets = new ArrayList<>();
    
    @Column(nullable = true)
    private double balance;
}
