import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent {
  filter = 'all';
  payments = [
    { title: 'Tontine Famille', method: 'Wave', amount: '-5000 FCFA', status: 'Réussi', date: '14 Mai 2026', reference: '#TXN043713' },
    { title: 'Tontine Famille', method: 'Wave', amount: '-10 000 FCFA', status: 'Réussi', date: '10 Mai 2026', reference: '#TXN004713' },
    { title: 'Tontine Famille', method: 'Wave', amount: '-5000 FCFA', status: 'Échoué', date: '7 Mai 2026', reference: '#TXN064713' }
  ];
}
