package com.example.Excermol.repository;



import com.example.Excermol.entity.PersonActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PersonActivityRepository extends JpaRepository<PersonActivity, Long> {
    // Şəxsə aid bütün aktivlikləri gətir
    List<PersonActivity> findByPersonId(Long personId);

    // Şəxsə aid aktivlikləri tarixa görə sıralı gətir — UI-da ən yeni əvvəl
    List<PersonActivity> findByPersonIdOrderByPerformedAtDesc(Long personId);
}