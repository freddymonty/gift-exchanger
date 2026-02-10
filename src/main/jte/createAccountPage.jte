<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <title>Register</title>
</head>
<body>
<main class="container-fluid text-center" style="background-color: #f9f9f9;">
    <div class = "mt-5 mb-5" >
        <h1>Create Account</h1>
    </div>

    <form method="post" action="/newAccount">
        Username: <input type="text" name="username"><br/><br/>

        Password: <input type="password" name="password"><br/><br/>
        <button type="submit">Create Account</button>
    </form>
</main>
</body>
</html>