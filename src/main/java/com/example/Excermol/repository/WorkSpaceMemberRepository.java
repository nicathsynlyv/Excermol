package com.example.Excermol.repository;

import com.example.Excermol.entity.WorkspaceMember;
import com.example.Excermol.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkSpaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {

    //workspace e gore butun member ler
    List<WorkspaceMember> findAllByWorkspaceId(Long workspaceId);

    // usere gore butun workspace uzvleri
    List<WorkspaceMember> findAllByUserId(Long userId);

    // workspace ve usera gore uzvleri tap
    Optional<WorkspaceMember> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    // workspace rola gore uzvler
    List<WorkspaceMember> findAllByWorkspaceIdAndRole(Long workspaceId, MemberRole role);

    // user artiq bu spacework de dir?
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);

}
