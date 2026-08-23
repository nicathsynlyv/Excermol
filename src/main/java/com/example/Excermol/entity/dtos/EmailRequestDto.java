package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.EmailStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
//Bu class client-dən backend-ə gələn request məlumatlarını qəbul etmək üçündür.
public class EmailRequestDto {


    @NotBlank(message = "Mövzu boş ola bilməz") //null,"","   " ola bilməz.
    @Size(max = 250,message = "Mövzu 250 simvoldan çox ola bilməz")
    private String subject;

    @NotBlank(message = "Email məzmunu boş ola bilməz")
    private String body;

    @NotNull(message = "Status boş ola bilməz")
    private EmailStatus status;

    private LocalDateTime sentAt;

    @Size(max = 10,message = "Maksimum 10 label əlavə edilə bilər")
    private List<String> labels;

    // RELATION IDS
    // "Client-dən tam User entity qəbul etmək əvəzinə yalnız onun ID-sini qəbul edirəm.
    // Service layer həmin ID vasitəsilə User-i repository-dən tapıb Email entity-yə relationship kimi əlavə edir.
    // Beləliklə API contract entity-dən ayrılır, lazımsız məlumatların client-dən göndərilməsinin qarşısı alınır və entity relationship-ləri service layer-də idarə olunur."
    @NotNull(message = "Göndərən ID-si boş ola bilməz")
    private Long senderId;

    private Long companyId;

    private Long campaignId;

    // Çünki Email və Person arasında Many-to-Many əlaqə var.
    // Bir email bir neçə recipient-ə göndərilə bilər, bir Person isə bir çox email-in recipient-i ola bilər.
    // DTO-da yalnız Person ID-lərini qəbul edirəm və Set istifadə etməklə duplicate recipient-lərin qarşısını alıram.
    @NotEmpty(message = "Ən azı bir alıcı olmalıdır")
    @Size(max = 50, message = "Maksimum 50 alıcı ola bilər")
    private Set<Long> recipientIds; // Person id-ləri
}


//String       → @NotBlank
//Enum         → @NotNull
//Set/List     → @NotEmpty