export interface User {
  id: string,
  phone: string,
  userInfo: UserInfo
}

export interface UserInfo {
  lastName: string,
  firstName: string,
  middleName: string,
  birthDay: Date,
}
