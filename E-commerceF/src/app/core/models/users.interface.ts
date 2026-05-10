export interface Users {
    id:                    string;
    name:                  string;
    email:                 string;
    password:              string;
    phone:                 string;
    role:                  string;
    tokenVersion:          number;
    refreshToken:          null | string;
    refreshTokenExpiry:    Date | null;
    passwordResetCode:     null | string;
    passwordResetExpires:  Date | null;
    passwordResetVerified: boolean;
    createdAt:             Date;
    updatedAt:             Date;
}
