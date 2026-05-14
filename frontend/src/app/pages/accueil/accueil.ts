import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Tontine {
  id: number;
  title: string;
  type: string;
  location: string;
  montant: number;
  members: number;
  maxMembers: number;
  description: string;
  benefits: string[];
  frequency: string;
  startDate: string;
  duration: string;
  organizer: string;
}

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './accueil.html',
  styleUrls: ['./accueil.scss']
})
export class Accueil {
  searchText = signal('');
  selectedType = signal('');
  selectedTontineId = signal<number | null>(null);

  tontines: Tontine[] = [
    {
      id: 1,
      title: 'Tontine Commerce',
      type: 'Commerce',
      location: 'Dakar',
      montant: 15000,
      members: 25,
      maxMembers: 30,
      description: 'Une tontine dynamique destinée aux commerçants et petits entrepreneurs pour financer leurs activités',
      benefits: ['Prêt prioritaire', 'Taux préférentiel', 'Accompagnement commercial', 'Réseau d\'affaires'],
      frequency: 'Mensuel',
      startDate: '15 Janvier 2026',
      duration: '12 mois',
      organizer: 'Amadou Diallo'
    },
    {
      id: 2,
      title: 'Épargne Jeunesse',
      type: 'Jeunesse',
      location: 'Pikine',
      montant: 5000,
      members: 18,
      maxMembers: 25,
      description: 'Tontine inclusive créée pour les jeunes professionnels qui souhaitent épargner collectivement',
      benefits: ['Formations gratuites', 'Mentorat', 'Accès à des opportunités', 'Réseau de jeunes'],
      frequency: 'Bi-mensuel',
      startDate: '01 Février 2026',
      duration: '12 mois',
      organizer: 'Fatou Seck'
    },
    {
      id: 3,
      title: 'Solidarité Femmes',
      type: 'Femmes',
      location: 'Rufisque',
      montant: 8000,
      members: 12,
      maxMembers: 20,
      description: 'Tontine solidaire pour l\'autonomisation et l\'épargne collective des femmes',
      benefits: ['Prêts sans intérêts', 'Formations en entrepreneuriat', 'Garde d\'enfants', 'Support mutuel'],
      frequency: 'Mensuel',
      startDate: '10 Mars 2026',
      duration: '18 mois',
      organizer: 'Mariama Ba'
    },
    {
      id: 4,
      title: 'Investissement Immobilier',
      type: 'Immobilier',
      location: 'Thiès',
      montant: 50000,
      members: 8,
      maxMembers: 15,
      description: 'Tontine spécialisée dans l\'acquisition de propriétés et l\'investissement immobilier collectif',
      benefits: ['Mise en commun', 'Expertise juridique', 'Suivi administratif', 'Rendement garanti'],
      frequency: 'Mensuel',
      startDate: '01 Avril 2026',
      duration: '24 mois',
      organizer: 'Modou Gueye'
    },
    {
      id: 5,
      title: 'Épargne Éducation',
      type: 'Éducation',
      location: 'Saint-Louis',
      montant: 12000,
      members: 20,
      maxMembers: 30,
      description: 'Tontine dédiée à financer l\'éducation des enfants à travers une épargne collective',
      benefits: ['Bourses scolaires', 'Matériel pédagogique', 'Encadrement académique', 'Partenariats écoles'],
      frequency: 'Mensuel',
      startDate: '01 Mai 2026',
      duration: '36 mois',
      organizer: 'Aïssatou Ndiaye'
    },
    {
      id: 6,
      title: 'Tontine Santé',
      type: 'Santé',
      location: 'Kaolack',
      montant: 10000,
      members: 15,
      maxMembers: 25,
      description: 'Tontine pour la couverture mutuelle de santé et l\'accès aux services médicaux',
      benefits: ['Mutuelle de santé', 'Consultations gratuites', 'Pharmacie partenaire', 'Urgences couvertes'],
      frequency: 'Mensuel',
      startDate: '15 Mai 2026',
      duration: '12 mois',
      organizer: 'Dr. Saliou Ciss'
    }
  ];

  filteredTontines = signal<Tontine[]>(this.tontines);

  toggleTontineDetails(id: number) {
    this.selectedTontineId.update(current => current === id ? null : id);
  }

  onSearchChange() {
    this.updateFilter();
  }

  onTypeChange() {
    this.updateFilter();
  }

  updateFilter() {
    const search = this.searchText().toLowerCase();
    const type = this.selectedType();

    this.filteredTontines.set(
      this.tontines.filter(t => {
        const matchesSearch = t.title.toLowerCase().includes(search) ||
                            t.description.toLowerCase().includes(search) ||
                            t.location.toLowerCase().includes(search);
        const matchesType = !type || t.type === type;
        return matchesSearch && matchesType;
      })
    );
  }

  getTypes(): string[] {
    return [...new Set(this.tontines.map(t => t.type))];
  }

  getProgressPercentage(tontine: Tontine): number {
    return (tontine.members / tontine.maxMembers) * 100;
  }
}
