package source.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Patient {
    private final Integer id;
    private String name;
    private String surname;
    private String jmbg;
    private String DOBformatted; //dd.MM.yyyy.
    private Integer massKg;
    private Integer sexId;
    private String weightGroup = null;

    /**
     * @param DOB converted to string in dd.MM.yyyy. format and stored in <code>this.DOBformatted</code>
     */
    public Patient(Integer id, String name, String surname, String jmbg, LocalDate DOB, Integer massKg, Integer sexId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.jmbg = jmbg;
        this.DOBformatted = DOB.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
        this.massKg = massKg;
        this.sexId = sexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public Integer getSexId() {
        return sexId;
    }

    public void setSexId(Integer sexId) {
        this.sexId = sexId;
    }

    public String getDOBformatted() {
        return DOBformatted;
    }

    public void setDOBformatted(String dOBformatted) {
        DOBformatted = dOBformatted;
    }

    public Integer getId() {
        return id;
    }

    public Integer getMassKg() {
        return massKg;
    }

    public void setMassKg(Integer massKg) {
        this.massKg = massKg;
    }

    public String getWeightGroup() {
        return weightGroup;
    }

    public void setWeightGroup(String weightGroup) {
        this.weightGroup = weightGroup;
    }


    public static void setWeightGroup(List<Patient> patients){
        patients.forEach(pat -> {
            if (pat.getMassKg() < 70) pat.setWeightGroup("Underweight");
            else if (pat.getMassKg() < 90) pat.setWeightGroup("Normal weight");
            else if (pat.getMassKg() < 120) pat.setWeightGroup("Overweight");
            else pat.setWeightGroup("Obese");
        });
    }


    @Override
    public String toString() {
        String weightWarning = "";
        if (this.weightGroup == "Underweight") weightWarning = " - Underweight!";
        else if (this.weightGroup == "Overweight") weightWarning = " - Overweight!";
        else if (this.weightGroup == "Obese") weightWarning = " - Obese!";

        return this.name + " " + this.surname +  " - " + this.DOBformatted + weightWarning;
    }

    @Override
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
        if (getClass() != obj.getClass())
            return false;

        Patient other = (Patient) obj;

        if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
            return false;
            
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
            return false;
            
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
            return false;
            
		if (jmbg == null) {
			if (other.jmbg != null)
				return false;
		} else if (!jmbg.equals(other.jmbg))
            return false;
            
		if (DOBformatted == null) {
			if (other.DOBformatted != null)
				return false;
		} else if (!DOBformatted.equals(other.DOBformatted))
            return false;
            
		if (massKg == null) {
			if (other.massKg != null)
				return false;
		} else if (!massKg.equals(other.massKg))
            return false;
            
		if (sexId == null) {
			if (other.sexId != null)
				return false;
		} else if (!sexId.equals(other.sexId))
            return false;
    
        return true;
    }
}
