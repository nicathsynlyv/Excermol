package com.example.Excermol.repository;

import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findByFolder(EmailFolder folder);

    List<Email> findByLabelsContaining(String label);

    List<Email> findByRead(boolean read);

    List<Email> findByFromAddress(String fromAddress);

}
