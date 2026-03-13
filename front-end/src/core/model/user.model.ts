
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
