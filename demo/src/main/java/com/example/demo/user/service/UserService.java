package com.example.demo.user.service;

import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    // 회원 가입 기능
    public UserEntity createServ(final UserEntity userEntity) throws RuntimeException {
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid args!");
        }

        //패스워드 인코딩
        String rawPw = userEntity.getPassword();
        userEntity.setPassword(encoder.encode(rawPw));

        boolean flag = userRepository.register(userEntity);

        return flag
                ? getByCredential(userEntity.getEmail())
                : null;
    }

    public UserEntity getByCredential(String email) {
        return userRepository.findUserByEmail(email);
    }

    // 로그인 검증 메서드
    public UserEntity validateLogin(final String email, final String password) {

        // 회원가입을 했는가?
        UserEntity user = getByCredential(email);

        if (user == null) throw new RuntimeException("가입된 회원이 아닙니다.");

        // 패스워드가 일치하는가?
        if (!encoder.matches(password, user.getPassword())) { //db에서 가져온 암호화된 비밀번호와 입력한 비밀번호 검증
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        return user; // 로그인 성공시 회원정보 리턴
    }

    //이메일 중복 체크 (step.1)
    public boolean isDuplicate(String email) {
        return userRepository.existByEmail(email);
    }

    //프로필 찾기
    public String getProfilePath(String userId) {
        String profile = userRepository.findProfile(userId);
        log.info("찾은 파일 경로 - {}", profile);
        return profile;
    }
}