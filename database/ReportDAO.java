package com.inventory.database;

import com.inventory.model.Report;
import com.inventory.model.ReportType;
import com.inventory.model.InventoryManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportDAO (Data Access Object) handles all database operations for Report entities.
 * This class provides methods for storing and retrieving generated reports.
 *
 * @author IMS Team
 * @version 2.0
 */
public class ReportDAO {

    private final Connection connection;

    /**
     * Constructs a ReportDAO with the database connection.
     */
    public ReportDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Inserts a new report into the database.
     *
     * @param reportType The type of report being generated
     * @param reportContent The content of the report
     * @return The generated report ID, or -1 if insertion failed
     */
    public int insertReport(ReportType reportType, String reportContent) {
        String sql = "INSERT INTO reports (report_type, report_content, created_at) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reportType.name());
            statement.setString(2, reportContent);
            statement.setString(3, LocalDateTime.now().toString());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting report: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Retrieves a report by its ID.
     *
     * @param reportId The unique report identifier
     * @param createdBy The manager who created the report
     * @return The Report object if found, null otherwise
     */
    public Report getReportById(int reportId, InventoryManager createdBy) {
        String sql = "SELECT * FROM reports WHERE report_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractReportFromResultSet(resultSet, createdBy);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving report: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves all reports from the database.
     *
     * @param createdBy The manager who created the reports
     * @return List of all Report objects
     */
    public List<Report> getAllReports(InventoryManager createdBy) {
        List<Report> reportList = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY created_at DESC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                reportList.add(extractReportFromResultSet(resultSet, createdBy));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all reports: " + e.getMessage());
        }

        return reportList;
    }

    /**
     * Retrieves reports by type.
     *
     * @param reportType The type of reports to retrieve
     * @param createdBy The manager who created the reports
     * @return List of Report objects of the specified type
     */
    public List<Report> getReportsByType(ReportType reportType, InventoryManager createdBy) {
        List<Report> reportList = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE report_type = ? ORDER BY created_at DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reportType.name());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                reportList.add(extractReportFromResultSet(resultSet, createdBy));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving reports by type: " + e.getMessage());
        }

        return reportList;
    }

    /**
     * Deletes a report from the database.
     *
     * @param reportId The report identifier
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM reports WHERE report_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting report: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts a Report object from a ResultSet row.
     *
     * @param resultSet The ResultSet positioned at a report row
     * @param createdBy The manager who created the report
     * @return The extracted Report object
     * @throws SQLException if database access error occurs
     */
    private Report extractReportFromResultSet(ResultSet resultSet, InventoryManager createdBy) throws SQLException {
        int reportId = resultSet.getInt("report_id");
        String reportTypeString = resultSet.getString("report_type");
        String reportContent = resultSet.getString("report_content");

        ReportType reportType = ReportType.valueOf(reportTypeString);

        return new Report(reportId, createdBy, reportType, reportContent);
    }
}
