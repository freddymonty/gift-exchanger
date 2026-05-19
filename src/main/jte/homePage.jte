@param freddym.webportfolio.Model.User user

@template.layouts.main(
title = "Freddy's Web Portfolio",
content = @`
    <style>
        .home-main {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 80vh;
            flex-direction: column;
        }
    </style>

    <div class="home-main">
        <div class="title mt-5 mb-5">
            <h1>Gift Exchanger</h1>
        </div>

        <div class="session-btns">
            <a href="createSession" class="btn btn-primary col-lg-auto" role="button">
                Create Session
            </a>

            <a href="viewSessions" class="btn btn-success col-lg-auto" role="button">
                View Sessions
            </a>
        </div>
    </div>
`
)