import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-paiement',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './paiement.html',
  styleUrls: ['./paiement.scss']
})
export class Paiement {}