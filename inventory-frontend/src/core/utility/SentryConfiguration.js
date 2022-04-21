import sentryBrowserTracing from "./integrations/SentryBrowserTracing";
import {SENTRY_DSN, SENTRY_ENVIRONMENT, SENTRY_TRACE_SAMPLE} from "../../constant";

const sentryIntegrations = [
  sentryBrowserTracing
]

const sentryConfiguration = {
  dsn: SENTRY_DSN,
  integrations: sentryIntegrations,
  tracesSampleRate: parseFloat(SENTRY_TRACE_SAMPLE),
  environment: SENTRY_ENVIRONMENT
}

export {
  sentryIntegrations,
  sentryConfiguration
};