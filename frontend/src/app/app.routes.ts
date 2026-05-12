import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { TontineCreate } from './pages/tontine-create/tontine-create';
import { Paiement } from './pages/paiement/paiement';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard },
  { path: 'tontine/create', component: TontineCreate },
  { path: 'paiement/:id', component: Paiement },
  { path: '**', redirectTo: '/dashboard' }
];