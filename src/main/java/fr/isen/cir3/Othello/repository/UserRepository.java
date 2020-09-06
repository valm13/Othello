package fr.isen.cir3.Othello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.isen.cir3.Othello.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
