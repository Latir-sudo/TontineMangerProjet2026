import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { LoginComponent } from './login.component';
import { CreateTontineComponent } from './create-tontine.component';
import { SignupComponent } from './pages/signup.component';
import { TontinesComponent } from './pages/tontines.component';
import { DetailTontineComponent } from './pages/detail-tontine.component';
import { PaymentPreviewComponent } from './pages/payment-preview.component';
import { PaymentComponent } from './pages/payment.component';
import { ValidatePaymentComponent } from './pages/validate-payment.component';
import { NotificationsComponent } from './pages/notifications.component';
import { ProfileComponent } from './pages/profile.component';
import { AdministrationComponent } from './pages/administration.component';
import { HistoryComponent } from './pages/history.component';
import { AvailableTontinesComponent } from './pages/available-tontines.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: SignupComponent },
  { path: 'create', component: CreateTontineComponent },
  { path: 'tontines', component: TontinesComponent },
  { path: 'available-tontines', component: AvailableTontinesComponent },
  { path: 'detail', component: DetailTontineComponent },
  { path: 'payment-preview', component: PaymentPreviewComponent },
  { path: 'payment', component: PaymentComponent },
  { path: 'validate-payment', component: ValidatePaymentComponent },
  { path: 'notifications', component: NotificationsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'administration', component: AdministrationComponent },
  { path: 'history', component: HistoryComponent },
  { path: '**', redirectTo: '' }
];
