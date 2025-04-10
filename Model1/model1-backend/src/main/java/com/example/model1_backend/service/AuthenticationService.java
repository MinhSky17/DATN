package com.example.model1_backend.service;

import com.example.model1_backend.dto.request.AuthenticationRequest;
import com.example.model1_backend.dto.request.IntrospectRequest;
import com.example.model1_backend.dto.response.AuthenticationResponse;
import com.example.model1_backend.dto.response.IntrospectResponse;
import com.example.model1_backend.exception.AppException;
import com.example.model1_backend.exception.ErrorCode;
import com.example.model1_backend.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")   //Lay tu file yaml
    protected String SIGNER_KEY;
//    protected static final String SIGNER_KEY = "qbxl1DSfXKPUzYBGjAUdIUbMLeFaa6vbdAgGzGTkLv+y06VlUVuMpTM/mLA6SHlK";

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime(); // lay ra time cua token de kiem tra xem da het han chua

        var verified = signedJWT.verify(verifier); // tra ve true/false

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    // de tao token them thu vien nimbus trong pom
    // Ham gen token
    String generateToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);  // noi dung thuat toan su dung

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()  // body: noi dung gui di trong token (payload)
                .subject(username)
                .issuer("minhsky.com")  // xac dinh token issue tu ai (thuong la domain)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()  // token het han trong 1h
                ))
                .claim("customClaim", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        // da build xong h ki token  (khoa de ki va khoa giai ma trung nhau)
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();   // kieu string
        } catch (JOSEException exception){
            log.error("Cannot create token", exception);
            throw new RuntimeException(exception);
        }
    }
}
