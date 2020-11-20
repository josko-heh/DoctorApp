package database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import main.Main;
import source.entity.Patient;
import source.entity.Sex;

public class Database {

    private static final String PROPERTIES_PATH = "src\\database\\database.properties";

    private static Connection getConnection() throws SQLException, IOException {
        Properties prop = new Properties();
        prop.load(new FileReader(PROPERTIES_PATH));

        String dbUrl = prop.getProperty("dbUrl");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");


		Connection conn = DriverManager.getConnection(dbUrl, username, password);

		return conn;
    }
    
	
	public static void savePatient(Patient patient) throws Exception{
		try(Connection connection = getConnection()){
			
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO patient(name, surname, jmbg, DOB, massKg, sex_id, doctor_id)" + " VALUES (?, ?, ?, ?, ?, ?, ?);"); 

			preparedStatement.setString(1, patient.getName());
			preparedStatement.setString(2, patient.getSurname());
			preparedStatement.setString(3, patient.getJmbg());
			preparedStatement.setString(4, patient.getDOBformatted());
			preparedStatement.setInt(5, patient.getMassKg());
			preparedStatement.setInt(6, patient.getSexId());
			preparedStatement.setInt(7, Main.getCurrentDoctorId());
			
			preparedStatement.executeUpdate();

			preparedStatement.close();

		}catch(SQLException | IOException ex) {
			throw ex;
		}
	}
	
	public static List<Sex> getAllSexes() throws Exception{
		try (Connection connection = getConnection()) {
			List<Sex> sexes = new ArrayList<>();

			Statement query = connection.createStatement();

			ResultSet resultSet = query.executeQuery("SELECT * FROM sex");

			while (resultSet.next()) {
				Integer id = resultSet.getInt("id");
				String gender = resultSet.getString("gender");

				sexes.add(new Sex(id, gender));
			}
			
			resultSet.close();
			query.close();
			
			return sexes;
			
		}catch (SQLException | IOException ex) {
			throw ex;
		}
	}

	public static Patient getPatient(Integer id) throws Exception{
		try (Connection connection = getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM patient WHERE id = ?");
			preparedStatement.setInt(1, id);

			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				String name = resultSet.getString("name");
				String surname = resultSet.getString("surname");
				String jmbg = resultSet.getString("jmbg");
				LocalDate sqlDate = resultSet.getDate("DOB").toLocalDate();
				Integer massKg = resultSet.getInt("massKg");
				Integer sexId = resultSet.getInt("sex_id");

				resultSet.close();
				preparedStatement.close();

				return new Patient(id, name, surname, jmbg, sqlDate, massKg, sexId);
			} else{ 
				resultSet.close();
				preparedStatement.close();
				throw new Exception("Patient id not found in database");
			}
		} catch (SQLException | IOException ex) {
			throw ex;
		}
	}

	public static List<Patient> getAllPatients() throws Exception{
		try (Connection connection = getConnection()) {
			List<Patient> patients = new ArrayList<>();

			Statement query = connection.createStatement();

			ResultSet resultSet = query.executeQuery("SELECT * FROM patient");

			while (resultSet.next()) {
				Integer id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String surname = resultSet.getString("surname");
				String jmbg = resultSet.getString("jmbg");
				LocalDate sqlDate = resultSet.getDate("DOB").toLocalDate();
				Integer massKg = resultSet.getInt("massKg");
				Integer sexId = resultSet.getInt("sex_id");

				patients.add(new Patient(id, name, surname, jmbg, sqlDate, massKg, sexId));
			}
			
			resultSet.close();
			query.close();
			
			return patients;
			
		}catch (SQLException | IOException ex) {
			throw ex;
		}
	}

}
