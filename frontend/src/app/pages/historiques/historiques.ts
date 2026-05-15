// pages/historiques/historiques.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';  // ← À importer
import { PaiementService } from '../../services/paiement.service';
import { PaiementHistorique, PaiementStats } from '../../models/paiement.model';

@Component({
  selector: 'app-historiques',
  standalone: true,  // ← Si composant standalone
  imports: [CommonModule],  // ← Ajoute CommonModule ici
  templateUrl: './historiques.html',
  styleUrls: ['./historiques.scss']
})
// OU si tu utilises NgModule, vois la solution alternative ci-dessous
export class Historiques implements OnInit {

  filter: 'all' | 'success' | 'pending' | 'failed' = 'all';
  payments: PaiementHistorique[] = [];
  stats: PaiementStats | null = null;
  isLoading: boolean = true;
  errorMessage: string | null = null;

  currentMembreId: number = 1;

  constructor(private paiementService: PaiementService) { }

  ngOnInit(): void {
    this.loadHistorique();
    this.loadStats();
  }

  loadHistorique(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.paiementService.getHistoriqueByMembre(this.currentMembreId).subscribe({
      next: (data) => {
        this.payments = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.errorMessage = 'Impossible de charger votre historique. Veuillez réessayer.';
        this.isLoading = false;
      }
    });
  }

  loadStats(): void {
    this.paiementService.getStatsByMembre(this.currentMembreId).subscribe({
      next: (data) => {
        this.stats = data;
      },
      error: (err) => console.error('Erreur chargement stats:', err)
    });
  }

  get filteredPayments(): PaiementHistorique[] {
    switch (this.filter) {
      case 'success':
        return this.payments.filter(p => p.valide === true);
      case 'pending':
        return this.payments.filter(p => p.valide === null);
      case 'failed':
        return this.payments.filter(p => p.valide === false);
      default:
        return this.payments;
    }
  }

  setFilter(filter: 'all' | 'success' | 'pending' | 'failed'): void {
    this.filter = filter;
  }

  getStatusLabel(payment: PaiementHistorique): string {
    if (payment.valide === true) return 'Réussi';
    if (payment.valide === false) return 'Échoué';
    return 'En attente';
  }

  getStatusClass(payment: PaiementHistorique): string {
    if (payment.valide === true) return 'success';
    if (payment.valide === false) return 'failed';
    return 'pending';
  }

  getMethodLabel(method: string): string {
    const labels: Record<string, string> = {
      'ORANGE_MONEY': 'Orange Money',
      'WAVE': 'Wave',
      'FREE_MONEY': 'Free Money'
    };
    return labels[method] || method;
  }

  getFilterLabel(): string {
    const labels: Record<string, string> = {
      'all': '',
      'success': 'réussie',
      'pending': 'en attente',
      'failed': 'échouée'
    };
    return labels[this.filter];
  }

  getProgressPercentage(): number {
    if (this.stats && this.stats.totalAttendu > 0) {
      return (this.stats.totalPaye / this.stats.totalAttendu) * 100;
    }
    return 0;
  }
}