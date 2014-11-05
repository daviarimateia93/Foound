package net.foound.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class BaseEntity implements Serializable
{
	private static final long serialVersionUID = -5700496884685155181L;
	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}
