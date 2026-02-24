@param freddym.webportfolio.Model.User user
@param java.util.List<freddym.webportfolio.Model.Session> sessions
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>

<h1>Welcome ${user.getUsername()}</h1>

<h2>Your Gift Exchange Sessions</h2>
@if(sessions.isEmpty()){
    <p>No current sessions.</p>
    } @else
    <ul>
        @for(var session: sessions)
            ${sessions.indexOf(session) + 1}: ${session.getSessionName()}<br><br>
        @endfor
    </ul>

@endif

</body>
</html>