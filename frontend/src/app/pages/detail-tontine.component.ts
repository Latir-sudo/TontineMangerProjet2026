import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-detail-tontine',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './detail-tontine.component.html',
  styleUrls: ['./detail-tontine.component.scss']
})
export class DetailTontineComponent {
  constructor(private router: Router) {}

  join() {
    this.router.navigate(['/payment-preview']);
  }
}
