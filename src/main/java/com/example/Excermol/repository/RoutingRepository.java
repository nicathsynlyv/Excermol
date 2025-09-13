package com.example.Excermol.repository;


import com.example.Excermol.entity.Routing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutingRepository extends JpaRepository<Routing, Long> {


    // Routing title-a görə tap
    Optional<Routing> findByRoutingTitle(String routingTitle);

    // Müəyyən email şərtini özündə saxlayan routing-ləri tap
    List<Routing> findByContainsContainingIgnoreCase(String contains);
}