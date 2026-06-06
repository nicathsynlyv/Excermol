package com.example.Excermol.repository;

import com.example.Excermol.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    // Ownera gore butun workspaceler
    List<Workspace> findAllByOwnerId(Long ownerId);

    // ada gore axtaris
    List<Workspace> findAllByNameContainingIgnoreCase(String name);

    // ownera gore workspace tapmaq
    List<Workspace> findByOwnerIdAndId(Long ownerId, Long workspaceId);

}
