package com.codefresher.validator;

import com.codefresher.entities.User;
import com.codefresher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {
    @Autowired
    UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;
        if(userService.findUserByUsername(user.getUsername()) != null){
            errors.rejectValue("username", "Duplicate.user.username", "Tên tài khoản đã được sử dụng");
        }
    }
}
