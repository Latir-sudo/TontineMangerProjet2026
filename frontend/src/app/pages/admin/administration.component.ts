import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-administration',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './administration.component.html',
  styleUrls: ['./administration.component.scss']
})
export class AdministrationComponent {
  requests = [
    { name: 'Khadidji Sow', phone: '+221 77 453 23 54', date: '6 mai', status: 'pending' },
    { name: 'Oumar Faye', phone: '+221 77 453 23 54', date: '5 mai', status: 'pending' }
  ];

  payments = [
    { name: 'Fatou Seck', amount: '5 000 FCFA', status: 'pending' },
    { name: 'Moussa Ndiaye', amount: '5 000 FCFA', status: 'pending' }
  ];

  acceptRequest(index: number) {
    this.requests[index].status = 'accepted';
  }

  rejectRequest(index: number) {
    this.requests[index].status = 'rejected';
  }

  confirmPayment(index: number) {
    this.payments[index].status = 'confirmed';
  }

  getRequestLabel(status: string) {
    if (status === 'accepted') {
      return 'Accepté';
    }
    if (status === 'rejected') {
      return 'Refusé';
    }
    return 'En attente';
  }

  getPaymentLabel(status: string) {
    if (status === 'confirmed') {
      return 'Confirmé';
    }
    return 'En attente';
  }
}
