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
 * Voyage interface - matches backend GetTravelResponseDTO
 */
export interface Voyage {
  uniqueTravelId: string;
  title: string;
  description?: string;
  startDate: string;
  endDate: string;
  destination: string;
  budget?: number;
  groupId?: number;
  createdAt?: string;
  updatedAt?: string;
  companyCode?: string;
  statut?: string;
  prix?: number;
  nbParticipantsMax?: number;
  adresse?: {
    rue?: string;
    ville: string;
    codePostal?: string;
    pays?: string;
  };
  categorie?: {
    nom: string;
    description?: string;
  };
  conseiller?: {
    nom: string;
    prenom: string;
    email?: string;
    telephone?: string;
  };
  groups?: Array<{
    id?: number;
    name?: string;
    visible?: boolean;
    createdAt?: string;
    updatedAt?: string;
  }>;
  participants?: Array<{
    uniqueCustomerId: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber?: string;
    birthDate?: string;
    companyCode?: string;
  }>;
  // Legacy field for backward compatibility
  groupes?: Array<{
    id?: number;
    nom?: string;
    participants?: Array<{
      id?: number;
      nom: string;
      prenom: string;
      email?: string;
    }>;
  }>;
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
