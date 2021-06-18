package br.facens.poo2.event.scheduler.entity;

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
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_ATTENDEE")
@PrimaryKeyJoinColumn(name = "BASE_USER_ID")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Attendee extends BaseUser {

  @Column(nullable = false)
  private double balance;

  @OneToMany
  @JoinColumn(name = "ATTENDEE_ID")
  private final List<Ticket> tickets = new ArrayList<>();

}
