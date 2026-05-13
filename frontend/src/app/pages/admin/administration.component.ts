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

  validations = [
    { name: 'Fatou Seck', amount: '5 000 FCFA' },
    { name: 'Moussa Ndiaye', amount: '5 000 FCFA' }
  ];

  constructor(private router: Router) {}

  acceptRequest(index: number) {
    this.requests[index].status = 'accepted';
  }

  rejectRequest(index: number) {
    this.requests[index].status = 'rejected';
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

  validate() {
    this.router.navigate(['/validate-payment']);
  }
}
