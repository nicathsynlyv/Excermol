package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.ActivityService;
import com.example.Excermol.entity.Activity;
import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.ActivityAction;
import com.example.Excermol.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;


    // ---- BaseService metodları ----

    @Override
    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    @Override
    public Optional<Activity> getById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    public Activity save(Activity entity) {
        return activityRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        activityRepository.deleteById(id);
    }

    // ---- Əlavə metodlar ----

    // Şəxsə görə bütün activity-lər (ən yenisi birinci)
    public List<Activity> getByPerson(Person person) {
        return activityRepository.findByPersonOrderByPerformedAtDesc(person);
    }

    // Action növünə görə activity-lər
    public List<Activity> getByAction(ActivityAction action) {
        return activityRepository.findByAction(action);
    }

    // Şəxs + Action filtr
    public List<Activity> getByPersonAndAction(Person person, ActivityAction action) {
        return activityRepository.findByPersonAndAction(person, action);
    }
}
