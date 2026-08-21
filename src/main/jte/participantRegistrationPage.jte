@param freddym.webportfolio.Model.Session session

@template.layouts.main(
title = "Join Gift Exchange",
content = @`
    <style>
        .registration-main {
            min-height: 80vh;
            padding: 3rem;
            background-color: #F5F1E8;
            color: #3E2723;
        }

        .registration-card {
            width: 100%;
            max-width: 700px;
            margin: 0 auto;
            background-color: #fffaf0;
            border: 2px solid #8B7E47;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
        }

        .form-control {
            border: 1px solid #8B7E47;
        }

        .form-control:focus {
            border-color: #C9A227;
            box-shadow: 0 0 0 0.25rem rgba(201, 162, 39, 0.25);
        }

        .sms-consent {
            margin-top: 1.5rem;
            padding: 1.25rem;
            background-color: #F8F4E8;
            border: 1px solid #D4C89A;
            border-radius: 10px;
        }

        .sms-disclaimer {
            font-size: 0.9rem;
            margin-top: 0.75rem;
            color: #5D4037;
        }

        .policy-links {
            margin-top: 0.75rem;
        }

        .policy-links a {
            color: #2D6A4F;
            margin-right: 15px;
        }

        .btn-register {
            margin-top: 1.5rem;
            background-color: #2D6A4F;
            border: none;
        }

        .btn-register:hover {
            background-color: #1B4332;
        }

        .session-name {
            color: #2D6A4F;
        }
    </style>

    <div class="registration-main">

        <div class="registration-card">

            <h1>Join Gift Exchange</h1>

            <h3 class="session-name">
                ${session.getSessionName()}
            </h3>

            <p>
                Enter your information below to join this gift exchange.
            </p>

            <form method="post"
                  action="/registerParticipant/${session.getId()}">

                <div class="mb-3">

                    <label class="form-label">
                        Your Name
                    </label>

                    <input class="form-control"
                           type="text"
                           name="name"
                           placeholder="Your name"
                           required>

                </div>

                <div class="mb-3">

                    <label class="form-label">
                        Mobile Phone Number
                    </label>

                    <input class="form-control"
                           type="tel"
                           name="phoneNumber"
                           placeholder="+1 555 123 4567">

                    <small class="text-muted">
                        Optional. Provide your mobile number if you would
                        like to receive your gift exchange assignment by SMS.
                    </small>

                </div>

                <div class="sms-consent">

                    <div class="form-check">

                        <input class="form-check-input"
                               type="checkbox"
                               id="smsConsent"
                               name="smsConsent"
                               value="true">

                        <label class="form-check-label"
                               for="smsConsent">

                            I agree to receive SMS gift exchange
                            notifications from Freddy's Gift Exchange.

                        </label>

                    </div>

                    <div class="sms-disclaimer">

                        By providing your phone number and checking this box,
                        you agree to receive SMS gift exchange notifications
                        from Freddy's Gift Exchange.

                        Message frequency may vary.
                        Standard message and data rates may apply.

                        Reply STOP to opt out.
                        Reply HELP for help.

                        Your mobile information will not be sold or shared
                        with third parties for promotional or marketing
                        purposes.

                    </div>

                    <div class="policy-links">

                        <a href="/privacy"
                           target="_blank">
                            Privacy Policy
                        </a>

                        <a href="/terms"
                           target="_blank">
                            Terms of Service
                        </a>

                    </div>

                </div>

                <button type="submit"
                        class="btn btn-register text-light">
                    Join Gift Exchange
                </button>

            </form>

        </div>

    </div>
`
)