import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

interface Tontine {
  id: number;
  nomTontine: string;
  montant: number;
  nombreMembres?: number;
}

interface DemandeAdhesion {
  id: number;
  prenomUser: string;
  nomUser: string;
  telephoneUser: string;
  emailUser?: string;
  dateAdhesion: string;
  statut: 'pending' | 'approved' | 'rejected';
}

interface PaiementAValider {
  id: number;
  nomUser: string;
  prenomUser: string;
  montant: number;
  datePaiement: string;
  statut: 'pending' | 'confirmed' | 'rejected';
  reference: string;
}

@Component({
  selector: 'app-administration',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './administration.html',
  styleUrls: ['./administration.scss']
})
export class Administration implements OnInit {

  // Données
  tontine: Tontine | null = null;
  requests: DemandeAdhesion[] = [];
  payments: PaiementAValider[] = [];
  
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
    try {
      // 1. Charger la tontine
      this.tontine = await this.apiService.get<Tontine>(`/tontine/${tontineId}`);
      
      // 2. Charger les demandes d'adhésion en attente
      const allRequests = await this.apiService.get<DemandeAdhesion[]>(`/tontine/${tontineId}/adhesion`);
      this.requests = allRequests.filter(r => r.statut === 'pending');
      
      // 3. Charger les paiements à valider
      const allPayments = await this.apiService.get<PaiementAValider[]>(`/tontine/${tontineId}/paiements`);
      this.payments = allPayments.filter(p => p.statut === 'pending');
      
      console.log('Tontine:', this.tontine);
      console.log('Demandes en attente:', this.requests.length);
      console.log('Paiements à valider:', this.payments.length);
      
    } catch (error) {
      console.error('Erreur chargement:', error);
      this.errorMessage = 'Impossible de charger les données';
    } finally {
      this.isLoading = false;
    }
  }

  // ========== GESTION DES DEMANDES D'ADHÉSION ==========

  async acceptRequest(index: number): Promise<void> {
    const request = this.requests[index];
    if (!request || request.statut !== 'pending') return;
    
    try {
      await this.apiService.put(`/tontine/adhesion/${request.id}/approve`, {});
      alert(`✅ Demande de ${request.prenomUser} ${request.nomUser} approuvée !`);
      
      // Recharger les données
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
    if (!request || request.statut !== 'pending') return;
    
    try {
      await this.apiService.put(`/tontine/adhesion/${request.id}/reject`, {});
      alert(`❌ Demande de ${request.prenomUser} ${request.nomUser} rejetée`);
      
      // Recharger les données
      if (this.tontine) {
        await this.loadData(this.tontine.id);
      }
    } catch (error) {
      console.error('Erreur rejet:', error);
      alert('❌ Erreur lors du rejet');
    }
  }

  // ========== GESTION DES PAIEMENTS ==========

  async confirmPayment(index: number): Promise<void> {
    const payment = this.payments[index];
    if (!payment || payment.statut !== 'pending') return;
    
    try {
      await this.apiService.put(`/tontine/paiement/${payment.id}/confirm`, {});
      alert(`✅ Paiement de ${payment.prenomUser} ${payment.nomUser} confirmé !`);
      
      // Recharger les données
      if (this.tontine) {
        await this.loadData(this.tontine.id);
      }
    } catch (error) {
      console.error('Erreur confirmation paiement:', error);
      alert('❌ Erreur lors de la confirmation du paiement');
    }
  }

  // ========== UTILITAIRES ==========

  getRequestLabel(status: string): string {
    const labels: Record<string, string> = {
      'pending': 'En attente',
      'approved': 'Approuvé',
      'rejected': 'Rejeté'
    };
    return labels[status] || status;
  }

  getPaymentLabel(status: string): string {
    const labels: Record<string, string> = {
      'pending': 'En attente',
      'confirmed': 'Confirmé',
      'rejected': 'Rejeté'
    };
    return labels[status] || status;
  }

  // Statistiques calculées dynamiquement
  get stats() {
    return {
      membresActifs: this.tontine?.nombreMembres || 0,
      demandesEnAttente: this.requests.length,
      paiementsAValider: this.payments.length,
      transactionsConfirmees: this.payments.filter(p => p.statut === 'confirmed').length
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