package com.domhub.api.service;


import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.StaffRequest;
import com.domhub.api.dto.request.UpdateProfileRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.StaffMapper;
import com.domhub.api.model.Staff;
import com.domhub.api.repository.StaffRepository;
import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import com.domhub.api.model.Account;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.domhub.api.dto.request.AccountRequest;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final AccountService accountService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;
    private final StaffMapper staffMapper;

    public long count() {
        return staffRepository.count();
    }

    public ApiResponse<List<Staff>> getAllStaff() {
        return ApiResponse.success(staffRepository.findAll());
    }

    public ApiResponse<Staff> getStaffById(Integer id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
        return ApiResponse.success(staff);
    }

    public Staff validateAndGetStaffByAuthHeader() {
        String authHeader = request.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(authHeader);
        accountService.validateAccountExists(accountId);
        return staffRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
    }

    public ApiResponse<Staff> getStaffProfileByAccountId() {
        Staff staff = validateAndGetStaffByAuthHeader();
        return ApiResponse.success(staff);
    }

    public Integer getStaffIdByAccountId(Integer accountId) {
        return staffRepository.findByAccountId(accountId)
                .map(Staff::getId).orElseThrow(() -> new RuntimeException("Staff not found with account id " + accountId));
    }

    public void validateUniqueEmailAndPhone(String email, String phoneNumber) {
        if (email != null && !email.isBlank() && staffRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (phoneNumber != null && !phoneNumber.isBlank() && staffRepository.existsByPhoneNumber(phoneNumber)) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    public ApiResponse<Void> createStaff(StaffRequest staffRequest) {

        validateUniqueEmailAndPhone(staffRequest.getEmail(), staffRequest.getPhoneNumber());

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(staffRequest.getEmail());
        accountRequest.setPassword("123456");

        if (Objects.equals(staffRequest.getPosition(), "Bảo vệ"))
            accountRequest.setRole("STAFF");
        else accountRequest.setRole("ADMIN");

        Account account = accountService.createAccount(accountRequest);
        Staff staff = staffMapper.toEntity(staffRequest);
        staff.setAccountId(account.getId());
        return ApiResponse.success("Staff created successfully");
    }

    public ApiResponse<Void> updateStaff(Integer id, StaffRequest staffRequest) {

        Staff staff =  staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));

        if (!staffRequest.getEmail().equals(staff.getEmail())){
            if (staffRepository.existsByEmail(staffRequest.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
        }
        if (!staffRequest.getPhoneNumber().equals(staff.getPhoneNumber())){
            if (staffRepository.existsByPhoneNumber(staffRequest.getPhoneNumber())) {
                throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
            }
        }

        staff.setFullName(staffRequest.getFullName());
        staff.setGender(Staff.Gender.valueOf(staffRequest.getGender()));
        staff.setBirthday(staffRequest.getBirthday());
        staff.setAddress(staffRequest.getAddress());
        staff.setEmail(staffRequest.getEmail());
        staff.setPhoneNumber(staffRequest.getPhoneNumber());
        staff.setStartDate(staffRequest.getStartDate());
        staff.setPosition(staffRequest.getPosition());

        String username = staff.getEmail();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(username);
        accountRequest.setPassword("123456");
        if (Objects.equals(staff.getPosition(), "Bảo vệ"))
            accountRequest.setRole("STAFF");
        else accountRequest.setRole("ADMIN");

        accountService.updateAccount(accountRequest, staff.getAccountId());


        staffRepository.save(staff);
        return ApiResponse.success("Staff updated successfully");
    }


    public ApiResponse<Void> updateProfile(UpdateProfileRequest updateProfileRequest) {

        Staff staff = validateAndGetStaffByAuthHeader();

        if (!updateProfileRequest.getEmail().isBlank() && !updateProfileRequest.getEmail().equals(staff.getEmail()) &&
                staffRepository.existsByEmail(updateProfileRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        staff.setEmail(updateProfileRequest.getEmail());

        if (!updateProfileRequest.getPhoneNumber().isBlank() && !updateProfileRequest.getPhoneNumber().equals(staff.getPhoneNumber())) {
            staff.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        }

        accountService.updateUserName(staff.getAccountId(), staff.getEmail());

        staffRepository.save(staff);

        return ApiResponse.success("Profile updated successfully");
    }


    public ApiResponse<Void> deleteStaffById(Integer id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
        staffRepository.deleteById(id);
        return ApiResponse.success("Staff deleted successfully");
    }


}

