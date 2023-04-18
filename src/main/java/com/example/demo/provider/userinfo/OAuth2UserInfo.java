package com.example.demo.provider.userinfo;


// 데이터베이스에서 저장할때 필요한 메소드들을 쭉 적어준다.
public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();

    String getEmail();
    String getName();

}
