import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent {
  payment = {
    amount: 10000,
    method: 'wave',
    phone: ''
  };

  constructor(private router: Router) {}

  pay() {
    this.router.navigate(['/validate-payment']);
  }
}
