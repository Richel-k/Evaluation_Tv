package com.Application.EvaluationTV.security;

import com.Application.EvaluationTV.model.Candidat;
import com.Application.EvaluationTV.model.Examinateur;


import com.Application.EvaluationTV.repository.CandidatRepository;
import com.Application.EvaluationTV.repository.ExaminateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CandidatRepository candidatRepository;
    private final ExaminateurRepository examinateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Chercher d'abord dans les candidats
        Candidat candidat = candidatRepository.findByEmail(email).orElse(null);
        if (candidat != null) {
            return new CustomUserDetails(candidat, "CANDIDAT");
        }

        // 2. Ensuite dans les examinateurs
        Examinateur examinateur = examinateurRepository.findByEmail(email).orElse(null);
        if (examinateur != null) {
            return new CustomUserDetails(examinateur, "EXAMINATEUR");
        }

        // 3. Utilisateur non trouvé
        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
    }
}