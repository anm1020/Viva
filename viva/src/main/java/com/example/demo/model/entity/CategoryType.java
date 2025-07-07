package com.example.demo.model.entity;

public enum CategoryType {
    웹개발("WEB_DEV"),
    프론트엔드("FRONTEND"),
    백엔드("BACKEND"),
    게임개발("GAME_DEV"),
    데이터사이언티스트("DATA_SCIENTIST"),
    데이터엔지니어("DATA_ENGINEER"),
    데이터분석가("DATA_ANALYST"),
    앱개발("APP_DEV"),
    보안("SECURITY"),
    보안컨설팅("SECURITY_CONSULTING"),
    개발PM("PM"),
    기술지원("TECH_SUPPORT"),
    유지보수("MAINTENANCE"),
    퍼블리셔("PUBLISHER"),
    웹마스터("WEBMASTER");

    private final String label;

    CategoryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
