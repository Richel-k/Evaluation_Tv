package com.Application.EvaluationTV.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "examinateurs")
public class Examinateur extends User {

    // private String specialite;

    // private String grade; // Ex: Professeur, Docteur, etc.

    @OneToMany(mappedBy = "examinateur", cascade = CascadeType.ALL)
    private List<Session> sessionsCreees = new ArrayList<>();
}