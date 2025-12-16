const evaluationConfig = {
    sessionId: /*[[${sessionId}]]*/ document.getElementById("CodeSessionCache").textContent,
    totalQuestions: /*[[${totalQuestions}]]*/ 20,
    timePerQuestion: 20, // secondes
    allowBacktracking: false,
    shuffleAnswers: /*[[${shuffleAnswers}]]*/ false,
    showTimer: true
};
//console.log(document.getElementById("CodeSessionCache").textContent)

class EvaluationSystem {
    constructor(config) {
        this.config = config;
        this.currentQuestionIndex = 0;
        this.questions = [];
        this.answers = new Map();
        this.startTime = null;
        this.questionTimer = null;
        this.globalTimer = null;
        this.isEvaluationActive = false;
        this.totalTime = 0;
        this.flaggedQuestions = new Set();

        // État du timer
        this.questionTimeLeft = config.timePerQuestion;
        this.circleProgress = null;

        this.init();
    }

    async init() {
        await this.loadQuestions();
        this.setupEventListeners();
        this.startEvaluation();
        this.updateProgressBar();
        this.generateQuestionDots();
    }

    async loadQuestions() {
        try {
            const response = await fetch(`/api/sessions/${this.config.sessionId}/questions`);
            if (!response.ok) throw new Error('Erreur lors du chargement des questions');

            this.questions = await response.json();
            console.log(this.questions)
            this.displayCurrentQuestion();
        } catch (error) {
            console.error('Erreur:', error);
            this.showError('Impossible de charger les questions. Veuillez rafraîchir la page.');
        }
    }

    displayCurrentQuestion() {
        if (this.currentQuestionIndex >= this.questions.length) {
            console.log(this.questions.length)
            this.endEvaluation();
            return;
        }

        const question = this.questions[this.currentQuestionIndex];

        // Mettre à jour l'affichage
        document.getElementById('displayQuestionNumber').textContent = this.currentQuestionIndex + 1;
        document.getElementById('questionStatement').innerHTML = `<p>${question.enonce}</p>`;
        document.getElementById('questionPoints').textContent = `${question.points} point${question.points > 1 ? 's' : ''}`;
        //document.getElementById('questionDifficulty').textContent = this.getDifficultyLabel(question.difficulte);

        // Mettre à jour l'indicateur de type
        //this.updateQuestionTypeIndicator(question.typeQuestion);

        // Afficher les réponses
        this.displayAnswers(question);

        // Mettre à jour les timers
        this.resetQuestionTimer();
        this.startQuestionTimer();

        // Mettre à jour la navigation
        this.updateQuestionDots();
        this.updateNextButton();

        // Désélectionner toutes les réponses
        document.querySelectorAll('.answer-option').forEach(option => {
            option.classList.remove('selected');
        });

        // Vérifier si une réponse a déjà été donnée
        if (this.answers.has(this.currentQuestionIndex)) {
            const selectedAnswer = this.answers.get(this.currentQuestionIndex);
            const answerOptions = document.querySelectorAll('.answer-option');
            if (answerOptions[selectedAnswer]) {
                answerOptions[selectedAnswer].classList.add('selected');
                document.getElementById('nextBtn').disabled = false;
            }
        }
    }

    displayAnswers(question) {
        const container = document.getElementById('answersContainer');
        container.innerHTML = '';

        let answers = question.reponses || [];

        // Mélanger les réponses si configuré
        if (this.config.shuffleAnswers) {
            answers = this.shuffleArray([...answers]);
        }

        const letters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'];

        answers.forEach((answer, index) => {
            const answerElement = document.createElement('div');
            answerElement.className = 'answer-option';
            answerElement.dataset.index = index;
            answerElement.setAttribute("identifier", answer.id)
            answerElement.setAttribute("correct", answer.correcte)

            answerElement.innerHTML = `
                <div class="answer-letter">${letters[index]}</div>
                <div class="answer-text">${answer.texte}</div>
            `;

            answerElement.addEventListener('click', () => this.selectAnswer(index));
            container.appendChild(answerElement);
        });
    }

    selectAnswer(answerIndex) {
        // Désélectionner toutes les réponses
        document.querySelectorAll('.answer-option').forEach(option => {
            option.classList.remove('selected');
        });

        // Sélectionner la réponse cliquée
        const selectedOption = document.querySelectorAll('.answer-option')[answerIndex];
        selectedOption.classList.add('selected');

        let identifier = selectedOption.getAttribute("identifier")
        let correct = selectedOption.getAttribute("correct")


        // Enregistrer la réponse
        this.answers.set(this.currentQuestionIndex,{
            answerIndex : answerIndex, 
            identifier : identifier, 
            correct : correct 
        } );

        // Activer le bouton suivant
        document.getElementById('nextBtn').disabled = false;
        
        // Sauvegarder automatiquement
        this.saveAnswer();

        // Afficher la confirmation 
        this.showAnswerConfirmation();
    }

    showAnswerConfirmation() {
        const confirmation = document.getElementById('answerConfirmation');
        confirmation.style.display = 'flex';

        let countdown = this.questionTimeLeft;
        const countdownElement = document.getElementById('nextQuestionCountdown');
        countdownElement.textContent = countdown;

        const countdownInterval = setInterval(() => {
            countdown--;
            countdownElement.textContent = countdown;

            if (countdown <= 0) {
                clearInterval(countdownInterval);
                this.nextQuestion();
            }
        }, 2000);
    }

    nextQuestion() {
        // Arrêter le timer de la question actuelle
        this.stopQuestionTimer();

        // Cacher la confirmation
        document.getElementById('answerConfirmation').style.display = 'none';

        // Passer à la question suivante
        this.currentQuestionIndex++;

        if (this.currentQuestionIndex < this.questions.length) {
            this.displayCurrentQuestion();
            this.updateProgressBar();

            // Jouer le son de transition
            this.playSound('nextQuestionSound');
        } else {
            this.endEvaluation();
        }
    }

    startEvaluation() {
        this.startTime = new Date();
        this.isEvaluationActive = true;

        // Démarrer le timer global
        this.startGlobalTimer();

        // Démarrer le timer de la première question
        this.startQuestionTimer();

        // Jouer le son de début
        this.playSound('tickSound');
    }

    startGlobalTimer() {
        this.globalTimer = setInterval(() => {
            this.totalTime++;
            this.updateGlobalTimer();
        }, 1000);
    }

    updateGlobalTimer() {
        const minutes = Math.floor(this.totalTime / 60);
        const seconds = this.totalTime % 60;
        const timerText = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(1, '0')}`;
        document.getElementById('globalTimer').textContent = timerText;
    }

    startQuestionTimer() {
        const part = this.questions[this.currentQuestionIndex].time.split(":")
        let time = parseInt(part[0]) * 60 + parseInt(part[1])
        this.questionTimeLeft = time * 2;// this.config.timePerQuestion;
        this.updateQuestionTimerDisplay();

        // Configurer le timer circulaire
        this.setupCircularTimer();

        // Démarrer le compte à rebours
        this.questionTimer = setInterval(() => {
            this.questionTimeLeft--;
            this.updateQuestionTimerDisplay();
            this.updateCircularTimer();

            if (this.questionTimeLeft <= 1) {
                this.handleTimeUp();
            } else if (this.questionTimeLeft <= 5) {
                this.showTimeWarning();
            }
        }, 1000);
    }

    setupCircularTimer() {
        const circle = document.getElementById('timerCircle');
        const radius = circle.r.baseVal.value;
        const circumference = radius * 2 * Math.PI;

        circle.style.strokeDasharray = `${circumference} ${circumference}`;
        circle.style.strokeDashoffset = circumference;

        this.circleProgress = {
            element: circle,
            circumference: circumference,
            timePerQuestion: this.config.timePerQuestion
        };

        this.updateCircularTimer();
    }

    updateCircularTimer() {
        if (!this.circleProgress) return;

        const offset = this.circleProgress.circumference * (1 - this.questionTimeLeft / this.config.timePerQuestion);
        this.circleProgress.element.style.strokeDashoffset = this.circleProgress.circumference - offset;

        // Changer la couleur en fonction du temps restant
        if (this.questionTimeLeft <= 5) {
            this.circleProgress.element.style.stroke = '#ef4444';
        } else if (this.questionTimeLeft <= 10) {
            this.circleProgress.element.style.stroke = '#f59e0b';
        }
    }

    updateQuestionTimerDisplay() {
        const timerElement = document.getElementById('questionTimer');
        const circularText = document.getElementById('circularTimerText');

        timerElement.textContent = `${this.questionTimeLeft}s`;
        circularText.textContent = `${this.questionTimeLeft}s`;

        // Ajouter des classes d'avertissement
        if (this.questionTimeLeft <= 5) {
            timerElement.classList.add('timer-danger');
            circularText.classList.add('timer-danger');
        } else if (this.questionTimeLeft <= 10) {
            timerElement.classList.add('timer-warning');
            circularText.classList.add('timer-warning');
        } else {
            timerElement.classList.remove('timer-warning', 'timer-danger');
            circularText.classList.remove('timer-warning', 'timer-danger');
        }
    }

    showTimeWarning() {
        // Clignoter le timer
        const timerElement = document.getElementById('questionTimer');
        timerElement.style.animation = 'pulse 0.5s infinite';

        // Jouer un son d'avertissement
        if (this.questionTimeLeft === 5) {
            this.playSound('timesUpSound');
        }
    }

    handleTimeUp() {
        this.stopQuestionTimer();

        // Jouer le son de fin de temps
        this.playSound('timesUpSound');

        // Si aucune réponse n'a été sélectionnée, marquer comme non répondu
        if (!this.answers.has(this.currentQuestionIndex)) {
            this.answers.set(this.currentQuestionIndex, null);
        }

        // Passer automatiquement à la question suivante après un délai
        setTimeout(() => {
            this.nextQuestion();
        }, 500);
    }

    stopQuestionTimer() {
        if (this.questionTimer) {
            clearInterval(this.questionTimer);
            this.questionTimer = null;
        }
    }

    resetQuestionTimer() {
        this.stopQuestionTimer();
        this.questionTimeLeft = this.config.timePerQuestion;
        this.updateQuestionTimerDisplay();
    }

    updateProgressBar() {
        const progress = ((this.currentQuestionIndex + 1) / this.questions.length) * 100;
        document.getElementById('progressFill').style.width = `${progress}%`;
        document.getElementById('currentQuestion').textContent = this.currentQuestionIndex + 1;
    }

    generateQuestionDots() {
        const container = document.getElementById('questionDots');
        container.innerHTML = '';

        for (let i = 0; i < this.questions.length; i++) {
            const dot = document.createElement('div');
            dot.className = 'question-dot';
            dot.textContent = i + 1;
            dot.dataset.index = i;

            // Ajouter des classes selon l'état
            if (i === this.currentQuestionIndex) {
                dot.classList.add('current');
            } else if (this.answers.has(i)) {
                dot.classList.add('answered');
            } else {
                dot.classList.add('unanswered');
            }

            if (this.flaggedQuestions.has(i)) {
                dot.classList.add('flagged');
            }

            container.appendChild(dot);
        }
    }

    updateQuestionDots() {
        const dots = document.querySelectorAll('.question-dot');
        dots.forEach((dot, index) => {
            dot.classList.remove('current', 'answered', 'unanswered');

            if (index === this.currentQuestionIndex) {
                dot.classList.add('current');
            } else if (this.answers.has(index)) {
                dot.classList.add('answered');
            } else {
                dot.classList.add('unanswered');
            }

            if (this.flaggedQuestions.has(index)) {
                dot.classList.add('flagged');
            } else {
                dot.classList.remove('flagged');
            }
        });
    }

    updateNextButton() {
        const nextBtn = document.getElementById('nextBtn');
        nextBtn.disabled = !this.answers.has(this.currentQuestionIndex);
    }

    
    async saveAnswer() {
        const answerData = {
            sessionId: this.config.sessionId,
            questionIndex: this.currentQuestionIndex,
            answerIndex: this.answers.get(this.currentQuestionIndex),
            timestamp: new Date().toISOString()
        };

    //     try {
    //         await fetch('/api/sessions/evaluation/save-answer', {
    //             method: 'POST',
    //             headers: {
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify(answerData)
    //         });
    //     } catch (error) {
    //         console.error('Erreur lors de la sauvegarde:', error);
    //     }
    }

    async endEvaluation() {
        this.isEvaluationActive = false;
        this.stopQuestionTimer();

        if(this.globalTimer) {
            clearInterval(this.globalTimer);
        }

        // Soumettre toutes les réponses
        await this.submitEvaluation();

        // Afficher le modal de fin
        this.showCompletionModal();
    }

    async submitEvaluation() {
        const evaluationData = {
            sessionId: this.config.sessionId,
            answers: Array.from(this.answers.entries()).map(([questionIndex, answerData]) => ({
                questionIndex,
                answerIndex : answerData.answerIndex,
                answerId : answerData.identifier,
                correct: Boolean(answerData.correct),
                questionId: this.questions[answerData.answerIndex]?.numero,
                points: this.questions[answerData.answerIndex]?.points

            })),
            duration: this.totalTime,
            completionTime: new Date().toISOString()
        };

        console.log(evaluationData)

        try {
            const response = await fetch('/api/sessions/evaluation/submitEvaluation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(evaluationData)
            });

            if (!response.ok) throw new Error('Erreur lors de la soumission');

            const result = await response.json();
            console.log("Le résult est ", result);

            alert("Merci pour votre participation !! .")

            window.location.href = "/candidat/dashboard"
            //return result;
        } catch (error) {
            console.error('Erreur:', error);
            this.showError('Erreur lors de la soumission. Veuillez réessayer.');
        }
    }

    showConfirmationModal() {
        // Mettre à jour les statistiques
        const answeredCount = this.answers.size;
        const remainingCount = this.questions.length - answeredCount;

        document.getElementById('answeredCount').textContent = answeredCount;
        document.getElementById('remainingCount').textContent = remainingCount;
        document.getElementById('elapsedTime').textContent = document.getElementById('globalTimer').textContent;

        // Afficher le modal
        document.getElementById('confirmationModal').style.display = 'flex';
    }

    showCompletionModal() {
        const score = this.calculateScore();

        document.getElementById('finalScore').textContent = `${score.points}/${score.totalPoints}`;
        document.getElementById('finalPercentage').textContent = `${score.percentage}%`;
        document.getElementById('finalTime').textContent = document.getElementById('globalTimer').textContent;

        document.getElementById('completionModal').style.display = 'flex';
    }

    calculateScore() {
        let totalPoints = 0;
        let earnedPoints = 0;

        this.questions.forEach((question, index) => {
            totalPoints += question.points;

            const answerIndex = this.answers.get(index);
            if (answerIndex !== undefined && answerIndex !== null) {
                const selectedAnswer = question.reponses[answerIndex];
                if (selectedAnswer && selectedAnswer.correcte) {
                    earnedPoints += question.points;
                }
            }
        });

        const percentage = totalPoints > 0 ? Math.round((earnedPoints / totalPoints) * 100) : 0;

        return {
            points: earnedPoints,
            totalPoints: totalPoints,
            percentage: percentage
        };
    }

    toggleFlag() {
        if (this.flaggedQuestions.has(this.currentQuestionIndex)) {
            this.flaggedQuestions.delete(this.currentQuestionIndex);
            document.getElementById('flagBtn').innerHTML = '<i class="far fa-flag"></i> Marquer la question';
        } else {
            this.flaggedQuestions.add(this.currentQuestionIndex);
            document.getElementById('flagBtn').innerHTML = '<i class="fas fa-flag"></i> Question marquée';
        }

        this.updateQuestionDots();
    }

    // updateQuestionTypeIndicator(type) {
    //     const indicator = document.getElementById('questionTypeIndicator');
    //     let icon, text;

    //     switch (type) {
    //         case 'SINGLE':
    //             icon = 'fas fa-check-circle';
    //             text = 'Choix unique (une seule réponse correcte)';
    //             break;
    //         case 'MULTIPLE':
    //             icon = 'fas fa-tasks';
    //             text = 'Choix multiple (plusieurs réponses correctes)';
    //             break;
    //         case 'TRUE_FALSE':
    //             icon = 'fas fa-balance-scale';
    //             text = 'Vrai/Faux';
    //             break;
    //         case 'TEXT':
    //             icon = 'fas fa-keyboard';
    //             text = 'Réponse texte';
    //             break;
    //         default:
    //             icon = 'fas fa-question-circle';
    //             text = 'Type de question';
    //     }

    //     indicator.innerHTML = `<i class="${icon}"></i> <span>${text}</span>`;
    // }

    getDifficultyLabel(difficulty) {
        switch (difficulty) {
            case 'EASY': return 'Facile';
            case 'MEDIUM': return 'Moyenne';
            case 'HARD': return 'Difficile';
            default: return 'Non définie';
        }
    }

    playSound(soundId) {
        const sound = document.getElementById(soundId);
        if (sound) {
            sound.currentTime = 0;
            sound.play().catch(e => console.log('Son non joué:', e));
        }
    }

    showError(message) {
        alert(`Erreur: ${message}`);
    }

    shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }

    setupEventListeners() {
        // Navigation clavier
        document.addEventListener('keydown', (e) => {
            if (!this.isEvaluationActive) return;

            switch (e.key) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                    const index = parseInt(e.key) - 1;
                    const answerOptions = document.querySelectorAll('.answer-option');
                    if (index < answerOptions.length) {
                        this.selectAnswer(index);
                    }
                    break;
                case ' ':
                case 'Enter':
                    if (!document.getElementById('nextBtn').disabled) {
                        this.nextQuestion();
                    }
                    break;
                case 'f':
                case 'F':
                    this.toggleFlag();
                    break;
            }
        });
    }
}

// Fonctions globales
let evaluation;

function closeConfirmationModal() {
    document.getElementById('confirmationModal').style.display = 'none';
}

function confirmDelete() {
    // Implémentation de la suppression
}

function redirectToDashboard() {
    window.location.href = '/candidat/dashboard';
}

// Initialisation quand la page est chargée
document.addEventListener('DOMContentLoaded', () => {
    evaluation = new EvaluationSystem(evaluationConfig);
});