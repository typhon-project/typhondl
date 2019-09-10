package org.typhon.textwarning.model.repository;

import org.typhon.textwarning.model.TextWarning;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "textwarning", path = "textwarning")
public interface TextWarningRepository extends PagingAndSortingRepository<TextWarning, String> {

}

