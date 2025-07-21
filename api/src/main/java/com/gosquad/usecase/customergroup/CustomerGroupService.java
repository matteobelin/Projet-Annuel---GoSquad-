package com.gosquad.usecase.customergroup;

public interface CustomerGroupService {
    /**
     * Ajoute un participant (customer) à un groupe.
     * @param customerId l'identifiant du participant
     * @param groupId l'identifiant du groupe
     */
    void addCustomerToGroup(int customerId, int groupId);

    /**
     * Supprime un participant (customer) d'un groupe.
     * @param customerId l'identifiant du participant
     * @param groupId l'identifiant du groupe
     */
    void removeCustomerFromGroup(int customerId, int groupId);
}
