package com.lura.hey.controller;

import com.lura.hey.config.auth.PrincipalDetails;
import com.lura.hey.model.User;
import com.lura.hey.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication Authentication, @AuthenticationPrincipal OAuth2User oauth ) {

        System.out.println("===========================");
        OAuth2User oAuth2User = (OAuth2User) Authentication.getPrincipal();
        System.out.println(oAuth2User.getAttributes());
        System.out.println(oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication Authentication, @AuthenticationPrincipal PrincipalDetails userDetail ) {

        System.out.println("===========================");
        PrincipalDetails principalDetails = (PrincipalDetails) Authentication.getPrincipal();
        System.out.println(principalDetails.getUser());
        System.out.println(userDetail.getUser());

        return "세션 정보 확인하기";
    }

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails :: "+principalDetails.getUser());
        return "user";
    }
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm () {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        user.setRole("ROLE_USER");
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }
}
