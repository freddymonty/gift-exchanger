@param freddym.webportfolio.Model.User user

@template.layouts.main(
title = "Freddy's Web Portfolio",
content = @`
<style>
    .createSession-main {
        display: flex;
        justify-content: center;
        align-items: start;
        height: 80vh;
        flex-direction: column;
        background-color: lightgray;
    }
</style>
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

<div class="createSession-main">
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
</div>


`
)