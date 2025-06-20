// Data models for the application

/**
 * Client interface.
 */
export interface Client {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  adresse: string;
}

/**
 * Voyage interface.
 */
export interface Voyage {
  id: number;
  titre: string;
  destination: string;
  date_depart: string;
  date_retour: string;
  participants: number;
  budget: number;
  client_id: number;
  statut: 'PLANIFIE' | 'EN_COURS' | 'TERMINE';
}

/**
 * Payment interface.
 */
export interface Payment {
  id: number;
  montant: number;
  date: string;
  methode: 'CARTE' | 'VIREMENT' | 'ESPECES';
  statut: 'EN_ATTENTE' | 'COMPLETE' | 'ANNULE';
  facture_id?: number;
  client_id: number;
}

export interface DocumentLine {
  id: number;
  document_id: number;
  description: string;
  quantite: number;
  prix_unitaire: number;
  montant: number;
}

export interface Document {
  id: number;
  type: 'FACTURE' | 'DEVIS';
  numero: string;
  date: string;
  montant: number;
  statut: 'BROUILLON' | 'ENVOYE' | 'PAYE' | 'ANNULE';
  client_id: number;
  voyage_id: number;
  lignes?: DocumentLine[];
}
