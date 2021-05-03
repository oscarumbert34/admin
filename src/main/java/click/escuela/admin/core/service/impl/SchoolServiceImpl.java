package click.escuela.admin.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.admin.core.mapper.Mapper;
import click.escuela.admin.core.model.School;
import click.escuela.admin.core.provider.student.api.SchoolApi;
import click.escuela.admin.core.provider.student.dto.SchoolDTO;
import click.escuela.admin.core.repository.SchoolRepository;
import click.escuela.admin.core.service.ServiceGeneric;

@Service
public class SchoolServiceImpl implements ServiceGeneric<SchoolApi,SchoolDTO>{

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public SchoolDTO getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SchoolDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(SchoolApi schoolApi) {
		School school=Mapper.mapperToSchool(schoolApi);
		schoolRepository.save(school);
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(SchoolApi entity) {
		// TODO Auto-generated method stub
		
	}



}
