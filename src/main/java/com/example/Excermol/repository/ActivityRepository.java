package com.example.Excermol.repository;

import com.example.Excermol.entity.Activity;
import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.ActivityAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Bir şəxsin bütün activity-ləri (ən yenisi birinci gələcək)
    List<Activity> findByPersonOrderByPerformedAtDesc(Person person);

    // Action növünə görə filtr
    List<Activity> findByAction(ActivityAction action);

    // Bir şəxsin müəyyən action-larını tapmaq
    List<Activity> findByPersonAndAction(Person person, ActivityAction action);
}