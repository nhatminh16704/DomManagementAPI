package com.domhub.api.service;


import com.domhub.api.model.Staff;
import com.domhub.api.repository.StaffRepository;
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

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Optional<Staff> getStaffById(Integer id) {
        return staffRepository.findById(id);
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

    public void deleteStaffById(Integer id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found with id " + id));
        if (staff.getAccountId() != null) {
            accountService.deleteAccount(staff.getAccountId());
        }
        staffRepository.deleteById(id);
    }



}

