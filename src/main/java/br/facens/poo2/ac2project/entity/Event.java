package br.facens.poo2.ac2project.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_event")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;
  
  @Column(nullable = true)
  private String description;
  
  @Column(nullable = false)
  private String place;
  
  @Column(nullable = false)
  private LocalDate startDate;
  
  @Column(nullable = false)
  private LocalDate endDate;
  
  @Column(nullable = false)
  private LocalTime startTime;
  
  @Column(nullable = false)
  private LocalTime endTime;
  
  @Column(nullable = true)
  private String email;

}
