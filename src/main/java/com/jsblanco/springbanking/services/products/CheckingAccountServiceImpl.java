package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckingAccountServiceImpl implements CheckingAccountService {

    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;

//    public static Account createNew(AccountHolder primaryOwner, AccountHolder secondaryOwner) {
//        if (primaryOwner == null)
//            throw new IllegalArgumentException("Primary owner cannot be null");
//        if (DateUtils.getPeriodBetweenDates(primaryOwner.getBirthDay(), new Date()).getYears() < 24)
//            return new StudentCheckingAccount();
//        CheckingAccount checkingAccount = new CheckingAccount();
//        checkingAccount.setPrimaryOwner(primaryOwner);
//        checkingAccount.setSecondaryOwner(secondaryOwner);
//
//        return checkingAccount;
//    }

}
