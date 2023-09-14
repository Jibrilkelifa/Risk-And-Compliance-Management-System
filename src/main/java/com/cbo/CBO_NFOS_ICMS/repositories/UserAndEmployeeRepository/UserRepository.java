package com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
//    public User findByUsername(String user);
    Boolean existsByUsername(String username);

   // Boolean existsByEmail(String email);
    Optional<User> findUserById(Long id);




//    @Query("SELECT u FROM User u WHERE u.email = ?1")
//    public User findByEmail(String email);

    void deleteUserById(Long id);

  //  List<User> findUsersByRolesId(Long roleId);



    @Query("SELECT u FROM User u WHERE u.username = ?1")
    public User findByUserName(String username);

    Optional<Object> findByUsername(String username);
}
