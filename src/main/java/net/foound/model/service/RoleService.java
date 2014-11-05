package net.foound.model.service;

import net.foound.model.entity.Role;
import net.foound.model.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService
{	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Role findOne(Long id)
	{
		return roleRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public Role findByName(String name)
	{
		return roleRepository.findByName(name);
	}
}
