package click.escuela.admin.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.mapper.Mapper;
import click.escuela.admin.core.model.School;
import click.escuela.admin.core.provider.student.api.SchoolApi;
import click.escuela.admin.core.provider.student.dto.SchoolDTO;
import click.escuela.admin.core.repository.SchoolRepository;
import click.escuela.admin.core.service.SchoolServiceGeneric;

@Service
public class SchoolServiceImpl implements SchoolServiceGeneric<SchoolApi,SchoolDTO>{

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public List<SchoolDTO> getAll() {
		return Mapper.mapperToSchoolsDTO(schoolRepository.findAll());
	}

	@Override
	public void create(SchoolApi schoolApi) {
		School school=Mapper.mapperToSchool(schoolApi);
		schoolRepository.save(school);
	}
}
