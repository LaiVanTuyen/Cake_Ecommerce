package com.codefresher.services;

import com.codefresher.dto.UserDTO;
import com.codefresher.entities.User;
import com.codefresher.entities.UserAddress;
import com.codefresher.repositories.UserAddressRepository;
import com.codefresher.repositories.UserRepository;
import com.codefresher.util.MailSenderUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MailSenderUtil mailSender;

    public void saveUser(User user, UserAddress userAddress)  {

        // set information which not in signup form
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setPoint(0);

        // type of province and district is: code-name. So have to split to get name
        userAddress.setProvince(userAddress.getProvince().split("-")[1]);
        userAddress.setDistrict(userAddress.getDistrict().split("-")[1]);
        userAddress.setReceiverName(user.getFullname());
        userAddress.setPhoneNumber(user.getPhoneNumber());
        userAddress.setUser(user);
        user.setUserAddressList(new ArrayList<UserAddress>(Arrays.asList(userAddress)));
        userRepository.saveAndFlush(user);

    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Authentication getUserLogged(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean resetPassword(String email){
        User user = userRepository.findByEmail(email);
        if (user == null)
            return false;
        String newPass = randomCharacter(6);
        String text = "<h4>Yêu cầu đặt lại mật khẩu của bạn đã được thực hiện.</h4>" +
                "<p>Mật khẩu mới của bạn là: <span style=\"color: red;\">"+newPass+"</span> <br />\n";
        String subject = "Quên mật khẩu";
        mailSender.sendMail(email, subject, text);

        newPass = passwordEncoder.encode(newPass);
        user.setPassword(newPass);
        userRepository.saveAndFlush(user);

        return true;
    }

    public User findUserById(Long id){
        return userRepository.findByUserId(id);
    }

    public boolean checkPassword(String password, Long id){
        String userPassword = userRepository.findByUserId(id).getPassword();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(password, userPassword);
    }

    public void updatePassword(UserDTO userDTO){
        String pass = passwordEncoder.encode(userDTO.getPassword());
        Long id = userDTO.getUserId();
        userRepository.updatePassword(pass, id);
    }


    public void updateUserInfor(UserDTO userDTO){
        System.out.println(userDTO);
        userRepository.setUserInforById(userDTO.getFullname(), userDTO.getPhoneNumber(), userDTO.getUserId());
    }

    public UserDTO getUserInfor(Long id){
        return mapUserToDTO(userRepository.findByUserId(id));
    }

    public Collection<? extends GrantedAuthority> getUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities();
    }
    public String getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    public User getUserByLogged(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();
        if(!username.equalsIgnoreCase("anonymousUser")){
            return userRepository.findByUsername(username);
        }
        return new User();
    }


    // Mapper
    private UserDTO mapUserToDTO(User user){
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        return userDTO;
    }
    private User mapUserToEntity(UserDTO userDTO){
        return mapper.map(userDTO, User.class);
    }
    public String randomCharacter(int length){
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        Random random = new Random();
        for (int i=0; i<length; i++){
            res += alphabet.charAt(random.nextInt(alphabet.length()));
        }
        return res;
    }
}
