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
  idAdmin?: number;  // ← UTILISER idAdmin
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
  isAdmin = false;
  currentUserId: number | null = null;

  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      await this.loadTontineDetail(parseInt(id));
      await this.checkIfJoined();
      this.checkIfAdmin();
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
      this.tontine = await this.apiService.get<Tontine>(`/tontine/${id}`);
      console.log('=== RÉPONSE API TONTINE ===');
      console.log('Tontine reçue:', this.tontine);
      console.log('idAdmin:', this.tontine?.idAdmin);
      console.log('===========================');
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

  private checkIfAdmin() {
    if (!this.tontine) {
      console.warn('⚠️ Tontine non chargée');
      this.isAdmin = false;
      return;
    }
    
    const adminId = this.tontine.idAdmin;
    
    if (!adminId) {
      console.warn('⚠️ Pas d\'idAdmin trouvé');
      this.isAdmin = false;
      return;
    }
    
    const currentUser = this.authService.getCurrentUser();
    console.log('🔍 Vérification admin:', {
      tontineAdminId: adminId,
      currentUserId: currentUser?.id,
      currentUserEmail: currentUser?.email
    });
    
    if (!currentUser) {
      console.warn('⚠️ Utilisateur non connecté');
      this.isAdmin = false;
      return;
    }
    
    this.isAdmin = adminId === currentUser.id;
    console.log(`${this.isAdmin ? '✅' : '❌'} Admin: ${this.isAdmin}`);
  }

  editTontine() {
    if (this.tontine) {
      this.router.navigate(['/tontine/create'], { queryParams: { id: this.tontine.id } });
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