import * as Sentry from "@sentry/react";
import {sleep} from "../ComponentUtility";

export const handleExceptionWithSentry = (exception) => {
    console.log(exception)
    Sentry.captureException(exception);
}

export const handleExceptionWithSentryAndSendFeedback = (exception, title, message) => {
    console.log(exception)
    Sentry.captureException(exception);
    sleep(3000).then(() => Sentry.showReportDialog({
        title: title ? title : "It looks like we’re having issues.",
        subtitle: message ? message : "Inventory team has been notified."
    }));
}