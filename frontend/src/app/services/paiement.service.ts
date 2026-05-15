// services/paiement.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  PaiementRequest, 
  PaiementResponse, 
  PaiementHistorique, 
  PaiementStats 
} from '../models/paiement.model';

@Injectable({
  providedIn: 'root'
})
export class PaiementService {
  private apiUrl = 'http://localhost:8080/api/paiements';

  constructor(private http: HttpClient) { }

  /**
   * Récupère l'historique des paiements d'un membre
   */
  getHistoriqueByMembre(membreId: number): Observable<PaiementHistorique[]> {
    return this.http.get<PaiementHistorique[]>(`${this.apiUrl}/membre/${membreId}/historique`);
  }

  /**
   * Récupère l'historique des paiements d'une tontine
   */
  getHistoriqueByTontine(tontineId: number): Observable<PaiementHistorique[]> {
    return this.http.get<PaiementHistorique[]>(`${this.apiUrl}/tontine/${tontineId}/historique`);
  }

  /**
   * Récupère tous les paiements d'une cotisation
   */
  getPaiementsByCotisation(cotisationId: number): Observable<PaiementResponse[]> {
    return this.http.get<PaiementResponse[]>(`${this.apiUrl}/cotisation/${cotisationId}`);
  }

  /**
   * Crée un nouveau paiement
   */
  createPaiement(cotisationId: number, request: PaiementRequest): Observable<PaiementResponse> {
    return this.http.post<PaiementResponse>(`${this.apiUrl}/cotisation/${cotisationId}`, request);
  }

  /**
   * Valide un paiement (admin uniquement)
   */
  validerPaiement(paiementId: number): Observable<PaiementResponse> {
    return this.http.put<PaiementResponse>(`${this.apiUrl}/${paiementId}/valider`, {});
  }

  /**
   * Rejette un paiement (admin uniquement)
   */
  rejeterPaiement(paiementId: number): Observable<PaiementResponse> {
    return this.http.put<PaiementResponse>(`${this.apiUrl}/${paiementId}/rejeter`, {});
  }

  /**
   * Récupère les statistiques des paiements d'un membre
   */
  getStatsByMembre(membreId: number): Observable<PaiementStats> {
    return this.http.get<PaiementStats>(`${this.apiUrl}/membre/${membreId}/stats`);
  }
}