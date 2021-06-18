package br.facens.poo2.event.scheduler.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_PLACE")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Place implements Serializable{

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;

  @ManyToMany
  @JoinTable(
      name="TB_EVENT_PLACE",
      joinColumns =  @JoinColumn(name="PLACE_ID"),
      inverseJoinColumns = @JoinColumn(name="EVENT_ID"))
  private final List<Event> events = new ArrayList<>();

}
