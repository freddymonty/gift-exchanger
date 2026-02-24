<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Create Gift Exchange Session</title>
    <script>
        function addParticipantField() {
            const container = document.getElementById("participantsContainer");

            const input = document.createElement("input");
            input.type = "text";
            input.name = "participants";
            input.placeholder = "Participant Name";

            container.appendChild(document.createElement("br"));
            container.appendChild(input);
        }
    </script>
</head>
<body>
<h1>Create Gift Exchange Session</h1>
<form method="post" action="/createNewSession">
    Session Name: <input type="text" name="sessionName" required><br/><br/>

    <h3>Participants</h3>
    <div id="participantsContainer">
        <input type="text" name="participants" placeholder="Participant Name" required>
    </div>
    <button type="button" onclick="addParticipantField()">Add Participant</button>

    <br><br>
    <button type="submit">Create Session</button>
</form>

</body>
</html>