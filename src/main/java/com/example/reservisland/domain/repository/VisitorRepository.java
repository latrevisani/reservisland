package com.example.reservisland.domain.repository;

import com.example.reservisland.domain.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor findByEmailOrFullName(String email, String fullName);

}
