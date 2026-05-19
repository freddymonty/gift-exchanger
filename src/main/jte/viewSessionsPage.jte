@param freddym.webportfolio.Model.User user
@param java.util.List<freddym.webportfolio.Model.Session> sessions


@template.layouts.main(
title = "Freddy's Web Portfolio",
content = @`
    <style>
        .viewSessions-main {
            display: flex;
            justify-content: center;
            align-items: start;
            height: 80vh;
            flex-direction: column;
            background-color: lightgray;
        }
    </style>
<div class="viewSessions-main">
<h1>Welcome ${user.getUsername()}</h1>

<h2>Your Gift Exchange Sessions</h2>

    !{ int count = 1;}
@if(sessions.isEmpty()){
    <p>No current sessions.</p>
    } @else
    <ul>

        @for(var session: sessions)

            @if(session.getSessionName().isEmpty())
                <p>${sessions.indexOf(session) + 1}: No name ${count}</p>
                !{ count++; }
            @else
            ${sessions.indexOf(session) + 1}: ${session.getSessionName()}<br><br>
                @endif
        @endfor
    </ul>

@endif

    </div>


`
)