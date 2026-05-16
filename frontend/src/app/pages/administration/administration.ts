import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

// ========== INTERFACES ==========

interface Tontine {
  id: number;
  nomTontine: string;
  montant: number;
  nombreMembres?: number;
  nombreMax?: number;
  admin?: {
    id: number;
    nom: string;
    prenom: string;
  };
}

interface DemandeAdhesion {
  id: number;
  prenomUser: string;
  nomUser: string;
  telephoneUser: string;
  emailUser?: string;
  dateAdhesion: string;
  statut: 'PENDING' | 'APPROVED' | 'REJECTED';
}

interface PaiementHistoriqueResponse {
  id: number;
  montant: number;
  datePaiement: string;
  modePaiement: 'ORANGE_MONEY' | 'WAVE' | 'FREE_MONEY';
  reference: string;
  valide: boolean;
  titreCotisation: string;
  cotisationId: number;
  membreNom: string;
  membrePrenom: string;
}

interface PaiementResponse {
  id: number;
  montant: number;
  datePaiement: string;
  modePaiement: 'ORANGE_MONEY' | 'WAVE' | 'FREE_MONEY';
  reference: string;
  valide: boolean;
  cotisationId: number;
}

@Component({
  selector: 'app-administration',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './administration.html',
  styleUrls: ['./administration.scss']
})
export class Administration implements OnInit {

  // Données
  tontine: Tontine | null = null;
  requests: DemandeAdhesion[] = [];
  payments: PaiementResponse[] = [];
  paiementsHistorique: PaiementHistoriqueResponse[] = [];
  
  // États
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      await this.loadData(parseInt(id));
    } else {
      this.errorMessage = 'ID de tontine non trouvé';
      this.isLoading = false;
    }
  }

  private async loadData(tontineId: number): Promise<void> {
    this.isLoading = true;
    this.errorMessage = '';
    
    try {
      // 1. Charger la tontine
      // GET /api/tontine/{id}
      this.tontine = await this.apiService.get<Tontine>(`/tontine/${tontineId}`);
      
      // 2. Charger les demandes d'adhésion en attente
      // GET /api/tontine/{id}/adhesion
      const allRequests = await this.apiService.get<DemandeAdhesion[]>(`/tontine/${tontineId}/adhesion`);
      this.requests = allRequests.filter(r => r.statut === 'PENDING');
      
      // 3. Charger l'historique des paiements de la tontine
      // GET /api/paiements/tontine/{id}/historique
      // Attention: baseUrl = 'http://localhost:8080/api' donc on ajoute juste '/paiements/...'
      this.paiementsHistorique = await this.apiService.get<PaiementHistoriqueResponse[]>(`/paiements/tontine/${tontineId}/historique`);
      
      // Les paiements à valider sont ceux avec valide = false (en attente)
      this.payments = this.paiementsHistorique
        .filter(p => p.valide === false)
        .map(p => ({
          id: p.id,
          montant: p.montant,
          datePaiement: p.datePaiement,
          modePaiement: p.modePaiement,
          reference: p.reference,
          valide: p.valide,
          cotisationId: p.cotisationId
        }));
      
      console.log('✅ Tontine chargée:', this.tontine);
      console.log('✅ Demandes en attente:', this.requests.length);
      console.log('✅ Paiements à valider:', this.payments.length);
      
    } catch (error) {
      console.error('❌ Erreur chargement:', error);
      this.errorMessage = 'Impossible de charger les données. Vérifiez votre connexion.';
    } finally {
      this.isLoading = false;
    }
  }

  // ========== GESTION DES DEMANDES D'ADHÉSION ==========

  async acceptRequest(index: number): Promise<void> {
    const request = this.requests[index];
    if (!request || request.statut !== 'PENDING') return;
    
    try {
      // PUT /api/tontine/adhesion/{id}/approve
      await this.apiService.put(`/tontine/adhesion/${request.id}/approve`, {});
      alert(`✅ Demande de ${request.prenomUser} ${request.nomUser} approuvée !`);
      
      if (this.tontine) {
        await this.loadData(this.tontine.id);
      }
    } catch (error) {
      console.error('Erreur approbation:', error);
      alert('❌ Erreur lors de l\'approbation');
    }
  }

  async rejectRequest(index: number): Promise<void> {
    const request = this.requests[index];
    if (!request || request.statut !== 'PENDING') return;
    
    try {
      // PUT /api/tontine/adhesion/{id}/reject
      await this.apiService.put(`/tontine/adhesion/${request.id}/reject`, {});
      alert(`❌ Demande de ${request.prenomUser} ${request.nomUser} rejetée`);
      
      if (this.tontine) {
        await this.loadData(this.tontine.id);
      }
    } catch (error) {
      console.error('Erreur rejet:', error);
      alert('❌ Erreur lors du rejet');
    }
  }

  // ========== GESTION DES PAIEMENTS ==========

  async validerPaiement(index: number): Promise<void> {
    const payment = this.payments[index];
    if (!payment) return;
    
    try {
      // PUT /api/paiements/{id}/valider
      await this.apiService.put(`/paiements/${payment.id}/valider`, {});
      alert(`✅ Paiement de ${payment.montant} FCFA validé !`);
      
      if (this.tontine) {
        await this.loadData(this.tontine.id);
      }
    } catch (error) {
      console.error('Erreur validation paiement:', error);
      alert('❌ Erreur lors de la validation du paiement');
    }
  }

  async rejeterPaiement(index: number): Promise<void> {
    const payment = this.payments[index];
    if (!payment) return;
    
    try {
      // PUT /api/paiements/{id}/rejeter
      await this.apiService.put(`/paiements/${payment.id}/rejeter`, {});
      alert(`❌ Paiement de ${payment.montant} FCFA rejeté !`);
      
      if (this.tontine) {
        await this.loadData(this.tontine.id);
      }
    } catch (error) {
      console.error('Erreur rejet paiement:', error);
      alert('❌ Erreur lors du rejet du paiement');
    }
  }

  // ========== UTILITAIRES ==========

  getStatusLabel(payment: PaiementResponse): string {
    if (payment.valide === true) return 'Confirmé';
    if (payment.valide === false) return 'Rejeté';
    return 'En attente';
  }

  getStatusClass(payment: PaiementResponse): string {
    if (payment.valide === true) return 'confirmed';
    if (payment.valide === false) return 'rejected';
    return 'pending';
  }

  getRequestLabel(status: string): string {
    const labels: Record<string, string> = {
      'PENDING': 'En attente',
      'APPROVED': 'Approuvé',
      'REJECTED': 'Rejeté'
    };
    return labels[status] || status;
  }

  getMethodLabel(method: string): string {
    const labels: Record<string, string> = {
      'ORANGE_MONEY': 'Orange Money',
      'WAVE': 'Wave',
      'FREE_MONEY': 'Free Money'
    };
    return labels[method] || method;
  }

  // Statistiques calculées dynamiquement
  get stats() {
    return {
      membresActifs: this.tontine?.nombreMembres || 0,
      demandesEnAttente: this.requests.length,
      paiementsAValider: this.payments.length,
      transactionsConfirmees: this.paiementsHistorique.filter(p => p.valide === true).length
    };
  }

  // Retour à la page détail
  goBack(): void {
    if (this.tontine) {
      this.router.navigate(['/tontine', this.tontine.id]);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }
}