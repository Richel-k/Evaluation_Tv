
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
        alert(`Connexion à la session: ${code}`);
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
