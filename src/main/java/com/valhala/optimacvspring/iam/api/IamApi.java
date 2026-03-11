package com.valhala.optimacvspring.iam.api;

import java.util.UUID;


public interface IamApi {

    UUID findUserIdByEmail(String email);
}
