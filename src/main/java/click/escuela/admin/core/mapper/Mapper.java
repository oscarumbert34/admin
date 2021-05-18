package click.escuela.admin.core.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.admin.core.model.School;
import click.escuela.admin.core.provider.student.api.SchoolApi;
import click.escuela.admin.core.provider.student.dto.SchoolDTO;

@Component
public class Mapper {

	private Mapper() {
		
	}

	private static ModelMapper modelMapper = new ModelMapper();

	public static School mapperToSchool(SchoolApi schoolApi) {
		return modelMapper.map(schoolApi, School.class);
	}

	public static List<SchoolDTO> mapperToSchoolsDTO(List<School> schools) {
		List<SchoolDTO> schoolDTOList = new ArrayList<>();
		schools.stream().forEach(p -> schoolDTOList.add(mapperToSchool(p)));
		return schoolDTOList;
	}

	private static SchoolDTO mapperToSchool(School school) {
		return modelMapper.map(school, SchoolDTO.class);
	}

}
