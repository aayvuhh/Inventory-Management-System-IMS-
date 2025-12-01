package com.inventory.model;

/**
 * ReportType enum defines the different types of reports available in the IMS.
 * Each type represents a specific category of inventory or operational report.
 *
 * @author IMS Team
 * @version 2.0
 */
public enum ReportType {
    /** Summary report of all stock levels and products */
    STOCK_SUMMARY,

    /** Report showing products below their reorder level */
    LOW_STOCK,

    /** Summary of sales transactions over a period */
    SALES_SUMMARY,

    /** Report of all purchase orders and their status */
    PURCHASE_ORDERS
}
