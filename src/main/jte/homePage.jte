@param freddym.webportfolio.Model.User user
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Freddy's Web Portfolio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .profile {
            font-size: 50px;
            color: black;
        }
    </style>
</head>
<body>
<header>
    <div class="profile">
        <p>Welcome ${user != null ? user.getUsername() : "!"}</p>

        <i class="bi bi-person-circle">
            @if(user == null)
            <a href="login" class="btn btn-success col-lg-auto" role="button">Login</a>
             @else
            <a href="logout" class="btn btn-danger col-lg-auto" role="button">Logout</a>
            @endif
        </i>

    </div>
</header>
</body>
</html>