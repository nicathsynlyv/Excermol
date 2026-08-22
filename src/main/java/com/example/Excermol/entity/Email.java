package com.example.Excermol.entity;

import com.example.Excermol.enums.EmailStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity  // jpa annotation yəni, bu class database entity-sidir.
@Table(name = "emails") // jpa annotation yəni, database-də cədvəlin adını müəyyən edir.
@Getter // lombok annonation yəni, bütün field-lər üçün getter yaradır.
@Setter // lombok annonation yəni, bütün field-lər üçün setter yaradır.
@AllArgsConstructor // lombok annonation yəni, bütün field-ləri qəbul edən constructor yaradır.
@NoArgsConstructor // lombok annonation yəni, boş constructor yaradır.
public class Email {

    // primary key
    @Id // id bu entity-nin primary key-idir,Unikaldır.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID-ni bizim verməyəcəyimizi deyir, id artmaası identity tipinde olacaq.
    private Long id;

    @NotBlank(message = "Mövzu boş ola bilməz") // Validation üçündür,NotBlank həm null, həm boş string, həm də yalnız whitespace vəziyyətlərini yoxlayır.
    @Size(max=250) // subject maksimum 250 character ola bilər.
    @Column(nullable = false, length = 250) // database səviyyəsində constraint/configuration-dır.
    private String subject;


    @NotBlank(message = "Email məzmunu boş ola bilməz") //  Validation üçündür.
    @Column(columnDefinition = "TEXT",nullable = false) // niye TEXT? çünki,TEXT databasede böyük mətn saxlamağa kömək edir.
    private String body;


    // String Set əvəzinə:
    @ManyToMany // Bir email-in bir neçə recipient-i ola bilər.
    @JoinTable(name = "email_recipients",
            joinColumns = @JoinColumn(name = "email_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))  // Many-to-many əlaqəsində arada ayrıca table yaranır,emails ve persons arasında olur email_id ile person_id elaqelendirir  (joinColumns-Bu table-də bizim entity-nin foreign key column-udur emails.id-ə bağlanır,inverseJoinColumns-Bu isə qarşı entity-nin ID-sidir,person id bağlanır )
    private Set<Person> recipients;  //Set duplicate elementlərin qarşısını almaq üçün istifadə olunur.

    @NotNull  // status mütləq olmalıdır.
    @Enumerated(EnumType.STRING) // bu databasede enum-ın String type inda saxlanacağını göstərirç ordinal olsa idi reqem seklinde saxlanardi sıra dəyişsə ordinal problem yarada bilər
    @Column(nullable = false)
    private EmailStatus status; // INBOX, IMPORTANT, SENT, DRAFT, SPAM, TRASH

    // Email-in oxunub-oxunmadığını göstərir,read = false → oxunmayıb. read = true → oxunub.
    private boolean read;


    private LocalDateTime createdAt; // Email nə vaxt yaradılıb?
    private LocalDateTime sentAt; // Email nə vaxt yaradılıb?
    private LocalDateTime updatedAt; // Email ən son nə vaxt dəyişdirilib?


    //attachments ile elaqe,Bir email-in çoxlu attachment-i ola bilər
    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true) // mappedBy = "email" - Əlaqənin sahibi Email deyil, Attachment entity-sindəki email field-idir,2-cascade = CascadeType.ALL -Bu o deməkdir ki, Email üzərində edilən bəzi persistence əməliyyatları attachment-lərə də ötürülür Məsələn email save edəndə onun attachment-ləri də save edilə bilər, orphanRemoval = true,Əgər attachment artıq email-in collection-ından çıxarılıbsa, orphan hesab edilir və database-dən silinə bilər,orphanRemoval=true olduğuna görə həmin attachment database-dən də silinə bilər. Bu xüsusilə parent-child əlaqələrində istifadə olunur.
    private List<Attachment> attachments = new ArrayList<>();

    //email ve compaign ile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;


    @ElementCollection
    @CollectionTable(name = "email_labels", joinColumns = @JoinColumn(name = "email_id"))
    @Column(name = "label")
    private List<String> labels; // Client, Work, Contest, Social media


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // String əvəzinə:
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    //company ile
    // Düzgünü:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;


    //gelecekede form routinglere baxmaq ucun istifade oluna biler
    @OneToMany(mappedBy = "email")
    private List<FormRouting> routings = new ArrayList<>();

}
