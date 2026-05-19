@param String title
@param gg.jte.Content content


<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">

    <style>
        body {
            overflow-x: hidden;
            background-color: lightgray;

        }
        header{
            font-size: 50px;
            border-bottom: black 2px solid;

        }
        /* clickable title */
        .title, .title a{
            font-size: 40px;
            list-style: none;
            color: black;
            text-decoration: none;
        }
        .title:hover{
            transform: scale(1.05);
        }

        /* profile icon */
        .profile {
            font-size: 50px;
            text-align: right;
            justify-self: right;
            color: black;
        }

        .menu {
            font-size: 50px;
            text-align: center;
            justify-self: center;
            width: 228px;
        }
        .menu-item{
            text-decoration: none;
            justify-self: center;
            justify-content: center;
            text-align: center;
        }


        .sidebar {
            width: 250px;
            transition: all 0.3s;
            background-color: lightgray;
            color: black;
            border-right: black 2px solid;

            height: 100vh;
            overflow-y: auto;
            overflow-x:hidden;
        }
        .sidebar.collapsed {
            margin-left: -250px;
        }
        .content {
            transition: margin-left 0.3s;
            margin-left: 250px;
        }
        .content.expanded {
            margin-left: 0;
        }
        .sidebar .nav-link {
            color: black;
        }
        .sidebar .nav-link:hover {
            background-color: lightgray;
        }

        .inventory {
            margin-top: auto;
            padding: 20px;
            justify-self: center;
            text-align: center;

        }


    </style>
</head>
<body>

<header class="navbar sticky-top d-flex bg-success">
    <div class="container-fluid">
        <!-- Hamburger button -->
        <button class="btn btn-outline-dark me-2 " id="toggleSidebar">&#9776;</button>
        <div class="title">
            <img src="images/logo.png" height="40">
            <a href="home">Gift Exchange</a>
        </div>
        <div class="profile">
            <a href="profile"><i class="bi bi-person-circle text-dark" ></i></a>
        </div>
    </div>
</header>

<div id="sidebar" class="position-absolute h-100 sidebar p-3">
     <div class="menu">
          <div class="menu-item">
              <a href="/createSession" class="btn btn-primary col-lg-auto" role="button">Create Session</a>
              <a href="/viewSessions" class="btn btn-success col-lg-auto" role="button">View Sessions</a>
          </div>
    </div>
</div>

<main id="content" class="content">
    ${content}

</main>

<script>
    const sidebar = document.getElementById("sidebar");
    const content = document.getElementById("content");

    function handleResize() {
        // Collapse sidebar on small screens < 576 or small devices
        if (window.innerWidth < 576) {
            sidebar.classList.add("collapsed");
            content.classList.add("expanded");
        } else {
            sidebar.classList.remove("collapsed");
            content.classList.remove("expanded");
        }
    }

    // Run on load
    handleResize();

    // Run on window resize
    window.addEventListener("resize", handleResize);

    // Burger button toggle
    const toggleBtn = document.getElementById("toggleSidebar");
    const toggleBtnMobile = document.getElementById("toggleSidebarMobile");

    function toggleSidebar() {
        sidebar.classList.toggle("collapsed");
        content.classList.toggle("expanded");
    }

    if (toggleBtn) toggleBtn.addEventListener("click", toggleSidebar);
    if (toggleBtnMobile) toggleBtnMobile.addEventListener("click", toggleSidebar);
</script>

</body>
</html>
