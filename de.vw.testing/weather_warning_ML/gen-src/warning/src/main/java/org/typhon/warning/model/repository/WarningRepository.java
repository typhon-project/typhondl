package org.typhon.warning.model.repository;

import org.typhon.warning.model.Warning;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@RepositoryRestResource(collectionResourceRel = "warning", path = "warning")
public interface WarningRepository extends PagingAndSortingRepository<Warning, Long> {
	
	@ApiOperation("Get warning by time")
	List<Warning> findByTime(@Param("time") 
								@ApiParam(value="Time")String time);

	@ApiOperation("Get warning by warning_id")
	List<Warning> findByWarning_id(@Param("warning_id") 
								@ApiParam(value="Warning_id")String warning_id);

	@ApiOperation("Get warning by warningType")
	List<Warning> findByWarningType(@Param("warningType") 
								@ApiParam(value="WarningType")String warningType);

	@ApiOperation("Get warning by severity")
	List<Warning> findBySeverity(@Param("severity") 
								@ApiParam(value="Severity")String severity);

	@ApiOperation("Get warning by area")
	List<Warning> findByArea(@Param("area") 
								@ApiParam(value="Area")String area);

}

