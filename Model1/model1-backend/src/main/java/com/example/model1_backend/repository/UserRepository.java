//package com.example.model1_backend.repository;
//
//import com.example.model1_backend.dto.response.UserResponse;
//import com.example.model1_backend.entity.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.Map;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, String> {
//    boolean existsByUsername(String username);
//    Optional<User> findByUsername(String username); //tim user tu username
//    @Query(value = """
//            SELECT
//                ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum,
//                id,
//                username,
//                password,
//                first_name,
//                last_name,
//                date_of_birth,
//                email,
//                phone_number,
//                gender,
//                address,
//                active,
//                created_at,
//                updated_at
//            FROM user
//            """, nativeQuery = true)
//    Page<Map<Object, Object>> getAllUser(Pageable pageable);
//}
