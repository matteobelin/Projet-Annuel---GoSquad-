// Importation des dépendances nécessaires
import { createContext, useState } from 'react';

// Création du contexte d'authentification
const AuthContext = createContext();

// Fournisseur de contexte pour l'authentification
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  // Fonction de connexion
  const login = (userData) => {
    setUser(userData);
  };

  // Fonction de déconnexion
  const logout = () => {
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook personnalisé pour utiliser le contexte d'authentification
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth doit être utilisé dans un AuthProvider');
  }
  return context;
};