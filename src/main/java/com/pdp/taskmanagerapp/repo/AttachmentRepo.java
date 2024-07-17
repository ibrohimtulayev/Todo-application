package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepo extends JpaRepository<Attachment, Integer> {

}