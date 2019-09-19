package org.typhon.warning.model.repository;

import org.typhon.warning.model.Warning;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "warning", path = "warning")
public interface WarningRepository extends PagingAndSortingRepository<Warning, String> {

}

