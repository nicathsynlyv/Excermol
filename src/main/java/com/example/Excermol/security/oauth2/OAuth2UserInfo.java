package com.example.Excermol.security.oauth2;

import java.util.Map;
//Bunun məqsədi: Google-dan gələn "xam" JSON məlumatını (Map<String, Object>)
//rahat oxuna bilən bir formaya çeviririk
//qiscasi:u OAuth2UserInfo class-ının əsas məqsədi Google, GitHub, Facebook və s.
// OAuth2 provider-lərdən gələn user məlumatlarını vahid formada idarə etməkdir.
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

//    Hər OAuth2 provider üçün id, name və email məlumatını necə tapacağını sən özün müəyyənləşdir
    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
}