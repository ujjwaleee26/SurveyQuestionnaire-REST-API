package com.springboot.restapi.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserDetailsRestRepository extends JpaRepository<UserDetails, Long>
{
   List<UserDetails> findByRole(String role);
}
