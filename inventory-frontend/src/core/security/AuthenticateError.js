export class AuthenticateError extends Error{
    constructor(...params) {

        // Pass remaining arguments (including vendor specific ones) to parent constructor
        super(...params)

        // Maintains proper stack trace for where our error was thrown (only available on V8)
        if (Error.captureStackTrace) {
            Error.captureStackTrace(this, AuthenticateError)
        }

        // Custom attributes
        this.name = 'AuthenticateError'
        this.date = new Date()
    }
}