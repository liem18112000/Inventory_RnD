import {AuthenticateError} from "./AuthenticateError";

const users = [
    {
        username: "admin",
        password: "admin",
        role: "admin"
    },
    {
        username: "inventory",
        password: "inventory",
        role: "inventory-keeper"
    },
    {
        username: "client",
        password: "client",
        role: "client-admin"
    }
]

const findUserWithUsernameAndPassword = (users, username, password) => {
    return users.filter(user => user.username === username && user.password === password)
}

export const getToken = (user, isAuthenticate = false) => {
    return {
        actorName: isAuthenticate ? user.username   : "unknown",
        actorRole: isAuthenticate ? user.role       : "unknown",
        isAuthenticate: isAuthenticate
    }
}

export const authenticate = (rawUsername, rawPassword) => {

    const username = rawUsername.trim();

    const password = rawPassword.trim();

    if(!username || username.length === 0) {
        throw AuthenticateError("username is null or blank");
    }

    if(!password || password.length === 0) {
        throw AuthenticateError("password is null or blank");
    }

    const foundUser = findUserWithUsernameAndPassword(users, username, password);

    const isAuthenticate = !foundUser;

    return getToken(foundUser, isAuthenticate);
}