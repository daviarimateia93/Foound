package net.foound.model.service;

import net.foound.model.entity.Status;
import net.foound.model.repository.StatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatusService
{
	@Autowired
	private StatusRepository statusRepository;
	
	@Transactional(readOnly = true)
	public Status findOne(Long id)
	{
		return statusRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public Status findByName(String name)
	{
		return statusRepository.findByName(name);
	}
}
