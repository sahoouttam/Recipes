package com.example.Recipes.Persistence;

import com.example.Recipes.Business.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    
}
