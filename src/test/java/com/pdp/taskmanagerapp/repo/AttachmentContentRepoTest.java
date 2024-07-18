package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.Attachment;
import com.pdp.taskmanagerapp.entity.AttachmentContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class AttachmentContentRepoTest {

    @Autowired
    private AttachmentContentRepo attachmentContentRepo;

    @Autowired
    private AttachmentRepo attachmentRepo;

    @Test
    void testSaveAndFindById() {
        Attachment attachment = Attachment.builder()
                .name("Test Attachment")
                .contentType("image/png")
                .size(12345)
                .build();
        attachmentRepo.save(attachment);

        AttachmentContent attachmentContent = AttachmentContent.builder()
                .content(new byte[]{1, 2, 3})
                .attachment(attachment)
                .build();
        attachmentContentRepo.save(attachmentContent);

        AttachmentContent foundContent = attachmentContentRepo.findById(attachmentContent.getId());

        assertNotNull(foundContent);
        assertEquals(attachmentContent.getId(), foundContent.getId());
        assertEquals(attachment.getId(), foundContent.getAttachment().getId());
    }

    @Test
    void testDeleteByAttachment() {
        Attachment attachment = Attachment.builder()
                .name("Test Attachment")
                .contentType("image/png")
                .size(12345)
                .build();
        attachmentRepo.save(attachment);

        AttachmentContent attachmentContent = AttachmentContent.builder()
                .content(new byte[]{1, 2, 3})
                .attachment(attachment)
                .build();
        attachmentContentRepo.save(attachmentContent);

        attachmentContentRepo.deleteByAttachment(attachment);

        AttachmentContent foundContent = attachmentContentRepo.findById(attachmentContent.getId());

        assertEquals(null, foundContent);
    }
}
