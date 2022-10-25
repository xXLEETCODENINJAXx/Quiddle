package com.quiddle.quiddleApplication.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users_classes", uniqueConstraints = {@UniqueConstraint(columnNames = {"students_id", "classes_id"})})
@IdClass(UserClassId.class)
public class UserClass {

    @Id
    @Column(name = "students_id")
    private Long studentId;

    @Id
    @Column(name = "classes_id")
    private Long classId;
}
