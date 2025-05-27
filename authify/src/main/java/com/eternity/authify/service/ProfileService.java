package com.eternity.authify.service;

import com.eternity.authify.entity.UserEntity;
import com.eternity.authify.io.ProfileRequest;
import com.eternity.authify.io.ProfileResponse;
import com.eternity.authify.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ProfileResponse createProfile(ProfileRequest request)
    {
        UserEntity newProfile = convertToUserEntity(request);

        if(!userRepo.existsByEmail(request.getEmail())) {
            newProfile = userRepo.save(newProfile);
            return convertToProfileResponse(newProfile);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email Already Exists");
        }
    }

    private UserEntity convertToUserEntity(ProfileRequest request )
    {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .build();
    }

    private ProfileResponse convertToProfileResponse(UserEntity newProfile)
    {
        return ProfileResponse
                .builder()
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .userId(newProfile.getUserId())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }
}
