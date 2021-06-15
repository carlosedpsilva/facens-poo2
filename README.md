# Event Scheduler REST API

Project developed as an activity for the Object Oriented Programming 2 class of the Computer Engineering course at FACENS.

Deployed on: https://facens-poo2.herokuapp.com/

Swagger: https://facens-poo2.herokuapp.com/swagger-ui/

# Contributors

- 190715 | Carlos Eduardo do Prado Silva
- 190654 | Gabriel Maciel Silvério

# Request Bodies

## Admin

### Admin Insert Request

```json
{
  "name": "Person G",
  "email": "example7@email.com",
  "phoneNumber": "(99) 99999-9999"
}
```

### Admin Update Request

```json
{
  "email": "anotherexample1@email.com",
  "phoneNumber": "(99) 99999-9999"
}
```

## Attendee

### Attendee Insert Request

```json
{
  "name": "Person H",
  "email": "example8@email.com"
}
```

### Attendee Update Request

```json
{
  "email": "anotherexample2@email.com"
}
```

## Event

### Event Insert Request

```json
{
  "adminId": 1,
  "name": "Event D",
  "description": "OptionalDescription",
  "startDate": "11/03/2021",
  "endDate": "15/03/2021",
  "startTime": "11:00",
  "endTime": "15:00",
  "email": "example@email.com",
  "amountFreeTickets": 3,
  "amountPayedTickets": 10,
  "priceTicket": 200
}
```

### Event Update Request

```json
{
  "name": "", // blank fields are not updated
  "description": "NewOptinalDescription",
  "email": "anotherexample@email.com"
}
```

## Place

### Place Insert Request

```json
{
  "name": "SomeAnotherPlace",
  "address": "Address D"
}
```

### Place Update Request
```json
{
  "address": "Another Address"
}
```


## Ticket

### Ticket Insert Request
```json
{
  "type": "PAID", // or "FREE"
  "attendeeId": "1"
}

```

# Class Diagram

<img alt="Class Diagram Image" src="docs/diagrams/out/architecture_overview/architecture_overview.png" width="300" height="100%">
