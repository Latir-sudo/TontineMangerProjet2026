// models/paiement.model.ts

export enum ModePaiement {
  ORANGE_MONEY = 'ORANGE_MONEY',
  WAVE = 'WAVE',
  FREE_MONEY = 'FREE_MONEY'
}

export enum StatutCotisation {
  EN_ATTENTE = 'EN_ATTENTE',
  PARTIEL = 'PARTIEL',
  COMPLET = 'COMPLET'
}

export interface PaiementRequest {
  montant: number;
  datePaiement: string;
  modePaiement: ModePaiement;
  reference: string;
}

export interface PaiementResponse {
  id: number;
  montant: number;
  datePaiement: string;
  modePaiement: ModePaiement;
  reference: string;
  valide: boolean;
  cotisationId: number;
  cotisationStatut: string;
}

export interface PaiementHistorique {
  id: number;
  montant: number;
  datePaiement: string;
  modePaiement: ModePaiement;
  reference: string;
  valide: boolean;
  titreCotisation: string;
  cotisationId: number;
  membreNom: string;
  membrePrenom: string;
}

export interface PaiementStats {
  totalPaye: number;
  totalAttendu: number;
  paiementsReussis: number;
  paiementsEnAttente: number;
  paiementsEchoues: number;
  tauxCompletude: number;
}