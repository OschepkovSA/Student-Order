package edu.java.studentorder.dao;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DBInit {
	public static void startUp() throws Exception {
		URL url_st_project = DictionaryDaoImplTest.class.getClassLoader().getResource("student_project.sql");
		List<String> str_st_project = Files.readAllLines(Paths.get(url_st_project.toURI()));
		String sql_st_project = str_st_project.stream().collect(Collectors.joining());
		
		URL url_st_data = DictionaryDaoImplTest.class.getClassLoader().getResource("student_data.sql");
		List<String> str_st_data = Files.readAllLines(Paths.get(url_st_data.toURI()));
		String sql_st_data = str_st_data.stream().collect(Collectors.joining());
		
		try (Connection con = ConnectionBuilder.getConnection();
			 Statement stmt = con.createStatement()) {
			stmt.executeUpdate(sql_st_project);
			stmt.executeUpdate(sql_st_data);
		}
	}
}
