package click.escuela.admin.core.util;

import java.time.LocalDate;

import click.escuela.admin.core.provider.student.api.AdressApi;
import click.escuela.admin.core.provider.student.api.ParentApi;
import click.escuela.admin.core.provider.student.api.StudentApi;

public class StudentApiBuilder {
	private String name;
	private String surname;
	private String document;
	private String gender;
	private String grade;
	private String division;
	private String level;
	private LocalDate birthday;
	private AdressApi adressApi;
	private String cellPhone;
	private String email;
	private ParentApi parentApi;
	private Integer schoolId;
	
	public static StudentApiBuilder getBuilder() {
		return new StudentApiBuilder();
	}
	public StudentApiBuilder setName(String name) {
		this.name = name;
		return this;
	}
	public StudentApiBuilder setSurname(String surname) {
		this.surname = surname;
		return this;
	}
	public StudentApiBuilder setDocument(String document) {
		this.document = document;
		return this;
	}
	public StudentApiBuilder setGender(String gender) {
		this.gender = gender;
		return this;
	}
	public StudentApiBuilder setDivision(String division) {
		this.division = division;
		return this;
	}
	public StudentApiBuilder setGrade(String grade) {
		this.grade = grade;
		return this;
	}
	public StudentApiBuilder setLevel(String level) {
		this.level = level;
		return this;
	}
	public StudentApiBuilder setBirthday(LocalDate birthday) {
		this.birthday = birthday;
		return this;
	}
	public StudentApiBuilder setAdressApi(AdressApi adressApi) {
		this.adressApi = adressApi;
		return this;
	}
	public StudentApiBuilder setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
		return this;
	}
	public StudentApiBuilder setEmail(String email) {
		this.email = email;
		return this;
	}
	public StudentApiBuilder setParentApi(ParentApi parentApi) {
		this.parentApi = parentApi;
		return this;
	}
	public StudentApiBuilder setSchool(Integer schoolId) {
		this.schoolId = schoolId;
		return this;
	}
	
	public StudentApi getStudentApi() {
		StudentApi studentApi = new StudentApi();
		
		studentApi.setAdressApi(adressApi);
		studentApi.setBirthday(birthday);
		studentApi.setCellPhone(cellPhone);
		studentApi.setDocument(document);
		studentApi.setDivision(division);
		studentApi.setGrade(grade);
		studentApi.setLevel(level);
		studentApi.setEmail(email);
		studentApi.setGender(gender);
		studentApi.setName(name);
		studentApi.setName(surname);
		studentApi.setParentApi(parentApi);
		studentApi.setSchoolId(schoolId);
		return new StudentApi(name, surname, document, gender, birthday, adressApi, cellPhone, email, parentApi, schoolId, grade, division,level);
	}
}
