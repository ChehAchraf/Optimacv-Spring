
export interface ILoginRequest {
  email: string,
  password: string,
}

export interface ILoginResponse {
  token: string,
  email: string,
  role: "ROLE_USER" | "ROLE_ADMIN" | "ROLE_COMPANY",
}

export interface IRegisterRequest {
  email: string,
  password : string,
  role: "ROLE_USER" | "ROLE_COMPANY",
}


export interface IUserProfileResponse {
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

export interface IUpdateProfileRequest {
  firstName: string;
  lastName: string;
  email: string;
}

export interface IChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}
