package net.foound.model.service;

import java.util.List;

import net.foound.model.entity.Speciality;
import net.foound.model.repository.SpecialityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialityService
{
	@Autowired
	private SpecialityRepository specialityRepository;
	
	@Transactional(readOnly = true)
	public Speciality findOne(Long id)
	{
		return specialityRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public List<Speciality> findAll()
	{
		return specialityRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Speciality findByName(String name)
	{
		return specialityRepository.findByName(name);
	}
}
