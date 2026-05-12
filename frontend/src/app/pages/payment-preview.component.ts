import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-payment-preview',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './payment-preview.component.html',
  styleUrls: ['./payment-preview.component.scss']
})
export class PaymentPreviewComponent {
  constructor(private router: Router) {}

  proceed() {
    this.router.navigate(['/payment']);
  }
}
