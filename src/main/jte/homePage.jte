@param freddym.webportfolio.Model.User user

@template.layouts.main(
title = "Freddy's Web Portfolio",
content = @`
    <style>
        .home-main {
            min-height: 80vh;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
            flex-direction: column;
        }

        .home-card {
            background: white;
            padding: 3rem;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            max-width: 600px;
        }
    </style>

    <div class="home-main">
        <div class="home-card">
            <h1>Gift Exchanger</h1>

            @if(user != null)
                <p class="lead">Welcome back, ${user.getUsername()}!</p>
                <p>Use the sidebar to create a new gift exchange session or view your existing sessions.</p>
            @else
                <p class="lead">Welcome!</p>
                <p>Log in to create and manage your gift exchange sessions.</p>

                <a href="/login" class="btn btn-success mt-3" role="button">
                    Login
                </a>
            @endif
        </div>
    </div>
`
)