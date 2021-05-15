package br.facens.poo2.ac2project.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.facens.poo2.ac2project.dto.request.AdminInsertRequest;
import br.facens.poo2.ac2project.dto.request.AdminUpdateRequest;
import br.facens.poo2.ac2project.dto.response.AdminResponse;
import br.facens.poo2.ac2project.dto.response.MessageResponse;
import br.facens.poo2.ac2project.exception.AdminNotFoundException;
import br.facens.poo2.ac2project.exception.EmptyRequestException;
import br.facens.poo2.ac2project.service.AdminService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
    private AdminService adminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse save(@RequestBody @Valid AdminInsertRequest adminInsertRequest) {
      return adminService.save(adminInsertRequest);
    }
  
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AdminResponse> findAll(
        @RequestParam(value =          "page", defaultValue =   "0")  Integer page,
        @RequestParam(value =  "linesPerPage", defaultValue =   "8")  Integer linesPerPage,
        @RequestParam(value =     "direction", defaultValue = "ASC")   String direction,
        @RequestParam(value =       "orderBy", defaultValue =  "id")   String orderBy,
        @RequestParam(value =          "name", defaultValue =    "")   String name,
        @RequestParam(value =         "email", defaultValue =    "")   String email,
        @RequestParam(value =   "phoneNumber", defaultValue =    "")   String phoneNumber
    ) {
      PageRequest pageRequest = PageRequest.of(0, 8, Direction.valueOf(direction), orderBy);
      return adminService.findAll(pageRequest, name, email, phoneNumber);
    }
  
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdminResponse findById(@PathVariable Long id) throws AdminNotFoundException {
      return adminService.findById(id);
    }
  
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse deleteById(@PathVariable Long id) throws AdminNotFoundException {
      return adminService.deleteById(id);
    }
  
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse updateById(@PathVariable Long id, @RequestBody @Valid AdminUpdateRequest adminUpdateRequest)
        throws AdminNotFoundException, EmptyRequestException {
      return adminService.updateById(id, adminUpdateRequest);
    }
}
