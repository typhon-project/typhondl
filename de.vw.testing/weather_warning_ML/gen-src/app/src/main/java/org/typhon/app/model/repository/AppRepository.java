package org.typhon.app.model.repository;

import org.typhon.app.model.App;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@RepositoryRestResource(collectionResourceRel = "app", path = "app")
public interface AppRepository extends PagingAndSortingRepository<App, Long> {
	
	@ApiOperation("Get app by timeStamp")
	List<App> findByTimeStamp(@Param("timeStamp") 
								@ApiParam(value="TimeStamp")String timeStamp);

	@ApiOperation("Get app by vehicle_id")
	List<App> findByVehicle_id(@Param("vehicle_id") 
								@ApiParam(value="Vehicle_id")String vehicle_id);

	@ApiOperation("Get app by vehicle_position")
	List<App> findByVehicle_position(@Param("vehicle_position") 
								@ApiParam(value="Vehicle_position")String vehicle_position);

}

