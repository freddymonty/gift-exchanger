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
    .participant-row{
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 10px;
    }
</style>
    <script>
        function addParticipantField() {
            const container = document.getElementById("participantsContainer");

            const row = document.createElement("div");
            row.className = "participant-row";

            row.innerHTML = '<input type="text" name="participantNames" placeholder="Participant Name" required> <input type="tel" name="participantPhoneNumbers" placeholder="Phone Number" required> ';
            container.appendChild(row);
                        }
    </script>

<div class="createSession-main">
<h1>Create Gift Exchange Session</h1>
<form method="post" action="/createNewSession">
    Session Name:
    <input type="text" name="sessionName" required><br/><br/>

    <h3>Participants</h3>
    <div id="participantsContainer">
        <div class="participant-row">
            <input type="text" name="participantNames" placeholder="Participant Name" required>
            <input type="tel" name="participantPhoneNumbers" placeholder="Phone Number" required>
        </div>
    </div>
    <button type="button" onclick="addParticipantField()">Add Participant</button>

    <br><br>
    <button type="submit">Create Session</button>
</form>
</div>


`
)