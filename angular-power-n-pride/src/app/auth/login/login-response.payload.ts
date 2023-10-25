export interface LoginResponse {
  access_token: string;
  refresh_token: string;
  expiresAt: Date;
  username: string;

  //TO-DO
  /*
  authenticationToken: string;
    refreshToken: string;
    expiresAt: Date;
    username: string;
    */
}
