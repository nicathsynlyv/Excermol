//package com.example.Excermol.Service;
//
//import com.example.Excermol.entity.FormRouting;
//import com.example.Excermol.repository.FormRoutingRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class FormRoutingService {
//    private final FormRoutingRepository repository;
//
//    public FormRoutingService(FormRoutingRepository repository) {
//        this.repository = repository;
//    }
//
//    public FormRouting create(FormRouting routing) {
//        return repository.save(routing);
//    }
//
//    public List<FormRouting> getByForm(Long formId) {
//        return repository.findByFormId(formId);
//    }
//}
