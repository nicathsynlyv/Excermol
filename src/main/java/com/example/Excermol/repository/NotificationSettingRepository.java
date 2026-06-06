package com.example.Excermol.repository;

import com.example.Excermol.entity.NotificationSetting;
import com.example.Excermol.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    // workspace e gore butun notification settings getirmek
    List<NotificationSetting> findAllByWorkspaceId(Long WorkspaceId);

    //  user e gore butun notification settings getirmek
    List<NotificationSetting> findAllByUserId(Long userId);

    // workspace ve usera gore notification settings getimek
    List<NotificationSetting> findAllByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    // userin bu workspace de notification setting si varmi
    boolean existsByWorkspaceIdAndUserId(Long workspaceId, Long userId);


}
