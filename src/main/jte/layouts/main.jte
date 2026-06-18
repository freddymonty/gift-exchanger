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
            background-color: #F5F1E8;
            color: #3E2723;
        }

        header {
            font-size: 50px;
            border-bottom: #8B7E47 2px solid;
        }

        .title,
        .title a {
            font-size: 40px;
            list-style: none;
            color: #F5F1E8;
            text-decoration: none;
        }

        .profile {
            font-size: 50px;
            text-align: right;
            justify-self: right;
            color: #F5F1E8;
        }

        .sidebar {
            width: 250px;
            transition: transform 0.3s ease;
            background-color: #2D6A4F;
            color: #F5F1E8;
            border-right: #8B7E47 2px solid;
            position: fixed;
            top: 72px;
            left: 0;
            height: calc(100vh - 64px);
            overflow-y: auto;
            overflow-x: hidden;
            z-index: 1000;
        }

        .sidebar.collapsed {
            transform: translateX(-250px);
        }

        .sidebar .nav-link {
            color: #F5F1E8;
        }

        .sidebar .nav-link:hover {
            background-color: #52796F;
        }

        .content {
            transition: margin-left 0.3s ease;
            margin-left: 250px;
        }

        .content.expanded {
            margin-left: 0;
        }
        .menu-item .btn-primary {
            background-color: #C9A227;
            border-color: #A8871E;
            color: #3E2723;
        }

        .menu-item .btn-primary:hover {
            background-color: #D4AF37;
            border-color: #C9A227;
        }

        .menu-item .btn-success {
            background-color: #1B4332;
            border-color: #143528;
        }

        .menu-item .btn-success:hover {
            background-color: #2D6A4F;
        }


    </style>
</head>
<body>

<header class="navbar sticky-top d-flex"
        style="background-color:#1B4332;">
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

<div id="sidebar" class="sidebar p-3">
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
    const toggleBtn = document.getElementById("toggleSidebar");

    function setSidebarState(collapsed) {
        sidebar.classList.toggle("collapsed", collapsed);
        content.classList.toggle("expanded", collapsed);
    }

    function handleResize() {
        setSidebarState(window.innerWidth < 576);
    }

    handleResize();

    window.addEventListener("resize", handleResize);

    if (toggleBtn) {
        toggleBtn.addEventListener("click", function () {
            const isCollapsed = sidebar.classList.contains("collapsed");
            setSidebarState(!isCollapsed);
        });
    }
</script>

</body>
</html>
