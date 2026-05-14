import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.services';

interface Tontine {
  id: number;
  nomTontine: string;
  descriptionTontine?: string;
  montant: number;
  frequence?: string;
  region?: string;
  categorie?: string;
  nombreMembres?: number;
  nombreMax?: number;
  statutTontine?: string;
  admin?: {
    id: number;
    nom: string;
    prenom: string;
  };
}

@Component({
  selector: 'app-tontine-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './detail-tontine.html',
  styleUrls: ['./detail-tontine.scss']
})
export class DetailTontine implements OnInit {
  
  tontine: Tontine | null = null;
  isLoading = true;
  errorMessage = '';
  isJoined = false;

  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      await this.loadTontineDetail(parseInt(id));
      await this.checkIfJoined();
      this.cdr.detectChanges();
    } else {
      this.errorMessage = 'ID de tontine non trouvé';
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }

  private async loadTontineDetail(id: number) {
    this.isLoading = true;
    this.cdr.detectChanges();
    try {
      // Récupérer les détails de la tontine
      this.tontine = await this.apiService.get<Tontine>(`/tontine/${id}`);
      console.log('Détails tontine chargés:', this.tontine);
    } catch (error) {
      console.error('Erreur chargement détail:', error);
      this.errorMessage = 'Impossible de charger les détails de la tontine';
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }

  private async checkIfJoined() {
    if (!this.tontine) return;
    try {
      const mesTontines = await this.apiService.get<Tontine[]>('/tontine/mes-tontines');
      this.isJoined = mesTontines.some(t => t.id === this.tontine?.id);
    } catch (error) {
      this.isJoined = false;
    } finally {
      this.cdr.detectChanges();
    }
  }

  async join() {
    if (!this.tontine) return;
    
    try {
      await this.apiService.post('/tontine/adhesion', { idTontine: this.tontine.id });
      alert('✅ Demande d\'adhésion envoyée avec succès !');
      this.isJoined = true;
    } catch (error) {
      console.error('Erreur adhésion:', error);
      alert('Erreur lors de la demande d\'adhésion');
    }
  }

 getProgressPercentage(): number {
  if (!this.tontine || !this.tontine.nombreMembres) return 0;
  const max = this.tontine.nombreMax || 20;
  const percentage = (this.tontine.nombreMembres / max) * 100;
  return Math.min(percentage, 100);
}
}