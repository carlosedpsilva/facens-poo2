package br.facens.poo2.event.scheduler.service.meta;

public interface SchedulerService<T> {

  T verifyIfExists(long id);

}
