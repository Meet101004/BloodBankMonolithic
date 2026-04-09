package com.example.bloodbank.service.implementation;

import com.example.bloodbank.entity.PasswordResetToken;
import com.example.bloodbank.entity.User;
import com.example.bloodbank.exception.UserNotFoundException;
import com.example.bloodbank.models.AuthReq;
import com.example.bloodbank.models.AuthResp;
import com.example.bloodbank.models.ForgotPasswordRequest;
import com.example.bloodbank.models.ResetPasswordRequest;
import com.example.bloodbank.proxy.UserProxy;
import com.example.bloodbank.repository.HospitalRepo;
import com.example.bloodbank.repository.PasswordResetTokenRepo;
import com.example.bloodbank.repository.UserRepo;
import com.example.bloodbank.service.AuthService;
import com.example.bloodbank.utils.JwtUtil;
import com.example.bloodbank.utils.MapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MapperHelper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private HospitalRepo hospitalRepo;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public AuthResp login(AuthReq authReq) {
        Optional<User> optUser = userRepo.findByEmail(authReq.getEmail());
        if (optUser.isEmpty()) {
            throw new UserNotFoundException("User not found with this email", HttpStatus.NOT_FOUND.value());
        }

        User user = optUser.get();

        // Check if currently locked
        if (user.getLockTime() != null) {
            long secondsRemaining = getLockDurationSeconds(user.getFailedAttempts());
            long secondsElapsed = java.time.Duration.between(user.getLockTime(), LocalDateTime.now()).getSeconds();
            long remaining = secondsRemaining - secondsElapsed;

            if (remaining > 0) {
                String msg = formatLockMessage(secondsRemaining);
                return AuthResp.builder()
                        .message(msg)
                        .lockedForSeconds(remaining)
                        .build();
            } else {
                // Lock expired — clear it but keep attempt count for progressive lockout
                user.setLockTime(null);
                userRepo.save(user);
            }
        }

        // Attempt authentication
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authToken);

            if (authenticate.isAuthenticated()) {
                // Success — reset counters
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepo.save(user);

                UserDetails userDetails = myUserDetailsService.loadUserByUsername(authReq.getEmail());
                String jwt = jwtUtil.generateToken(userDetails);
                return AuthResp.builder()
                        .email(authReq.getEmail())
                        .token(jwt)
                        .message("Login successful")
                        .build();
            }
        } catch (Exception e) {
            // Wrong password — increment failed attempts
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 3) {
                user.setLockTime(LocalDateTime.now());
                long lockSecs = getLockDurationSeconds(attempts);
                userRepo.save(user);
                return AuthResp.builder()
                        .message(formatLockMessage(lockSecs))
                        .lockedForSeconds(lockSecs)
                        .build();
            }

            userRepo.save(user);
            int remaining = 3 - attempts;
            return AuthResp.builder()
                    .message("Invalid credentials. " + remaining + " attempt(s) remaining before lockout.")
                    .build();
        }

        return AuthResp.builder().message("Login failed").build();
    }

    // Progressive lock durations based on attempt count
    private long getLockDurationSeconds(int attempts) {
        if (attempts >= 5) return 300; // 5 minutes
        if (attempts == 4) return 60;  // 1 minute
        return 30;                     // 30 seconds (3rd attempt)
    }

    private String formatLockMessage(long seconds) {
        if (seconds >= 300) return "Account locked for 5 minutes. Please try again later.";
        if (seconds >= 60)  return "Account locked for 1 minute. Please try again later.";
        return "Account locked for 30 seconds. Please try again later.";
    }

    @Override
    public String register(UserProxy userProxy) {
        User user = mapper.proxyToEntityUser(userProxy);
        if( userRepo.existsByEmail(userProxy.getEmail())){
            return "Email Already Exist Use Different Email";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRole().equals("HOSPITAL")){
            user.setStatus(false);
        }
        if(user.getRole().equals("DONOR")){
            user.setStatus(false);
        }
        if(user.getRole().equals("ADMIN")){
            user.setStatus(true);
        }
        userRepo.save(user);
        return "Registration Successfully Now Login!";
    }

    @Override
    public String forget(AuthReq authReq) {
        Optional<User> byEmail = userRepo.findByEmail(authReq.getEmail());
        if(byEmail.isEmpty()){
            throw new UserNotFoundException("User Not Found With this Email", HttpStatus.NOT_FOUND.value());
        }
        User user = byEmail.get();
        user.setPassword(encoder.encode(authReq.getPassword()));
        userRepo.save(user);
        return "Forget Password Successfully";
    }

    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> byEmail = userRepo.findByEmail(request.getEmail());
        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("No account found with this email", HttpStatus.NOT_FOUND.value());
        }

        // Remove any existing token for this email
        passwordResetTokenRepo.deleteByEmail(request.getEmail());

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(request.getEmail());
        resetToken.setExpiryTime(LocalDateTime.now().plusMinutes(3));
        passwordResetTokenRepo.save(resetToken);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Blood Bank - Password Reset Request");
        message.setText("Hello,\n\nClick the link below to reset your password. This link expires in 3 minutes.\n\n"
                + resetLink
                + "\n\nIf you did not request this, please ignore this email.");
        mailSender.send(message);

        return "Password reset link sent to your email";
    }

    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        Optional<PasswordResetToken> optToken = passwordResetTokenRepo.findByToken(request.getToken());

        if (optToken.isEmpty()) {
            throw new UserNotFoundException("Invalid or expired reset token", HttpStatus.BAD_REQUEST.value());
        }

        PasswordResetToken resetToken = optToken.get();

        if (resetToken.isExpired()) {
            passwordResetTokenRepo.delete(resetToken);
            throw new UserNotFoundException("Reset token has expired. Please request a new one.", HttpStatus.BAD_REQUEST.value());
        }

        Optional<User> byEmail = userRepo.findByEmail(resetToken.getEmail());
        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND.value());
        }

        User user = byEmail.get();
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepo.save(user);

        passwordResetTokenRepo.delete(resetToken);

        return "Password reset successfully";
    }
}
