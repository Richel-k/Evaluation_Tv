
// Toggle Sidebar
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.querySelector('.sidebar-overlay');
    sidebar.classList.toggle('active');
    overlay.classList.toggle('active');
}

// Examinateur Functions
function createSession() {
    alert('Redirection vers la page de création de session...');
    window.location.href = "/examinateur/create-session"
    // window.location.href = '/examinateur/creer-session';
}

function modifySession() {
    const sessionId = document.getElementById('modifySessionId').value;
    if (sessionId) {
        alert(`Modification de la session ID: ${sessionId}`);
        // window.location.href = `/examinateur/modifier-session/${sessionId}`;
    } else {
        alert('Veuillez entrer un ID de session');
    }
}

function viewStats() {
    alert('Affichage des statistiques...');
    // window.location.href = '/examinateur/statistiques';
}

function viewSession(id) {
    alert(`Consultation de la session ${id}`);
    // window.location.href = `/examinateur/session/${id}`;
}

function editSession(id) {
    alert(`Modification de la session ${id}`);
    // window.location.href = `/examinateur/modifier-session/${id}`;
}

function deleteSession(id) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la session ${id} ?`)) {
        alert(`Session ${id} supprimée`);
        // Appel API pour supprimer
    }
}

function viewResults(id) {
    alert(`Affichage des résultats de la session ${id}`);
    // window.location.href = `/examinateur/resultats/${id}`;
}

function exportResults(id) {
    alert(`Export des résultats de la session ${id}`);
    // Appel API pour exporter
}

function refreshSessions() {
    alert('Actualisation des sessions...');
    location.reload();
}

// Candidat Functions
function joinSession() {
    const code = document.getElementById('sessionCode').value;
    if (code) {
        //alert(`Connexion à la session: ${code}`);
        window.location.href = `/candidat/session/evaluation/${code}`;
    } else {
        alert('Veuillez entrer un code de session');
    }
}

function viewMyResults() {
    alert('Affichage de vos résultats...');
    // window.location.href = '/candidat/resultats';
}

function viewHistory() {
    alert('Affichage de l\'historique...');
    // window.location.href = '/candidat/historique';
}

function viewDetailedResults(id) {
    alert(`Détails des résultats de la session ${id}`);
    // window.location.href = `/candidat/resultat/${id}`;
}


function getMyResult(){
    if( window.location.href.includes("candidat")){
        try {

            fetch(`/candidat/get-My-result`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => response.json()
                    // results.forEach(element => {

                    // });

                )
                .then(results => {
                    console.log(results)

                    let ResultBloc = document.querySelector(".sessions-section")
                    // let Result = results[0]
                    // console.log(Result)
                    // console.log(ResultBloc)

                    results.forEach((result, index) => {
                        console.log(`Résultat ${index + 1}:`);
                        console.log(`  Titre: ${result.title}`);
                        console.log(`  Points: ${result.points}`);
                        console.log(`  Session ID: ${result.session_id}`);
                        console.log(`  Date: ${new Date(result.date_debut).toLocaleDateString()}`);

                        let plainText = `
                 <div class="session-item">
                    <div class="session-header">
                        <span class="session-code">${result.session_id}</span>
                        <span class="session-status status-ended">Terminée</span>
                    </div>
                    <div class="session-title">${result.title}</div>
                    <div class="session-details">
                        <span>📅 ${new Date(result.date_debut).toLocaleDateString('fr-FR')}</span>
                        <span>⏰ ${new Date(result.date_debut).getHours()}:${String(new Date(result.date_debut).getMinutes()).padStart(2, '0')}</span>
                        <span>✅ ${result.points} points</span>
                        <span>📊 ${result.points} / ${result.total}</span>
                    </div>
                    <div class="session-actions">
                        <button class="btn-small btn-primary" onclick="viewDetailedResults(1)">Détails</button>
                    </div>
                </div>
                `;

                        ResultBloc.insertAdjacentHTML('beforeend', plainText);
                    });
                })
        } catch (error) {
            console.error('Erreur:', error);
            this.showError('impossible de charger les résultats');
        }

    }


}
getMyResult()