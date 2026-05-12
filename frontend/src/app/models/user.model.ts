export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  password: string;
  role: 'USER' | 'ADMIN';
  statut: 'VERIFIE' | 'NON_VERIFIE';
  localite: string;
  dateInscription: string;
}