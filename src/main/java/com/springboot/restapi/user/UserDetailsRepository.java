package com.springboot.restapi.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>
{
   List<UserDetails> findByRole(String role);
}
