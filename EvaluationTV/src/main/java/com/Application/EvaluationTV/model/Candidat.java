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
@Table(name = "candidats")
public class Candidat extends User {

    // @Column(unique = true)
    // private String matricule;

    // private String niveau; // Ex: Licence, Master, etc. 

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReponseCandidat> reponses = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "candidat_session", joinColumns = @JoinColumn(name = "candidat_id"), inverseJoinColumns = @JoinColumn(name = "session_id"))
    private List<Session> sessions = new ArrayList<>();
}
