package com.example.reservisland.business.service;

import com.example.reservisland.business.exception.InvalidVisitorException;
import com.example.reservisland.domain.entity.Visitor;
import com.example.reservisland.domain.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    public Visitor getOrCreateVisitor(String email, String fullName) {
        var visitor = visitorRepository.findByEmailOrFullName(email, fullName);

        if (isNull(visitor)) {
            return visitorRepository.save(Visitor.builder().email(email).fullName(fullName).build());
        }

        if (!email.equals(visitor.getEmail()) || !fullName.equals(visitor.getFullName())) {
            throw new InvalidVisitorException("Visitor is sending a different registered email or full name.");
        }

        return visitor;
    }
}
