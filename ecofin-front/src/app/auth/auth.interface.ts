export interface LoginResponse {
  operationId: string,
  expired: Date,
  tempToken: string
}

export interface TokenResponse {
  token: string,
  refreshToken: string
}
