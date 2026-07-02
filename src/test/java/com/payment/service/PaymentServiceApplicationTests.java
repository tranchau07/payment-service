package com.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

@SpringBootTest
class PaymentServiceApplicationTests {

	@Autowired
	com.payment.service.repository.ClientRepository clientRepository;

	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;

	@Autowired
	@Qualifier("identityDataSource")
	private DataSource identityDataSource;

	@Test
	void contextLoads() {
		System.out.println("=== CLIENTS WITH 'Chau' OR 'Tran' IN NAME ===");
		clientRepository.findAll().forEach(c -> {
			String sn = c.getShortName() != null ? c.getShortName().toLowerCase() : "";
			String fn = c.getFirstName() != null ? c.getFirstName().toLowerCase() : "";
			String ln = c.getLastName() != null ? c.getLastName().toLowerCase() : "";
			if (sn.contains("chau") || sn.contains("tran") || 
				fn.contains("chau") || fn.contains("tran") || 
				ln.contains("chau") || ln.contains("tran")) {
				System.out.println("ID: " + c.getId() + 
					" | ShortName: '" + c.getShortName() + "'" +
					" | FirstName: '" + c.getFirstName() + "'" +
					" | LastName: '" + c.getLastName() + "'" +
					" | ClientNumber: '" + c.getClientNumber() + "'" +
					" | AmndState: '" + c.getAmndState() + "'");
			}
		});
	}

	@Test
	void inspectDocTable() throws Exception {
		System.out.println("\n=== INSPECTING DOC TABLE IN ORACLE (Primary) ===");
		try (Connection conn = primaryDataSource.getConnection()) {
			DatabaseMetaData meta = conn.getMetaData();
			// In Oracle metadata, table names are usually uppercase, try "DOC"
			try (ResultSet tables = meta.getTables(null, null, "DOC", null)) {
				boolean found = false;
				while (tables.next()) {
					found = true;
					String schema = tables.getString("TABLE_SCHEM");
					String tableName = tables.getString("TABLE_NAME");
					System.out.println("Found table: " + schema + "." + tableName);
					printTableColumns(conn, schema, tableName);
					printTableData(conn, schema, tableName);
				}
				if (!found) {
					// Try lowercase "doc"
					try (ResultSet tablesLower = meta.getTables(null, null, "doc", null)) {
						while (tablesLower.next()) {
							found = true;
							String schema = tablesLower.getString("TABLE_SCHEM");
							String tableName = tablesLower.getString("TABLE_NAME");
							System.out.println("Found table: " + schema + "." + tableName);
							printTableColumns(conn, schema, tableName);
							printTableData(conn, schema, tableName);
						}
					}
				}
				if (!found) {
					System.out.println("Table 'DOC' not found in Oracle. Searching for tables matching '%DOC%'...");
					try (ResultSet tablesLike = meta.getTables(null, null, "%DOC%", null)) {
						int count = 0;
						while (tablesLike.next() && count < 30) {
							System.out.println(" - " + tablesLike.getString("TABLE_SCHEM") + "." + tablesLike.getString("TABLE_NAME"));
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error inspecting Oracle DB: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("\n=== INSPECTING DOC TABLE IN MYSQL (Identity) ===");
		try (Connection conn = identityDataSource.getConnection()) {
			DatabaseMetaData meta = conn.getMetaData();
			// Try "DOC"
			try (ResultSet tables = meta.getTables(null, null, "DOC", null)) {
				boolean found = false;
				while (tables.next()) {
					found = true;
					String schema = tables.getString("TABLE_SCHEM");
					String tableName = tables.getString("TABLE_NAME");
					System.out.println("Found table: " + schema + "." + tableName);
					printTableColumns(conn, schema, tableName);
					printTableData(conn, schema, tableName);
				}
				// Try lowercase "doc"
				if (!found) {
					try (ResultSet tablesLower = meta.getTables(null, null, "doc", null)) {
						while (tablesLower.next()) {
							found = true;
							String schema = tablesLower.getString("TABLE_SCHEM");
							String tableName = tablesLower.getString("TABLE_NAME");
							System.out.println("Found table: " + schema + "." + tableName);
							printTableColumns(conn, schema, tableName);
							printTableData(conn, schema, tableName);
						}
					}
				}
				if (!found) {
					System.out.println("Table 'DOC' not found in MySQL. Searching for tables matching '%DOC%'...");
					try (ResultSet tablesLike = meta.getTables(null, null, "%DOC%", null)) {
						int count = 0;
						while (tablesLike.next() && count < 30) {
							System.out.println(" - " + tablesLike.getString("TABLE_SCHEM") + "." + tablesLike.getString("TABLE_NAME"));
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error inspecting MySQL DB: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void printTableColumns(Connection conn, String schema, String tableName) throws Exception {
		System.out.println("Columns for " + schema + "." + tableName + ":");
		DatabaseMetaData meta = conn.getMetaData();
		try (ResultSet rs = meta.getColumns(null, schema, tableName, null)) {
			while (rs.next()) {
				System.out.println("  " + rs.getString("COLUMN_NAME") + " | " + rs.getString("TYPE_NAME") + " | Size: " + rs.getInt("COLUMN_SIZE") + " | Nullable: " + rs.getString("IS_NULLABLE"));
			}
		}
	}

	private void printTableData(Connection conn, String schema, String tableName) throws Exception {
		System.out.println("Sample Data (First 5 rows) for " + schema + "." + tableName + ":");
		String query = "SELECT * FROM " + (schema != null && !schema.trim().isEmpty() ? schema + "." : "") + tableName;
		boolean isOracle = conn.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle");
		if (isOracle) {
			query = "SELECT * FROM (" + query + ") WHERE rownum <= 5";
		} else {
			query = query + " LIMIT 5";
		}

		try (java.sql.Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			for (int i = 1; i <= colCount; i++) {
				System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			while (rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					System.out.print(rs.getObject(i) + "\t");
				}
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Could not fetch data: " + e.getMessage());
		}
	}
}

