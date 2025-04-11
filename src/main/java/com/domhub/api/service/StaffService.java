package com.domhub.api.service;


import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.UpdateProfileRequest;
import com.domhub.api.model.Staff;
import com.domhub.api.repository.StaffRepository;
import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    public long count() {
        return staffRepository.count();
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Optional<Staff> getStaffById(Integer id) {
        return staffRepository.findById(id);
    }

    public Staff getStaffProfileByAccountId() {
        String authHeader = request.getHeader("Authorization");
        return staffRepository.findByAccountId(jwtUtil.extractAccountIdFromHeader(authHeader)).orElse(null);
    }

    public Integer getStaffIdByAccountId(Integer accountId) {
        return staffRepository.findByAccountId(accountId)
                .map(Staff::getId).orElseThrow(() -> new RuntimeException("Staff not found with account id " + accountId));
    }

    public Staff createStaff(Staff staff) {
        if (staff.getEmail() == null || staff.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }


        String username = staff.getEmail();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(username);
        accountRequest.setPassword("123456");
        if (Objects.equals(staff.getPosition(), "Bảo vệ"))
            accountRequest.setRole("STAFF");
        else accountRequest.setRole("ADMIN");

        Account account = accountService.createAccount(accountRequest);

        staff.setAccountId(account.getId());

        return staffRepository.save(staff);
    }

    public String updateStaff(Integer id, Staff updatedStaff) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isEmpty()) {
            return "Staff not found!";
        }

        if (updatedStaff.getEmail() == null || updatedStaff.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        Staff staff = optionalStaff.get();

        // Check if the new studentCode is unique before updating
        if (!updatedStaff.getEmail().equals(staff.getEmail()) &&
                staffRepository.existsByEmail(updatedStaff.getEmail())) {
            throw new RuntimeException("Staff with email " + updatedStaff.getEmail() + " already exists");
        }

        staff.setFirstName(updatedStaff.getFirstName());
        staff.setLastName(updatedStaff.getLastName());
        staff.setGender(updatedStaff.getGender());
        staff.setBirthday(updatedStaff.getBirthday());
        staff.setAddress(updatedStaff.getAddress());
        staff.setEmail(updatedStaff.getEmail());
        staff.setPhoneNumber(updatedStaff.getPhoneNumber());
        staff.setStartDate(updatedStaff.getStartDate());
        staff.setPosition(updatedStaff.getPosition());

        String username = staff.getEmail();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(username);
        accountRequest.setPassword("123456");
        if (Objects.equals(staff.getPosition(), "Bảo vệ"))
            accountRequest.setRole("STAFF");
        else accountRequest.setRole("ADMIN");

        accountService.updateAccount(accountRequest, staff.getAccountId());



        staffRepository.save(staff);
        return "Updated staff";
    }

    public String updateProfile(UpdateProfileRequest updateProfileRequest) {
        String authHeader = request.getHeader("Authorization");
        Optional<Staff> optionalStaff = staffRepository.findByAccountId(jwtUtil.extractAccountIdFromHeader(authHeader));
        if (optionalStaff.isEmpty()) {
            return "Staff not found!";
        }
        Staff staff = optionalStaff.get();
        // Check if the new studentCode is unique before updating
        if (!updateProfileRequest.getEmail().equals(staff.getEmail()) &&
                staffRepository.existsByEmail(updateProfileRequest.getEmail())) {
            throw new RuntimeException("Staff with email " + updateProfileRequest.getEmail() + " already exists");
        }
        staff.setEmail(updateProfileRequest.getEmail());
        staff.setPhoneNumber(updateProfileRequest.getPhoneNumber());

        String username = staff.getEmail();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(username);
        accountRequest.setPassword("123456");
        if (Objects.equals(staff.getPosition(), "Bảo vệ"))
            accountRequest.setRole("STAFF");
        else accountRequest.setRole("ADMIN");
        accountService.updateAccount(accountRequest, staff.getAccountId());

        staffRepository.save(staff);
        return "Updated staff";
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        String authHeader = request.getHeader("Authorization");
        return accountService.changePassword(jwtUtil.extractAccountIdFromHeader(authHeader), changePasswordRequest);
    }

    public void deleteStaffById(Integer id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found with id " + id));
        if (staff.getAccountId() != null) {
            accountService.deleteAccount(staff.getAccountId());
        }
        staffRepository.deleteById(id);
    }



}

