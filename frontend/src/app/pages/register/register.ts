import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class Register {
  userData = {
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    password: '',
    ville: 'DAKAR',
    roles: ['USER']
  };
  
  errorMessage = '';
  successMessage = '';
  isLoading = false;

  localites = ['DAKAR', 'THIES', 'SAINT-LOUIS', 'TOUBA', 'ZIGUINCHOR'];

  constructor(private authService: AuthService, private router: Router) {}

  async onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';
    
    try {
      const result = await this.authService.register(this.userData);
      
      if (result.success) {
        this.successMessage = result.message;
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1500);
      } else {
        this.errorMessage = result.message;
      }
    } catch (error) {
      this.errorMessage = 'Erreur de connexion au serveur';
    } finally {
      this.isLoading = false;
    }
  }
}