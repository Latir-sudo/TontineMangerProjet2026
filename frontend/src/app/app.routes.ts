import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { TontineCreate } from './pages/tontine-create/tontine-create';
import { Paiement } from './pages/paiement/paiement';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'inscription', component: Register },
  { path: 'dashboard', component: Dashboard, canActivate: [AuthGuard] },
  { path: 'tontine/create', component: TontineCreate, canActivate: [AuthGuard] },
  { path: 'paiement/:id', component: Paiement, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];