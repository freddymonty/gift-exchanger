@param freddym.webportfolio.Model.User user
@param java.util.List<freddym.webportfolio.Model.Session> sessions

@template.layouts.main(
title = "Freddy's Web Portfolio",
content = @`
    <style>
        .viewSessions-main {
            min-height: 80vh;
            padding: 3rem;
            background-color: #F5F1E8;
            color: #3E2723;
        }

        .sessions-card {
            width: 100%;
            max-width: 700px;
            margin: 0 auto;
            background-color: #fffaf0;
            border: 2px solid #8B7E47;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
        }

        .sessions-list {
            list-style: none;
            padding-left: 0;
            margin-top: 1.5rem;
        }

        .session-item {
            padding: 0.75rem 1rem;
            margin-bottom: 0.75rem;
            background-color: #2D6A4F;
            color: #F5F1E8;
            border-radius: 10px;
            border-left: 6px solid #C9A227;
        }

        .empty-message {
            margin-top: 1rem;
            font-style: italic;
        }
    </style>

    <div class="viewSessions-main">
        <div class="sessions-card">
            <h1>Welcome ${user.getUsername()}</h1>

            <h2>Your Gift Exchange Sessions</h2>

            @if(sessions.isEmpty())
                <p class="empty-message">No current sessions.</p>
            @else
                <ul class="sessions-list">
                    !{ int count = 1; }

                    @for(var session : sessions)
                        <li class="session-item">
                            @if(session.getSessionName() == null || session.getSessionName().isBlank())
                                ${count}: No name ${count}
                            @else
                                ${count}: ${session.getSessionName()}
                            @endif
                        </li>

                        !{ count++; }
                    @endfor
                </ul>
            @endif
        </div>
    </div>
`
)