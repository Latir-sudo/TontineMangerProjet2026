import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { User, AuthResponse, UserRequest } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  
  currentUser = signal<User | null>(null);
  isLoggedIn = signal(false);

  constructor(private http: HttpClient, private router: Router) {
    this.loadStoredData();
  }

  private loadStoredData() {
    const token = localStorage.getItem('token');
    if (token && !this.isTokenExpired(token)) {
      const user = this.decodeUserFromToken(token);
      this.currentUser.set(user);
      this.isLoggedIn.set(true);
    } else {
      localStorage.removeItem('token');
    }
  }

  // Décoder l'utilisateur depuis le token JWT
  private decodeUserFromToken(token: string): User | null {
    try {
      const decoded: any = jwtDecode(token);
      // Ton token a la structure: { sub, user: {...}, iat, exp }
      const userData = decoded.user;
      
      if (!userData) {
        console.error('Token ne contient pas de user');
        return null;
      }
      
      return {
        id: userData.id,
        nom: userData.nom,
        prenom: userData.prenom,
        email: userData.email,
        telephone: userData.telephone,
        ville: userData.ville || userData.localite,
        roles: userData.roles || [],
        dateInscription: userData.dateInscription
      };
    } catch (error) {
      console.error('Erreur décodage token:', error);
      return null;
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const decoded: any = jwtDecode(token);
      const exp = decoded.exp;
      const now = Date.now() / 1000;
      return exp < now;
    } catch {
      return true;
    }
  }

  async login(email: string, password: string): Promise<{ success: boolean; message: string }> {
    try {
      const response = await firstValueFrom(
        this.http.post<AuthResponse>(`${this.apiUrl}/login`, { email, password })
      );
      
      if (response.success && response.token) {
        localStorage.setItem('token', response.token);
        const user = this.decodeUserFromToken(response.token);
        
        if (user) {
          this.currentUser.set(user);
          this.isLoggedIn.set(true);
          return { success: true, message: response.message };
        } else {
          return { success: false, message: 'Erreur lors du décodage du token' };
        }
      }
      return { success: false, message: response.message };
    } catch (error: any) {
      console.error('Login error:', error);
      return { 
        success: false, 
        message: error.error?.message || 'Email ou mot de passe incorrect' 
      };
    }
  }

  async register(userData: UserRequest): Promise<{ success: boolean; message: string }> {
    try {
      const response = await firstValueFrom(
        this.http.post<AuthResponse>(`${this.apiUrl}/register`, userData)
      );
      
      if (response.success && response.token) {
        localStorage.setItem('token', response.token);
        const user = this.decodeUserFromToken(response.token);
        
        if (user) {
          this.currentUser.set(user);
          this.isLoggedIn.set(true);
          return { success: true, message: response.message };
        } else {
          return { success: false, message: 'Erreur lors du décodage du token' };
        }
      }
      return { success: false, message: response.message };
    } catch (error: any) {
      console.error('Register error:', error);
      return { 
        success: false, 
        message: error.error?.message || 'Erreur lors de l\'inscription' 
      };
    }
  }

  logout() {
    localStorage.removeItem('token');
    this.currentUser.set(null);
    this.isLoggedIn.set(false);
    this.router.navigate(['/login']);
  }

  isAdmin(): boolean {
    const user = this.currentUser();
    return user?.roles?.includes('ADMIN') ?? false;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}