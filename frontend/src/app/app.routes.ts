import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { CreateTontineComponent } from './pages/create-tontine/create-tontine.component';
import { SignupComponent } from './pages/signup/signup.component';
import { TontinesComponent } from './pages/tontines/tontines.component';
import { DetailTontineComponent } from './pages/detail-tontine/detail-tontine.component';
import { PaymentPreviewComponent } from './pages/payment-preview/payment-preview.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { ValidatePaymentComponent } from './pages/validate-payment/validate-payment.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { AdministrationComponent } from './pages/admin/administration.component';
import { HistoryComponent } from './pages/history/history.component';
import { AvailableTontinesComponent } from './pages/available-tontines/available-tontines.component';
import { BrowseTontinesComponent } from './pages/browse-tontines/browse-tontines.component';
import { AddMemberComponent } from './pages/tontines/add-member/add-member.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: DashboardComponent },
  { path: 'browse', component: BrowseTontinesComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: SignupComponent },
  { path: 'create', component: CreateTontineComponent },
  { path: 'tontines', component: TontinesComponent },
  { path: 'tontines/add-member', component: AddMemberComponent },
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
