package com.example.hospital.repository;

import com.example.hospital.model.entity.User;
import com.example.hospital.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    // Tìm theo username
    Optional<User> findByUserName(String userName);

    // Tìm theo email (để kiểm tra trùng khi tạo mới)
    Optional<User> findByEmail(String email);

    // Tìm theo id và status (để xóa mềm - chỉ xóa khi đang ACTIVE)
    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    // Tìm theo username và status (chỉ lấy user đang ACTIVE)
    Optional<User> findByUserNameAndStatus(String userName, UserStatus status);

    // Tìm tất cả theo status + phân trang
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    // Tìm kiếm theo tên (chứa keyword) + phân trang
    @Query("SELECT u FROM User u WHERE u.userName LIKE %:keyword%")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm kiếm theo tên + chỉ lấy ACTIVE
    @Query("SELECT u FROM User u WHERE u.userName LIKE %:keyword% AND u.status = :status")
    Page<User> searchByKeywordAndStatus(@Param("keyword") String keyword,
            @Param("status") UserStatus status, Pageable pageable);
}
