package com.furnihub.service;

import com.furnihub.dto.UserRequest;
import com.furnihub.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface AdminUserService {

    Page<UserResponse> getAllUsers(String search, int page, int size);

    UserResponse getUserById(Integer id);

    UserResponse updateUser(Integer id, UserRequest request);

    void deleteUser(Integer id);

    void restoreUser(Integer id);

    void blockUser(Integer id);

    void unblockUser(Integer id);
}