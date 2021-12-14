function fn() {

    // Get config
    var env = karate.env;
    karate.log('karate.env system property was:', env);

    // Set-up config
    var config = {
        env: !env ? "dev" : env,
        baseUrl : "http://localhost:8080/ingredient",
        timeOutDuration : 5000
    };

    // Switch between environment configs
    if (env === 'dev') { // Local dev environment
        config.timeOutDuration = 30000;
    } else if (env === 'prod') { // Production prod environment
        config.baseUrl = '/ingredient';
    } // Can add multiple env type in here

    // Timeouts
    karate.configure('connectTimeout', config.timeOutDuration);
    karate.configure('readTimeout', config.timeOutDuration);

    return config;
}