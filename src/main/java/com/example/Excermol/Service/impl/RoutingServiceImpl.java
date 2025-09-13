package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.RoutingService;
import com.example.Excermol.entity.Routing;
import com.example.Excermol.repository.RoutingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoutingServiceImpl implements RoutingService {
    private final RoutingRepository routingRepository;

    public RoutingServiceImpl(RoutingRepository routingRepository) {
        this.routingRepository = routingRepository;
    }

    @Override
    public List<Routing> getAll() {
        return routingRepository.findAll();
    }

    @Override
    public Optional<Routing> getById(Long id) {
        return routingRepository.findById(id);
    }

    @Override
    public Routing save(Routing entity) {
        return routingRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        routingRepository.deleteById(id);
    }

    public Optional<Routing> findByRoutingTitle(String title){
        return routingRepository.findByRoutingTitle(title);
    }

    public List<Routing> findByContains(String contains){
        return routingRepository.findByContainsContainingIgnoreCase(contains);
    }
}
