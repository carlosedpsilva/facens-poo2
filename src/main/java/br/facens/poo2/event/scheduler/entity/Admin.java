package br.facens.poo2.event.scheduler.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_ADMIN")
@PrimaryKeyJoinColumn(name = "BASE_USER_ID")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends BaseUser {

  @Column(nullable = false)
  private String phoneNumber;

  @OneToMany(mappedBy = "admin")
  private final List<Event> events = new ArrayList<>();

}
