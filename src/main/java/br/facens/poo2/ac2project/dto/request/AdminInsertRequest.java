package br.facens.poo2.ac2project.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminInsertRequest {
    
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 200, message = "Name must be between 2 and 200 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 11, max = 11, message = "Phone number must contain 11 characters")
    private String phoneNumber;
}
