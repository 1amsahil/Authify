package com.eternity.authify.controller;

import com.eternity.authify.io.ProfileRequest;
import com.eternity.authify.io.ProfileResponse;
import com.eternity.authify.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
//@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private  ProfileService profileService;

    @PostMapping("/register")
    public ProfileResponse register(@Valid @RequestBody ProfileRequest request)
    {
        return profileService.createProfile(request);
    }
}
