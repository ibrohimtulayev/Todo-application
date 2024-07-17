package com.pdp.taskmanagerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String name;
        private LocalDate deadLine;
        private LocalDate finishDate;

        @ManyToOne(fetch = FetchType.LAZY)
        private Column column;

        @ManyToMany
        private List<User> users;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Comment> comments;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Attachment> attachments;

        private Boolean isCompletedOnTime = checkIfCompletedOnTime();

        public Boolean checkIfCompletedOnTime() {
                if (finishDate != null && finishDate.isBefore(deadLine)) {
                        return true;
                }
                return false;
        }

        public Boolean isOverDue() {
                return deadLine.isBefore(LocalDate.now()) && (column == null || !column.getName().equals("Completed"));
        }

        public void setColumn(Column column) {
                this.column = column;
                if (column != null && column.getName().equals("Completed")) {
                        this.finishDate = LocalDate.now();
                } else {
                        this.finishDate = null; // or use another default value if needed
                }
        }
}
