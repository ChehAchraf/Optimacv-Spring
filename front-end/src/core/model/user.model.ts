
export interface ILoginRequest {
  email : string,
  password : string,
}

export interface ILoginResponse {
  token: string;
  email: string;
  role: "ROLE_USER" | "ROLE_ADMIN" | "ROLE_COMPANY";
}
