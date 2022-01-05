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
    console.log(username, password)
    return users.filter(user => user.username == username && user.password == password)
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

    const isAuthenticate = foundUser.length > 0;

    console.log(isAuthenticate)

    return {
        actorName: isAuthenticate ? foundUser.username   : "unknown",
        actorRole: isAuthenticate ? foundUser.role       : "unknown",
        isAuthenticate: isAuthenticate
    }
}