package com.Application.EvaluationTV.model;

public class Enum{

    public enum StatutSession {
        PLANIFIEE,
        EN_COURS,
        TERMINEE,
        ANNULEE
    }

    public enum TypeQuestion {
        QCM_UNIQUE,
        QCM_MULTIPLE,
        VRAI_FAUX,
        TEXTE_COURT,
        TEXTE_LONG,
        NUMERIQUE
    }

    public enum StatutReponse {
        EN_ATTENTE,
        CORRIGEE,
        VALIDEE
    }

}
