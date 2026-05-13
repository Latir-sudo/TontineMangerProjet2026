import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-create-tontine',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './create-tontine.component.html',
  styleUrls: ['./create-tontine.component.scss']
})
export class CreateTontineComponent {
  form = {
    name: '',
    category: '',
    location: 'Dakar',
    amount: 5000,
    frequency: '',
    maxMembers: 20
  };

  categories = ['Épargne solidaire', 'Famille', 'Travail', 'Communauté'];
  frequencies = ['Hebdomadaire', 'Mensuelle', 'Quinzaine', 'Ponctuelle'];

  constructor(private router: Router) {}

  create() {
    this.router.navigate(['/tontines']);
  }
}
