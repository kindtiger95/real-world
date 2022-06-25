export interface UsersScheme {
    email: string;
    token: string;
    username: string;
    bio: string;
    image: string;
}

export interface JwtPayload {
    user_uid: number;
}

export interface AuthInfo {
    user_uid: number;
    token: string;
}
