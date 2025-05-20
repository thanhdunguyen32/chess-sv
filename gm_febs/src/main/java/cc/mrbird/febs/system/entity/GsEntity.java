/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

//JPA标识
@Data
@TableName("gs_list")
public class GsEntity implements Serializable {

	private Integer id;

	private Integer zone_id;

	private String name;

	private String host;

	private Integer port;

	private Boolean is_selected;
	private LocalDateTime time_open;
	// Thêm 2 field “ảo”, không map DB
	@TableField(exist = false)
	private int totalUser;
	
	@TableField(exist = false)
	private int userOnline;

	@Override
	public String toString() {
		return "GsEntity{" +
				"id=" + id +
				", zone_id=" + zone_id +
				", name='" + name + '\'' +
				", host='" + host + '\'' +
				", port=" + port +
				", is_selected=" + is_selected +
				", time_open=" + time_open +
				'}';
	}

}
