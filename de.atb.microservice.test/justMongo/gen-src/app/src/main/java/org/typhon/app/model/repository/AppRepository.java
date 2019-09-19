package org.typhon.app.model.repository;

import org.typhon.app.model.App;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "app", path = "app")
public interface AppRepository extends PagingAndSortingRepository<App, String> {

}

