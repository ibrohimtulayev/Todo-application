package com.pdp.taskmanagerapp.repo;


import com.pdp.taskmanagerapp.entity.Attachment;
import com.pdp.taskmanagerapp.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AttachmentContentRepo extends JpaRepository<AttachmentContent, UUID> {
    @Transactional
    void deleteByAttachment(Attachment attachment);

    AttachmentContent findById(Integer id);

}
