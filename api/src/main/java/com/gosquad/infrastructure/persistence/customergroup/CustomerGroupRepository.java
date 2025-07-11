package com.gosquad.infrastructure.persistence.customergroup;

public interface CustomerGroupRepository {
    /**
     * Ajoute un participant (customer) à un groupe dans la table de liaison.
     * @param customerId l'identifiant du participant
     * @param groupId l'identifiant du groupe
     */
    void addCustomerToGroup(int customerId, int groupId);

    /**
     * Supprime un participant (customer) d'un groupe dans la table de liaison.
     * @param customerId l'identifiant du participant
     * @param groupId l'identifiant du groupe
     */
    void removeCustomerFromGroup(int customerId, int groupId);
}
