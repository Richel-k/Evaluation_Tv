package com.Application.EvaluationTV.service;

import com.Application.EvaluationTV.dto.CandidatRegistrationDto;
import com.Application.EvaluationTV.dto.ExaminateurRegistrationDto;
import com.Application.EvaluationTV.model.Candidat;
import com.Application.EvaluationTV.model.Examinateur;
import com.Application.EvaluationTV.repository.CandidatRepository;
import com.Application.EvaluationTV.repository.ExaminateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CandidatRepository candidatRepository;
    private final ExaminateurRepository examinateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Candidat registerCandidat(CandidatRegistrationDto dto) {
        // Vérifier si l'email existe déjà
        if (candidatRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        if (examinateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        // // Vérifier si le matricule existe déjà
        // if (candidatRepository.existsByEmail(dto.getEmail())) {
        //     throw new RuntimeException("Ce matricule est déjà utilisé");
        // }

        // Créer le candidat
        Candidat candidat = new Candidat();
        candidat.setUsername(dto.getUsername());
        candidat.setPrenom(dto.getPrenom());
        candidat.setEmail(dto.getEmail());
        candidat.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        //candidat.setMatricule(dto.getMatricule());
        //candidat.setNiveau(dto.getNiveau());

        return candidatRepository.save(candidat);
    }

    @Transactional
    public Examinateur registerExaminateur(ExaminateurRegistrationDto dto) {
        // Vérifier si l'email existe déjà
        if (candidatRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        if (examinateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        // Créer l'examinateur
        Examinateur examinateur = new Examinateur();
        examinateur.setUsername(dto.getUsername());
        examinateur.setPrenom(dto.getPrenom());
        examinateur.setEmail(dto.getEmail());
        examinateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        //examinateur.setSpecialite(dto.getSpecialite());
        //examinateur.setGrade(dto.getGrade());
        
        return examinateurRepository.save(examinateur);
    }
}