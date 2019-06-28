package com.bezbednost.service;

import com.bezbednost.model.Authority;

public interface AuthorityServiceInterface {
    Authority findByName(String name);

}