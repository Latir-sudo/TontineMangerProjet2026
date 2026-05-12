import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-tontines',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './tontines.component.html',
  styleUrls: ['./tontines.component.scss']
})
export class TontinesComponent {
  tontines = [
    { title: 'Épargne Solidaire', amount: '5 000 FCFA', type: 'Familiale', location: 'Dakar', members: '15/20', start: '15 Mai 2026' },
    { title: 'Tontine des Commerçants', amount: '15 000 FCFA', type: 'Professionnelle', location: 'Thiès', members: '25/30', start: '1 Juin 2026' },
    { title: 'Projet immobilier', amount: '50 000 FCFA', type: 'Projet', location: 'Dakar', members: '30/30', start: '17 Mai 2026' }
  ];

  showFilters = false;
  selectedFilter = 'Toutes';
  filters = ['Toutes', 'Familiale', 'Professionnelle', 'Projet'];

  constructor(private router: Router) {}

  get visibleTontines() {
    return this.selectedFilter === 'Toutes'
      ? this.tontines
      : this.tontines.filter((t) => t.type === this.selectedFilter);
  }

  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  setFilter(filter: string) {
    this.selectedFilter = filter;
  }

  openDetail() {
    this.router.navigate(['/detail']);
  }
}
