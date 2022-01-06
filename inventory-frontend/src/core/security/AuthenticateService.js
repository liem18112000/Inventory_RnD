import {AuthenticateError} from "./AuthenticateError";
import {users} from "./DummyUsers";

const tokenIndicator = "_token";

const findUserWithUsernameAndPassword = (users, username, password) => {
    console.log(username, password)
    return users.find(user => user.username === username && user.password === password)
}

export const getAuthenticateToken = () => {
    const token = JSON.parse(sessionStorage.getItem(tokenIndicator));
    return !token ? {
        "actorName": null,
        "actorRole": null,
        "isAuthenticate": false
    } : token;
}

const storeAuthenticateToken = (token) => {
    sessionStorage.setItem(tokenIndicator, JSON.stringify(token));
}

export const authenticateService = (rawUsername, rawPassword) => {

    const username = rawUsername.trim();
    const password = rawPassword.trim();

    if(!username || username.length === 0) {
        throw AuthenticateError("username is null or blank");
    }

    if(!password || password.length === 0) {
        throw AuthenticateError("password is null or blank");
    }

    const foundUser = findUserWithUsernameAndPassword(users, username, password);
    const isAuthenticate = foundUser != null;

    if(isAuthenticate){
        storeAuthenticateToken({
            actorName: foundUser.username,
            actorRole: foundUser.role,
            isAuthenticate: isAuthenticate
        });
    }

    return isAuthenticate
}

export const logout = () => {
    sessionStorage.removeItem(tokenIndicator);
    window.location.replace("/")
}