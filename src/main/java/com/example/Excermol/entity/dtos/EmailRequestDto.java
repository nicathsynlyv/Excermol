package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.EmailStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EmailRequestDto {


    @NotBlank(message = "Mövzu boş ola bilməz")
    @Size(max = 250,message = "Mövzu 255 simvoldan çox ola bilməz")
    private String subject;

    @NotBlank(message = "Email məzmunu boş ola bilməz")
    private String body;

    @NotBlank(message = "Status boş ola bilməz")
    private EmailStatus status;

    private LocalDateTime sentAt;

    @Size(max = 10,message = "Maksimum 10 label əlavə edilə bilər")
    private List<String> labels;

    // RELATION IDS
    @NotBlank(message = "Göndərən boş ola bilməz")
    private Long senderId;

    private Long companyId;

    private Long campaignId;

    @NotEmpty(message = "Ən azı bir alıcı olmalıdır")
    @Size(max = 50, message = "Maksimum 50 alıcı ola bilər")
    private Set<Long> recipientIds; // Person id-ləri
}
