package com.domhub.api.service;


import com.domhub.api.model.Staff;
import com.domhub.api.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.model.Account;
import java.util.List;
import java.util.Optional;
import com.domhub.api.dto.request.AccountRequest;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final AccountService accountService;

    public long count() {
        return staffRepository.count();
    }

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
        accountRequest.setRole("STAFF");

        Account account = accountService.createAccount(accountRequest);

        staff.setAccountId(account.getId());

        return staffRepository.save(staff);
    }




}

