// services/paiement.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  PaiementRequest, 
  PaiementResponse, 
  PaiementHistorique, 
  PaiementStats 
} from '../models/cotisation.model';

@Injectable({ providedIn: 'root' })
export class PaiementService {
  private apiUrl = 'http://localhost:8080/api/paiements';

  constructor(private http: HttpClient) { }

  getHistoriqueByMembre(membreId: number): Observable<PaiementHistorique[]> {
    return this.http.get<PaiementHistorique[]>(`${this.apiUrl}/membre/${membreId}/historique`);
  }

  getHistoriqueByTontine(tontineId: number): Observable<PaiementHistorique[]> {
    return this.http.get<PaiementHistorique[]>(`${this.apiUrl}/tontine/${tontineId}/historique`);
  }

  getPaiementsByCotisation(cotisationId: number): Observable<PaiementResponse[]> {
    return this.http.get<PaiementResponse[]>(`${this.apiUrl}/cotisation/${cotisationId}`);
  }

  createPaiement(cotisationId: number, request: PaiementRequest): Observable<PaiementResponse> {
    return this.http.post<PaiementResponse>(`${this.apiUrl}/cotisation/${cotisationId}`, request);
  }

  validerPaiement(paiementId: number): Observable<PaiementResponse> {
    return this.http.put<PaiementResponse>(`${this.apiUrl}/${paiementId}/valider`, {});
  }

  rejeterPaiement(paiementId: number): Observable<PaiementResponse> {
    return this.http.put<PaiementResponse>(`${this.apiUrl}/${paiementId}/rejeter`, {});
  }

  getStatsByMembre(membreId: number): Observable<PaiementStats> {
    return this.http.get<PaiementStats>(`${this.apiUrl}/membre/${membreId}/stats`);
  }
}