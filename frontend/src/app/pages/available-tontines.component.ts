import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Tontine {
  id: number;
  title: string;
  description: string;
  amount: number;
  amountDisplay: string;
  type: string;
  location: string;
  members: number;
  maxMembers: number;
  startDate: string;
  creator: string;
  frequency: string;
  duration: string;
  joined?: boolean;
}

@Component({
  selector: 'app-available-tontines',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './available-tontines.component.html',
  styleUrls: ['./available-tontines.component.scss']
})
export class AvailableTontinesComponent implements OnInit {
  tontines: Tontine[] = [
    {
      id: 1,
      title: 'Tontine Famille',
      description: 'Une tontine solidaire pour l\'épargne en famille',
      amount: 5000,
      amountDisplay: '5 000 FCFA',
      type: 'Familiale',
      location: 'Dakar',
      members: 8,
      maxMembers: 20,
      startDate: '15 Mai 2026',
      creator: 'Amadou Sall',
      frequency: 'Mensuel',
      duration: '20 mois'
    },
    {
      id: 2,
      title: 'Tontine des Commerçants',
      description: 'Pour les petits commerçants de la région',
      amount: 15000,
      amountDisplay: '15 000 FCFA',
      type: 'Professionnelle',
      location: 'Thiès',
      members: 25,
      maxMembers: 30,
      startDate: '1 Juin 2026',
      creator: 'Fatou Ba',
      frequency: 'Bi-mensuel',
      duration: '30 mois'
    },
    {
      id: 3,
      title: 'Projet Immobilier',
      description: 'Financement collectif pour un projet immobilier',
      amount: 50000,
      amountDisplay: '50 000 FCFA',
      type: 'Projet',
      location: 'Dakar',
      members: 28,
      maxMembers: 30,
      startDate: '17 Mai 2026',
      creator: 'Moussa Diallo',
      frequency: 'Mensuel',
      duration: '36 mois'
    },
    {
      id: 4,
      title: 'Épargne Solidaire',
      description: 'Tontine d\'épargne collective pour tous',
      amount: 10000,
      amountDisplay: '10 000 FCFA',
      type: 'Familiale',
      location: 'Saint-Louis',
      members: 12,
      maxMembers: 25,
      startDate: '20 Mai 2026',
      creator: 'Aïssatou Diouf',
      frequency: 'Mensuel',
      duration: '24 mois'
    },
    {
      id: 5,
      title: 'Tontine Femmes Entrepreneur',
      description: 'Pour soutenir les femmes entrepreneuses',
      amount: 20000,
      amountDisplay: '20 000 FCFA',
      type: 'Professionnelle',
      location: 'Kaolack',
      members: 15,
      maxMembers: 20,
      startDate: '10 Juin 2026',
      creator: 'Mariam Ndiaye',
      frequency: 'Mensuel',
      duration: '18 mois'
    },
    {
      id: 6,
      title: 'Fonds d\'Investissement',
      description: 'Tontine pour investissements collectifs',
      amount: 100000,
      amountDisplay: '100 000 FCFA',
      type: 'Projet',
      location: 'Dakar',
      members: 5,
      maxMembers: 10,
      startDate: '1 Juillet 2026',
      creator: 'Ali Ba',
      frequency: 'Mensuel',
      duration: '60 mois'
    }
  ];

  filteredTontines: Tontine[] = [];
  selectedFilter = 'Toutes';
  searchText = '';
  sortBy = 'recent';
  showFilters = false;
  selectedTontineId: number | null = null;

  filters = ['Toutes', 'Familiale', 'Professionnelle', 'Projet'];
  sortOptions = [
    { value: 'recent', label: 'Plus récent' },
    { value: 'members', label: 'Nombre de membres' },
    { value: 'amount', label: 'Montant' }
  ];

  joinedTontines: number[] = [];

  constructor(private router: Router) {}

  ngOnInit() {
    this.applyFilters();
    this.loadJoinedTontines();
  }

  applyFilters() {
    let result = this.tontines;

    // Filtre par type
    if (this.selectedFilter !== 'Toutes') {
      result = result.filter(t => t.type === this.selectedFilter);
    }

    // Filtre par recherche
    if (this.searchText.trim()) {
      const search = this.searchText.toLowerCase();
      result = result.filter(t =>
        t.title.toLowerCase().includes(search) ||
        t.location.toLowerCase().includes(search) ||
        t.description.toLowerCase().includes(search)
      );
    }

    // Tri
    result = this.sortTontines(result);

    this.filteredTontines = result;
  }

  sortTontines(tontines: Tontine[]): Tontine[] {
    const sorted = [...tontines];
    switch (this.sortBy) {
      case 'members':
        return sorted.sort((a, b) => (b.maxMembers - b.members) - (a.maxMembers - a.members));
      case 'amount':
        return sorted.sort((a, b) => a.amount - b.amount);
      default:
        return sorted;
    }
  }

  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  setFilter(filter: string) {
    this.selectedFilter = filter;
    this.applyFilters();
  }

  onSortChange() {
    this.applyFilters();
  }

  onSearchChange() {
    this.applyFilters();
  }

  toggleTontineDetails(id: number) {
    this.selectedTontineId = this.selectedTontineId === id ? null : id;
  }

  getProgressPercentage(tontine: Tontine): number {
    return (tontine.members / tontine.maxMembers) * 100;
  }

  canJoin(tontine: Tontine): boolean {
    return tontine.members < tontine.maxMembers && !this.joinedTontines.includes(tontine.id);
  }

  joinTontine(tontine: Tontine) {
    if (this.canJoin(tontine)) {
      this.joinedTontines.push(tontine.id);
      this.saveTontinesLocally();
      alert(`Vous avez adhéré à "${tontine.title}"!`);
    }
  }

  viewDetails(tontine: Tontine) {
    this.router.navigate(['/detail'], { state: { tontine } });
  }

  private loadJoinedTontines() {
    const saved = localStorage.getItem('joinedTontines');
    if (saved) {
      this.joinedTontines = JSON.parse(saved);
    }
  }

  private saveTontinesLocally() {
    localStorage.setItem('joinedTontines', JSON.stringify(this.joinedTontines));
  }

  isJoined(id: number): boolean {
    return this.joinedTontines.includes(id);
  }
}
