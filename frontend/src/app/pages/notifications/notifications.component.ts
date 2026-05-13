import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent {
  notifications = [
    { title: 'Paiement validé', message: 'Votre cotisation de 5 000 FCFA a été validée', time: '2h', color: '#5bbf7e', read: false },
    { title: 'Rappel de cotisation', message: 'Votre cotisation arrive à échéance dans 3 jours', time: '5h', color: '#ffc86a', read: false },
    { title: 'Nouveau membre', message: 'Khadidja Sow a rejoint votre tontine', time: '1j', color: '#97b3ff', read: false },
    { title: 'Cotisation en retard', message: 'Votre cotisation de Mai est en retard', time: '1j', color: '#ff7262', read: false }
  ];

  markAllRead() {
    this.notifications = this.notifications.map((notification) => ({
      ...notification,
      read: true
    }));
  }
}
