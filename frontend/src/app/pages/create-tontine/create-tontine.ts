import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { AuthService } from '../../services/auth.services';

@Component({
  selector: 'app-tontine-create',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './create-tontine.html',
  styleUrls: ['./create-tontine.scss']
})
export class TontineCreate implements OnInit {
  
  // Formulaire - correspond exactement au DTO TontineRequest
  form = {
    nomTontine: '',
    frequence: 'MENSUEL',
    montant: 0,
    dateCreation: new Date().toISOString().split('T')[0], // Format YYYY-MM-DD
    descriptionTontine: '',
    politiqueTontine: 'Standard',
    categorie: '',
    region: 'Dakar',
    nombreMax: 10
  };
  
  // Options pour les sélecteurs
  categories = ['Épargne', 'Investissement', 'Sociale', 'Commerce', 'Famille', 'Solidaire'];
  frequencies = ['MENSUEL', 'HEBDOMADAIRE', 'BIMENSUEL', 'TRIMESTRIEL'];
  localites = ['Dakar', 'Thiès', 'Saint-Louis', 'Kaolack', 'Touba', 'Ziguinchor'];
  
  // États
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  editingTontineId: number | null = null;
  isEditing = false;

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  async ngOnInit() {
    // Vérifier si nous sommes en mode édition
    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.editingTontineId = parseInt(id);
      this.isEditing = true;
      await this.loadTontineData(this.editingTontineId);
    }
  }

  private async loadTontineData(id: number) {
    this.isLoading = true;
    try {
      const tontine = await this.apiService.get<any>(`/tontine/${id}`);
      this.form = {
        nomTontine: tontine.nomTontine,
        frequence: tontine.frequence || 'MENSUEL',
        montant: tontine.montant,
        dateCreation: tontine.dateCreation || new Date().toISOString().split('T')[0],
        descriptionTontine: tontine.descriptionTontine || '',
        politiqueTontine: tontine.politiqueTontine || 'Standard',
        categorie: tontine.categorie || '',
        region: tontine.region || 'Dakar',
        nombreMax: tontine.nombreMax || 10
      };
    } catch (error) {
      this.errorMessage = 'Impossible de charger les données de la tontine';
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  }

  async create() {
    // Réinitialisation des messages
    this.errorMessage = '';
    this.successMessage = '';
    
    // Validation
    if (!this.form.nomTontine.trim()) {
      this.errorMessage = 'Le nom de la tontine est requis';
      return;
    }
    
    if (this.form.montant < 1000) {
      this.errorMessage = 'Le montant doit être au minimum de 1000 FCFA';
      return;
    }
    
    if (!this.form.categorie) {
      this.errorMessage = 'Veuillez sélectionner une catégorie';
      return;
    }
    
    if (this.form.nombreMax < 2) {
      this.errorMessage = 'Le nombre maximum de membres doit être au moins 2';
      return;
    }
    
    this.isLoading = true;
    
    try {
      // Données exactement comme attendu par le backend TontineRequest
      const tontineData = {
        nomTontine: this.form.nomTontine,
        frequence: this.form.frequence,
        montant: this.form.montant,
        dateCreation: this.form.dateCreation,
        descriptionTontine: this.form.descriptionTontine || null,
        politiqueTontine: this.form.politiqueTontine,
        categorie: this.form.categorie,
        region: this.form.region,
        nombreMax: this.form.nombreMax
      };
      
      let response: any;
      let tontineId: number;

      if (this.isEditing && this.editingTontineId) {
        // Mode édition - utiliser PUT
        console.log('📤 Mise à jour tontine:', tontineData);
        response = await this.apiService.put(`/tontine/${this.editingTontineId}`, tontineData);
        tontineId = this.editingTontineId;
        this.successMessage = 'Tontine modifiée avec succès ! Redirection...';
      } else {
        // Mode création - utiliser POST
        console.log('📤 Création tontine:', tontineData);
        response = await this.apiService.post('/tontine', tontineData);
        tontineId = response.id;
        this.successMessage = 'Tontine créée avec succès ! Redirection...';
      }
      
      console.log('✅ Réponse:', response);
      
      // Redirection vers la page de détail après 1.5 secondes
      setTimeout(() => {
        this.router.navigate(['/tontine', tontineId]);
      }, 1500);
      
    } catch (error: any) {
      console.error('❌ Erreur:', error);
      this.errorMessage = error.error?.message || 'Erreur lors de l\'opération';
    } finally {
      this.isLoading = false;
    }
  }
}