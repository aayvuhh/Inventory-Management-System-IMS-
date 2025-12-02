package com.inventory.model;

/**
 * OrderStatus enum represents the different states a purchase order can be in
 * throughout its lifecycle from creation to completion or cancellation.
 *
 * @author IMS Team
 * @version 2.0
 */
public enum OrderStatus {
    /** Order has been created but not yet sent */
    CREATED,

    /** Order has been sent to the supplier */
    SENT_TO_SUPPLIER,

    /** Order has been received from the supplier */
    RECEIVED,

    /** Order has been cancelled */
    CANCELLED
}
