package click.escuela.admin.core.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.admin.core.model.School;
import click.escuela.admin.core.provider.student.api.SchoolApi;

@Component
public class Mapper{

	private static ModelMapper modelMapper = new ModelMapper();

	public static School mapperToSchool(SchoolApi schoolApi) {
		School school = modelMapper.map(schoolApi, School.class);
		return school;
	}

}
