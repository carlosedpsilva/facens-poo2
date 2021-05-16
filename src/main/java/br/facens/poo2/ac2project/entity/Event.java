package br.facens.poo2.ac2project.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_EVENT")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {

  private static final long serialVersionUID = 1L;

  @ManyToOne
  @JoinColumn(name="ADMIN_BASE_USER_ID")
  private Admin admin;

  @OneToMany
  @JoinColumn(name="EVENT_ID")
  private final List<Ticket> tickets = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name="TB_EVENT_PLACE",
      joinColumns =  @JoinColumn(name="EVENT_ID"),
      inverseJoinColumns = @JoinColumn(name="PLACE_ID"))
  private final List<Place> places = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String place;

  @Column(nullable = false)
  private String name;

  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  @Column(nullable = true)
  private String emailContact;

  @Column(nullable = false)
  private Long amountFreeTickets;

  @Column(nullable = false)
  private Long amountPayedTickets;

  @Column(nullable = false)
    private Double priceTicket;

}
