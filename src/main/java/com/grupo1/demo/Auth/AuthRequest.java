package com.grupo1.demo.Auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String token;
    private long systemId;
}
